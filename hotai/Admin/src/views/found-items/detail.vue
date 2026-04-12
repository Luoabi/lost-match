<template>
  <el-dialog
    v-model="visible"
    title="拾获详情"
    width="800px"
    @close="handleClose"
  >
    <div v-loading="loading" class="detail-container">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="ID">{{ detail.id }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(detail.status)">{{ detail.statusText }}</el-tag>
        </el-descriptions-item>
        
        <el-descriptions-item label="标题" :span="2">{{ detail.title }}</el-descriptions-item>
        
        <el-descriptions-item label="物品类型" :span="2">
          <el-tag type="success">{{ detail.type }}</el-tag>
        </el-descriptions-item>
        
        <el-descriptions-item label="详细描述" :span="2">
          {{ detail.description || '暂无描述' }}
        </el-descriptions-item>
        
        <el-descriptions-item label="拾获位置" :span="2">
          <div class="location-info">
            <el-icon><Location /></el-icon>
            <span>{{ detail.location }}</span>
            <span v-if="detail.locationDetail" class="location-detail">（{{ detail.locationDetail }}）</span>
          </div>
        </el-descriptions-item>
        
        <el-descriptions-item label="拾获时间" :span="2">
          {{ detail.foundTime }}
        </el-descriptions-item>
        
        <el-descriptions-item label="联系人">{{ detail.contactName }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ detail.contactPhone }}</el-descriptions-item>
        
        <el-descriptions-item label="拾获人">{{ detail.userName }}</el-descriptions-item>
        <el-descriptions-item label="浏览次数">
          <el-tag type="info">{{ detail.viewCount }} 次</el-tag>
        </el-descriptions-item>
        
        <el-descriptions-item label="关键词" :span="2">
          <el-tag
            v-for="(keyword, index) in detail.keywords"
            :key="index"
            size="small"
            style="margin-right: 8px;"
          >
            {{ keyword }}
          </el-tag>
          <span v-if="!detail.keywords || detail.keywords.length === 0" style="color: #999;">暂无关键词</span>
        </el-descriptions-item>
        
        <el-descriptions-item label="物品图片" :span="2">
          <div class="image-list">
            <el-image
              v-for="(img, index) in detail.images"
              :key="index"
              :src="img"
              :preview-src-list="detail.images"
              :initial-index="index"
              fit="cover"
              class="detail-image"
            />
            <span v-if="!detail.images || detail.images.length === 0" style="color: #999;">暂无图片</span>
          </div>
        </el-descriptions-item>
        
        <el-descriptions-item label="审核状态">
          <el-tag :type="getAuditStatusType(detail.auditStatus)">
            {{ detail.auditStatusText }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="审核时间">
          {{ detail.auditTime || '未审核' }}
        </el-descriptions-item>
        
        <el-descriptions-item v-if="detail.auditRemark" label="审核备注" :span="2">
          {{ detail.auditRemark }}
        </el-descriptions-item>
        
        <el-descriptions-item label="发布时间">{{ detail.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ detail.updateTime }}</el-descriptions-item>
      </el-descriptions>
    </div>
    
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">关闭</el-button>
        <el-button
          v-if="detail.auditStatus === 0"
          type="success"
          @click="handleAudit(1)"
        >
          审核通过
        </el-button>
        <el-button
          v-if="detail.auditStatus === 0"
          type="danger"
          @click="handleAudit(2)"
        >
          审核拒绝
        </el-button>
        <el-button
          v-if="detail.status === 0"
          type="primary"
          @click="handleUpdateStatus(1)"
        >
          标记为已归还
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getFoundItemDetail, updateFoundItemStatus } from '@/api/foundItem'
import { approveAudit, rejectAudit } from '@/api/audit'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  itemId: {
    type: Number,
    default: null
  }
})

const emit = defineEmits(['update:modelValue', 'refresh'])

const visible = ref(false)
const loading = ref(false)
const detail = ref({})

watch(() => props.modelValue, (val) => {
  visible.value = val
  if (val && props.itemId) {
    fetchDetail()
  }
})

watch(visible, (val) => {
  emit('update:modelValue', val)
})

const fetchDetail = async () => {
  loading.value = true
  try {
    const res = await getFoundItemDetail(props.itemId)
    detail.value = res.data
  } catch (error) {
    ElMessage.error('获取详情失败')
  } finally {
    loading.value = false
  }
}

const getStatusType = (status) => {
  const map = { 0: 'warning', 1: 'success', 2: 'info' }
  return map[status]
}

const getAuditStatusType = (status) => {
  const map = { 0: 'warning', 1: 'success', 2: 'danger' }
  return map[status]
}

const handleAudit = async (status) => {
  // 获取当前登录用户ID
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  const auditorId = userInfo.id || 1
  
  if (status === 2) {
    const { value } = await ElMessageBox.prompt('请输入拒绝原因', '审核拒绝', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputPattern: /.+/,
      inputErrorMessage: '请输入拒绝原因'
    }).catch(() => ({ value: null }))
    
    if (!value) return
    
    try {
      await rejectAudit({ 
        id: props.itemId, 
        type: 'found', 
        remark: value,
        auditorId: auditorId
      })
      ElMessage.success('审核拒绝成功')
      emit('refresh')
      handleClose()
    } catch (error) {
      console.error('审核拒绝失败:', error)
      ElMessage.error(error.message || '操作失败')
    }
  } else {
    try {
      await approveAudit({ 
        id: props.itemId, 
        type: 'found',
        remark: '审核通过',
        auditorId: auditorId
      })
      ElMessage.success('审核通过')
      emit('refresh')
      handleClose()
    } catch (error) {
      console.error('审核通过失败:', error)
      ElMessage.error(error.message || '操作失败')
    }
  }
}

const handleUpdateStatus = async (status) => {
  try {
    await updateFoundItemStatus(props.itemId, status)
    ElMessage.success('状态更新成功')
    emit('refresh')
    handleClose()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleClose = () => {
  visible.value = false
}
</script>

<style scoped>
.detail-container {
  min-height: 200px;
}

.location-info {
  display: flex;
  align-items: center;
  gap: 4px;
}

.location-detail {
  color: #909399;
  font-size: 13px;
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

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>
