<template>
  <div class="match-records-container">
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <span>匹配记录</span>
        </div>
      </template>
      
      <!-- 表格 -->
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="lostItemTitle" label="失物" min-width="150" />
        <el-table-column prop="foundItemTitle" label="拾获物" min-width="150" />
        <el-table-column prop="matchScore" label="总分" width="100">
          <template #default="{ row }">
            <el-progress
              :percentage="row.matchScore * 100"
              :color="getScoreColor(row.matchScore)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="imageScore" label="图像分" width="100" />
        <el-table-column prop="locationScore" label="位置分" width="100" />
        <el-table-column prop="textScore" label="文本分" width="100" />
        <el-table-column prop="statusText" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ row.statusText }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="匹配时间" width="180" />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleView(row)">查看</el-button>
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
    <DetailDialog v-model="detailVisible" :match-id="currentMatchId" @refresh="fetchData" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getMatchList } from '@/api/match'
import DetailDialog from './detail.vue'

const loading = ref(false)
const tableData = ref([])
const detailVisible = ref(false)
const currentMatchId = ref(null)

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

const getStatusType = (status) => {
  const map = {
    0: 'warning',
    1: 'success',
    2: 'danger'
  }
  return map[status]
}

const getScoreColor = (score) => {
  if (score >= 0.8) return '#67C23A'
  if (score >= 0.6) return '#E6A23C'
  return '#F56C6C'
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getMatchList({
      page: pagination.page,
      pageSize: pagination.pageSize
    })
    tableData.value = res.data.list
    pagination.total = res.data.total
  } catch (error) {
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

const handleView = (row) => {
  currentMatchId.value = row.id
  detailVisible.value = true
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.match-records-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination {
  margin-top: 20px;
  justify-content: flex-end;
}
</style>
