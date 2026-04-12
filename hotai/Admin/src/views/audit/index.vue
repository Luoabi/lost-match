<template>
  <div class="audit-container">
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <span>内容审核</span>
          <el-badge :value="pendingCount" :hidden="pendingCount === 0" class="badge">
            <el-tag type="warning">待审核</el-tag>
          </el-badge>
        </div>
      </template>
      
      <!-- 标签页 -->
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane name="pending">
          <template #label>
            <span>待审核</span>
            <el-badge v-if="pendingCount > 0" :value="pendingCount" class="tab-badge" />
          </template>
        </el-tab-pane>
        <el-tab-pane label="已通过" name="approved" />
        <el-tab-pane label="已拒绝" name="rejected" />
      </el-tabs>
      
      <!-- 筛选 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="类型">
          <el-select v-model="searchForm.type" placeholder="请选择" clearable>
            <el-option label="失物" value="lost" />
            <el-option label="拾获" value="found" />
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
      >
        <el-table-column prop="id" label="ID" width="70" align="center" />
        <el-table-column label="类型" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.itemType === 'lost' ? 'warning' : 'success'">
              {{ row.itemType === 'lost' ? '失物' : '拾获' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="物品信息" min-width="200">
          <template #default="{ row }">
            <div class="item-info">
              <el-image
                v-if="row.images && row.images.length > 0"
                :src="row.images[0]"
                fit="cover"
                class="item-image"
                :preview-src-list="row.images"
              >
                <template #error>
                  <div class="image-slot">
                    <el-icon><Picture /></el-icon>
                  </div>
                </template>
              </el-image>
              <div v-else class="item-image image-slot">
                <el-icon><Picture /></el-icon>
              </div>
              <div class="item-detail">
                <div class="item-title">{{ row.title }}</div>
                <el-tag size="small" type="info">{{ row.type }}</el-tag>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="位置" width="150">
          <template #default="{ row }">
            <div class="location-info">
              <el-icon><Location /></el-icon>
              <span>{{ row.location }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="发布人" width="120">
          <template #default="{ row }">
            <div class="user-info">
              <el-avatar :size="30" :src="row.userAvatar">
                <el-icon><User /></el-icon>
              </el-avatar>
              <span class="user-name">{{ row.userName }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="提交时间" width="160" />
        <el-table-column label="审核状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getAuditStatusType(row.auditStatus)">
              {{ row.auditStatusText }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right" align="center">
          <template #default="{ row }">
            <el-button
              v-if="row.auditStatus === 0"
              type="success"
              size="small"
              @click="handleApprove(row)"
            >
              通过
            </el-button>
            <el-button
              v-if="row.auditStatus === 0"
              type="danger"
              size="small"
              @click="handleReject(row)"
            >
              拒绝
            </el-button>
            <el-button
              type="primary"
              size="small"
              @click="handleViewDetail(row)"
            >
              查看
            </el-button>
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
    
    <!-- 拒绝原因弹窗 -->
    <el-dialog v-model="rejectDialogVisible" title="拒绝原因" width="500px">
      <el-form :model="rejectForm" label-width="80px">
        <el-form-item label="拒绝原因">
          <el-input
            v-model="rejectForm.reason"
            type="textarea"
            :rows="4"
            placeholder="请输入拒绝原因"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="rejectDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmReject">确定</el-button>
      </template>
    </el-dialog>
    
    <!-- 详情弹窗 -->
    <el-dialog
      v-model="detailDialogVisible"
      :title="currentRow?.itemType === 'lost' ? '失物详情' : '拾获详情'"
      width="700px"
    >
      <el-descriptions v-if="currentRow" :column="2" border>
        <el-descriptions-item label="ID">{{ currentRow.id }}</el-descriptions-item>
        <el-descriptions-item label="类型">
          <el-tag :type="currentRow.itemType === 'lost' ? 'warning' : 'success'">
            {{ currentRow.itemType === 'lost' ? '失物' : '拾获' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="标题" :span="2">{{ currentRow.title }}</el-descriptions-item>
        <el-descriptions-item label="物品类型" :span="2">
          <el-tag type="info">{{ currentRow.type }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="描述" :span="2">
          {{ currentRow.description || '暂无描述' }}
        </el-descriptions-item>
        <el-descriptions-item label="位置" :span="2">
          <div class="location-info">
            <el-icon><Location /></el-icon>
            <span>{{ currentRow.location }}</span>
          </div>
        </el-descriptions-item>
        <el-descriptions-item label="物品图片" :span="2">
          <div class="image-list">
            <el-image
              v-for="(img, index) in currentRow.images"
              :key="index"
              :src="img"
              :preview-src-list="currentRow.images"
              :initial-index="index"
              fit="cover"
              class="detail-image"
            >
              <template #error>
                <div class="image-slot">
                  <el-icon><Picture /></el-icon>
                </div>
              </template>
            </el-image>
            <span v-if="!currentRow.images || currentRow.images.length === 0" style="color: #999;">
              暂无图片
            </span>
          </div>
        </el-descriptions-item>
        <el-descriptions-item label="审核状态">
          <el-tag :type="getAuditStatusType(currentRow.auditStatus)">
            {{ currentRow.auditStatusText }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="提交时间">
          {{ currentRow.createTime }}
        </el-descriptions-item>
        <el-descriptions-item v-if="currentRow.auditRemark" label="审核备注" :span="2">
          {{ currentRow.auditRemark }}
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAuditList, approveAudit, rejectAudit } from '@/api/audit'

const loading = ref(false)
const tableData = ref([])
const activeTab = ref('pending')
const rejectDialogVisible = ref(false)
const detailDialogVisible = ref(false)
const currentRow = ref(null)

const searchForm = reactive({
  type: ''
})

const rejectForm = reactive({
  reason: ''
})

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

// 计算待审核数量
const pendingCount = computed(() => {
  return pagination.total
})

const getAuditStatusType = (status) => {
  const map = {
    0: 'warning',
    1: 'success',
    2: 'danger'
  }
  return map[status]
}

const fetchData = async () => {
  loading.value = true
  try {
    const auditStatusMap = {
      'pending': 0,
      'approved': 1,
      'rejected': 2
    }
    
    const res = await getAuditList({
      page: pagination.page,
      pageSize: pagination.pageSize,
      auditStatus: auditStatusMap[activeTab.value],
      type: searchForm.type
    })
    
    console.log('审核列表数据:', res)
    tableData.value = res.data.list || []
    pagination.total = res.data.total || 0
  } catch (error) {
    console.error('获取审核列表失败:', error)
    ElMessage.error(error.message || '获取数据失败')
    tableData.value = []
    pagination.total = 0
  } finally {
    loading.value = false
  }
}

const handleTabChange = () => {
  pagination.page = 1
  fetchData()
}

const handleSearch = () => {
  pagination.page = 1
  fetchData()
}

const handleReset = () => {
  searchForm.type = ''
  pagination.page = 1
  fetchData()
}

const handleApprove = (row) => {
  ElMessageBox.confirm('确定要通过这条审核吗？', '审核通过', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'success'
  }).then(async () => {
    try {
      // 获取当前登录用户ID
      const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
      const auditorId = userInfo.id || 1
      
      await approveAudit({ 
        id: row.id, 
        type: row.itemType,
        remark: '审核通过',
        auditorId: auditorId
      })
      ElMessage.success('审核通过')
      fetchData()
    } catch (error) {
      console.error('审核失败:', error)
      ElMessage.error(error.message || '审核失败')
    }
  }).catch(() => {})
}

const handleReject = (row) => {
  currentRow.value = row
  rejectForm.reason = ''
  rejectDialogVisible.value = true
}

const confirmReject = async () => {
  if (!rejectForm.reason) {
    ElMessage.warning('请输入拒绝原因')
    return
  }
  
  try {
    // 获取当前登录用户ID
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    const auditorId = userInfo.id || 1
    
    await rejectAudit({
      id: currentRow.value.id,
      type: currentRow.value.itemType,
      remark: rejectForm.reason,
      auditorId: auditorId
    })
    ElMessage.success('已拒绝')
    rejectDialogVisible.value = false
    fetchData()
  } catch (error) {
    console.error('操作失败:', error)
    ElMessage.error(error.message || '操作失败')
  }
}

const handleViewDetail = (row) => {
  currentRow.value = row
  detailDialogVisible.value = true
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.audit-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.badge {
  margin-left: 10px;
}

.tab-badge {
  margin-left: 5px;
}

.search-form {
  margin-bottom: 20px;
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
  flex-shrink: 0;
}

.image-slot {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
  font-size: 24px;
}

.item-detail {
  flex: 1;
  min-width: 0;
}

.item-title {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 6px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.location-info {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #606266;
  font-size: 14px;
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

.pagination {
  margin-top: 20px;
  justify-content: flex-end;
}

.image-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.detail-image {
  width: 100px;
  height: 100px;
  border-radius: 8px;
  cursor: pointer;
}
</style>
