<template>
  <div class="users-container">
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <span>用户管理</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon> 添加用户
          </el-button>
        </div>
      </template>
      
      <!-- 搜索栏 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="关键词">
          <el-input
            v-model="searchForm.keyword"
            placeholder="请输入用户名或手机号"
            clearable
          />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      
      <!-- 表格 -->
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="70" align="center" />
        <el-table-column label="用户信息" min-width="200">
          <template #default="{ row }">
            <div class="user-info-cell">
              <el-avatar :size="40" :src="row.avatar" />
              <div class="user-detail">
                <div class="user-name-row">
                  <span class="real-name">{{ row.realName }}</span>
                  <el-tag size="small" :type="getRoleType(row.role)">{{ row.roleText }}</el-tag>
                </div>
                <div class="username">@{{ row.username }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="联系方式" width="180">
          <template #default="{ row }">
            <div class="contact-info">
              <div><el-icon><Phone /></el-icon> {{ row.phone }}</div>
              <div><el-icon><Message /></el-icon> {{ row.email }}</div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="department" label="院系/部门" width="130" />
        <el-table-column label="统计数据" width="150" align="center">
          <template #default="{ row }">
            <div class="stats-info">
              <el-tag size="small" type="warning">失物 {{ row.lostCount }}</el-tag>
              <el-tag size="small" type="success">拾获 {{ row.foundCount }}</el-tag>
              <el-tag size="small" type="info">匹配 {{ row.matchSuccessCount }}</el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="信用分" width="100" align="center">
          <template #default="{ row }">
            <el-progress
              type="circle"
              :percentage="row.creditScore"
              :width="50"
              :color="getCreditColor(row.creditScore)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="注册时间" width="160" />
        <el-table-column label="操作" width="180" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        class="pagination"
        @size-change="fetchData"
        @current-change="fetchData"
      />
    </el-card>
    
    <!-- 编辑用户弹窗 -->
    <el-dialog v-model="editDialogVisible" title="编辑用户" width="600px">
      <el-form :model="editForm" label-width="100px">
        <el-form-item label="用户名">
          <el-input v-model="editForm.username" disabled />
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="editForm.realName" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="editForm.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="editForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="editForm.role" placeholder="请选择角色">
            <el-option label="学生" value="student" />
            <el-option label="教师" value="teacher" />
            <el-option label="管理员" value="admin" />
            <el-option label="保洁员" value="cleaner" />
          </el-select>
        </el-form-item>
        <el-form-item label="学号/工号">
          <el-input v-model="editForm.studentId" placeholder="请输入学号或工号" />
        </el-form-item>
        <el-form-item label="院系/部门">
          <el-input v-model="editForm.department" placeholder="请输入院系或部门" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="editForm.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveEdit">保存</el-button>
      </template>
    </el-dialog>
    
    <!-- 添加用户弹窗 -->
    <el-dialog v-model="addDialogVisible" title="添加用户" width="600px">
      <el-form :model="addForm" label-width="100px">
        <el-form-item label="用户名" required>
          <el-input v-model="addForm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" required>
          <el-input v-model="addForm.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="addForm.realName" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="addForm.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="addForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="addForm.role" placeholder="请选择角色">
            <el-option label="学生" value="student" />
            <el-option label="教师" value="teacher" />
            <el-option label="管理员" value="admin" />
            <el-option label="保洁员" value="cleaner" />
          </el-select>
        </el-form-item>
        <el-form-item label="学号/工号">
          <el-input v-model="addForm.studentId" placeholder="请输入学号或工号" />
        </el-form-item>
        <el-form-item label="院系/部门">
          <el-input v-model="addForm.department" placeholder="请输入院系或部门" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="addForm.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveAdd">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getUserList, deleteUser, updateUser, addUser } from '@/api/user'

const loading = ref(false)
const tableData = ref([])
const editDialogVisible = ref(false)
const addDialogVisible = ref(false)

const searchForm = reactive({
  keyword: ''
})

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

const editForm = reactive({
  id: null,
  username: '',
  realName: '',
  phone: '',
  email: '',
  role: '',
  studentId: '',
  department: '',
  status: 1
})

const addForm = reactive({
  username: '',
  password: '',
  realName: '',
  phone: '',
  email: '',
  role: 'student',
  studentId: '',
  department: '',
  status: 1
})

const getRoleType = (role) => {
  const map = {
    admin: 'danger',
    teacher: 'warning',
    student: 'primary',
    cleaner: 'success'
  }
  return map[role] || 'info'
}

const getCreditColor = (score) => {
  if (score >= 90) return '#67C23A'
  if (score >= 80) return '#E6A23C'
  return '#F56C6C'
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getUserList({
      page: pagination.page,
      pageSize: pagination.pageSize,
      ...searchForm
    })
    tableData.value = res.data.list
    pagination.total = res.data.total
  } catch (error) {
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.page = 1
  fetchData()
}

const handleReset = () => {
  searchForm.keyword = ''
  pagination.page = 1
  fetchData()
}

const handleEdit = (row) => {
  editForm.id = row.id
  editForm.username = row.username
  editForm.realName = row.realName
  editForm.phone = row.phone
  editForm.email = row.email
  editForm.role = row.role
  editForm.studentId = row.studentId
  editForm.department = row.department
  editForm.status = row.status
  editDialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除这个用户吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteUser(row.id)
      ElMessage.success('删除成功')
      fetchData()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

const handleAdd = () => {
  addForm.username = ''
  addForm.password = ''
  addForm.realName = ''
  addForm.phone = ''
  addForm.email = ''
  addForm.role = 'student'
  addForm.studentId = ''
  addForm.department = ''
  addForm.status = 1
  addDialogVisible.value = true
}

const handleSaveEdit = async () => {
  try {
    await updateUser(editForm)
    ElMessage.success('更新成功')
    editDialogVisible.value = false
    fetchData()
  } catch (error) {
    ElMessage.error('更新失败')
  }
}

const handleSaveAdd = async () => {
  if (!addForm.username || !addForm.password) {
    ElMessage.warning('请填写用户名和密码')
    return
  }
  try {
    await addUser(addForm)
    ElMessage.success('添加成功')
    addDialogVisible.value = false
    fetchData()
  } catch (error) {
    ElMessage.error('添加失败')
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.users-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  justify-content: flex-end;
}

.user-info-cell {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-detail {
  flex: 1;
}

.user-name-row {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 4px;
}

.real-name {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
}

.username {
  font-size: 12px;
  color: #909399;
}

.contact-info {
  font-size: 13px;
  color: #606266;
}

.contact-info > div {
  display: flex;
  align-items: center;
  gap: 4px;
  margin-bottom: 4px;
}

.stats-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}
</style>
