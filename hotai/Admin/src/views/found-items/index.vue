<template>
  <div class="found-items-container">
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <span>拾获管理</span>
          <div class="header-actions">
            <el-button
              type="danger"
              :disabled="selectedIds.length === 0"
              @click="handleBatchDelete"
            >
              批量删除 ({{ selectedIds.length }})
            </el-button>
            <el-button
              type="success"
              :disabled="selectedIds.length === 0"
              @click="handleBatchAudit"
            >
              批量审核
            </el-button>
          </div>
        </div>
      </template>
      
      <!-- 搜索栏 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="关键词">
          <el-input
            v-model="searchForm.keyword"
            placeholder="请输入关键词"
            clearable
          />
        </el-form-item>
        
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="待认领" :value="0" />
            <el-option label="已归还" :value="1" />
            <el-option label="已关闭" :value="2" />
          </el-select>
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
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column prop="id" label="ID" width="70" align="center" />
        <el-table-column label="物品信息" min-width="200">
          <template #default="{ row }">
            <div class="item-info">
              <el-image
                :src="row.images[0]"
                fit="cover"
                class="item-image"
                :preview-src-list="row.images"
              />
              <div class="item-detail">
                <div class="item-title">{{ row.title }}</div>
                <el-tag size="small" type="success">{{ row.type }}</el-tag>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="位置信息" width="180">
          <template #default="{ row }">
            <div class="location-info">
              <el-icon><Location /></el-icon>
              <span>{{ row.location }}</span>
            </div>
            <div class="location-detail">{{ row.locationDetail }}</div>
          </template>
        </el-table-column>
        <el-table-column label="拾获人" width="150">
          <template #default="{ row }">
            <div class="user-info">
              <el-avatar :size="30" :src="row.userAvatar" />
              <span class="user-name">{{ row.userName }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="viewCount" label="浏览" width="80" align="center">
          <template #default="{ row }">
            <div class="view-count">
              <el-icon><View /></el-icon>
              <span>{{ row.viewCount }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="statusText" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ row.statusText }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="发布时间" width="160" />
        <el-table-column label="操作" width="180" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleView(row)">查看</el-button>
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
    
    <!-- 详情弹窗 -->
    <DetailDialog v-model="detailVisible" :item-id="currentItemId" @refresh="fetchData" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getFoundItemList, deleteFoundItem, batchDeleteFoundItems, batchAuditFoundItems } from '@/api/foundItem'
import DetailDialog from './detail.vue'

const loading = ref(false)
const tableData = ref([])
const selectedIds = ref([])
const detailVisible = ref(false)
const currentItemId = ref(null)

const searchForm = reactive({
  keyword: '',
  status: ''
})

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

const getStatusType = (status) => {
  const map = {
    0: 'warning',
    1: 'success',
    2: 'info'
  }
  return map[status]
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getFoundItemList({
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
  searchForm.status = ''
  pagination.page = 1
  fetchData()
}

const handleView = (row) => {
  currentItemId.value = row.id
  detailVisible.value = true
}

const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id)
}

const handleBatchDelete = () => {
  ElMessageBox.confirm(`确定要删除选中的 ${selectedIds.value.length} 条记录吗？`, '批量删除', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await batchDeleteFoundItems(selectedIds.value)
      ElMessage.success('批量删除成功')
      selectedIds.value = []
      fetchData()
    } catch (error) {
      ElMessage.error('批量删除失败')
    }
  }).catch(() => {})
}

const handleBatchAudit = () => {
  ElMessageBox.confirm(`确定要审核通过选中的 ${selectedIds.value.length} 条记录吗？`, '批量审核', {
    confirmButtonText: '通过',
    cancelButtonText: '取消',
    type: 'success'
  }).then(async () => {
    try {
      // 获取当前登录用户ID
      const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
      const auditorId = userInfo.id || 1 // 默认使用1作为审核人ID
      
      await batchAuditFoundItems({ 
        ids: selectedIds.value, 
        auditStatus: 1, // 1表示审核通过
        auditRemark: '批量审核通过',
        auditorId: auditorId
      })
      ElMessage.success('批量审核成功')
      selectedIds.value = []
      fetchData()
    } catch (error) {
      console.error('批量审核失败:', error)
      ElMessage.error(error.message || '批量审核失败')
    }
  }).catch(() => {})
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除这条记录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteFoundItem(row.id)
      ElMessage.success('删除成功')
      fetchData()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.found-items-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.search-form {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  justify-content: flex-end;
}

.item-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.item-image {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  cursor: pointer;
}

.item-detail {
  flex: 1;
}

.item-title {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 6px;
}

.location-info {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #606266;
  font-size: 14px;
  margin-bottom: 4px;
}

.location-detail {
  color: #909399;
  font-size: 12px;
  padding-left: 20px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.user-name {
  font-size: 14px;
  color: #606266;
}

.view-count {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  color: #909399;
  font-size: 13px;
}
</style>
