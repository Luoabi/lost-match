<template>
  <el-dialog
    v-model="visible"
    title="匹配详情"
    width="900px"
    @close="handleClose"
  >
    <div v-loading="loading" class="detail-container">
      <!-- 匹配信息 -->
      <el-card shadow="never" class="match-info-card">
        <template #header>
          <span>匹配信息</span>
        </template>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="匹配ID">{{ detail.id }}</el-descriptions-item>
          <el-descriptions-item label="匹配状态">
            <el-tag :type="getStatusType(detail.status)">{{ detail.statusText }}</el-tag>
          </el-descriptions-item>
          
          <el-descriptions-item label="匹配类型">
            <el-tag :type="detail.matchType === 'auto' ? 'primary' : 'warning'">
              {{ detail.matchType === 'auto' ? '自动匹配' : '手动匹配' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="匹配时间">{{ detail.createTime }}</el-descriptions-item>
          
          <el-descriptions-item label="总匹配得分" :span="2">
            <el-progress
              :percentage="detail.matchScore * 100"
              :color="getScoreColor(detail.matchScore)"
              :stroke-width="20"
            >
              <span class="score-text">{{ (detail.matchScore * 100).toFixed(0) }}%</span>
            </el-progress>
          </el-descriptions-item>
          
          <el-descriptions-item label="图像相似度">
            <el-progress
              :percentage="detail.imageScore * 100"
              color="#409EFF"
              :format="() => (detail.imageScore * 100).toFixed(0) + '%'"
            />
          </el-descriptions-item>
          <el-descriptions-item label="位置匹配度">
            <el-progress
              :percentage="detail.locationScore * 100"
              color="#67C23A"
              :format="() => (detail.locationScore * 100).toFixed(0) + '%'"
            />
          </el-descriptions-item>
          
          <el-descriptions-item label="文本匹配度" :span="2">
            <el-progress
              :percentage="detail.textScore * 100"
              color="#E6A23C"
              :format="() => (detail.textScore * 100).toFixed(0) + '%'"
            />
          </el-descriptions-item>
          
          <el-descriptions-item v-if="detail.confirmTime" label="确认人">
            {{ detail.confirmUserName }}
          </el-descriptions-item>
          <el-descriptions-item v-if="detail.confirmTime" label="确认时间">
            {{ detail.confirmTime }}
          </el-descriptions-item>
          
          <el-descriptions-item v-if="detail.feedback" label="用户反馈" :span="2">
            <div class="feedback-content">
              <el-rate v-model="detail.feedbackScore" disabled />
              <p>{{ detail.feedback }}</p>
            </div>
          </el-descriptions-item>
        </el-descriptions>
      </el-card>
      
      <!-- 失物和拾获对比 -->
      <el-row :gutter="20" class="items-compare">
        <!-- 失物信息 -->
        <el-col :span="12">
          <el-card shadow="never" class="item-card lost-card">
            <template #header>
              <div class="card-header">
                <el-icon><Box /></el-icon>
                <span>失物信息</span>
              </div>
            </template>
            <el-descriptions :column="1" border>
              <el-descriptions-item label="标题">{{ detail.lostItemTitle }}</el-descriptions-item>
              <el-descriptions-item label="类型">
                <el-tag type="warning">{{ detail.lostItemType }}</el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="描述">
                {{ detail.lostItemDescription || '暂无描述' }}
              </el-descriptions-item>
              <el-descriptions-item label="位置">
                <div class="location-info">
                  <el-icon><Location /></el-icon>
                  <span>{{ detail.lostItemLocation }}</span>
                </div>
              </el-descriptions-item>
              <el-descriptions-item label="时间">{{ detail.lostTime }}</el-descriptions-item>
              <el-descriptions-item label="图片">
                <div class="image-list">
                  <el-image
                    v-for="(img, index) in detail.lostItemImages"
                    :key="index"
                    :src="img"
                    :preview-src-list="detail.lostItemImages"
                    :initial-index="index"
                    fit="cover"
                    class="item-image"
                  />
                </div>
              </el-descriptions-item>
            </el-descriptions>
          </el-card>
        </el-col>
        
        <!-- 拾获信息 -->
        <el-col :span="12">
          <el-card shadow="never" class="item-card found-card">
            <template #header>
              <div class="card-header">
                <el-icon><Present /></el-icon>
                <span>拾获信息</span>
              </div>
            </template>
            <el-descriptions :column="1" border>
              <el-descriptions-item label="标题">{{ detail.foundItemTitle }}</el-descriptions-item>
              <el-descriptions-item label="类型">
                <el-tag type="success">{{ detail.foundItemType }}</el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="描述">
                {{ detail.foundItemDescription || '暂无描述' }}
              </el-descriptions-item>
              <el-descriptions-item label="位置">
                <div class="location-info">
                  <el-icon><Location /></el-icon>
                  <span>{{ detail.foundItemLocation }}</span>
                </div>
              </el-descriptions-item>
              <el-descriptions-item label="时间">{{ detail.foundTime }}</el-descriptions-item>
              <el-descriptions-item label="图片">
                <div class="image-list">
                  <el-image
                    v-for="(img, index) in detail.foundItemImages"
                    :key="index"
                    :src="img"
                    :preview-src-list="detail.foundItemImages"
                    :initial-index="index"
                    fit="cover"
                    class="item-image"
                  />
                </div>
              </el-descriptions-item>
            </el-descriptions>
          </el-card>
        </el-col>
      </el-row>
    </div>
    
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">关闭</el-button>
        <el-button
          v-if="detail.status === 0"
          type="success"
          @click="handleConfirm"
        >
          确认匹配
        </el-button>
        <el-button
          v-if="detail.status === 0"
          type="danger"
          @click="handleReject"
        >
          不匹配
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { getMatchDetail, confirmMatch, rejectMatch } from '@/api/match'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  matchId: {
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
  if (val && props.matchId) {
    fetchDetail()
  }
})

watch(visible, (val) => {
  emit('update:modelValue', val)
})

const fetchDetail = async () => {
  loading.value = true
  try {
    const res = await getMatchDetail(props.matchId)
    detail.value = res.data
  } catch (error) {
    ElMessage.error('获取详情失败')
  } finally {
    loading.value = false
  }
}

const getStatusType = (status) => {
  const map = { 0: 'warning', 1: 'success', 2: 'danger' }
  return map[status]
}

const getScoreColor = (score) => {
  if (score >= 0.8) return '#67C23A'
  if (score >= 0.6) return '#E6A23C'
  return '#F56C6C'
}

const handleConfirm = async () => {
  try {
    await confirmMatch(props.matchId, null)
    ElMessage.success('确认成功')
    emit('refresh')
    handleClose()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleReject = async () => {
  try {
    await rejectMatch(props.matchId)
    ElMessage.success('已标记为不匹配')
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
  max-height: 70vh;
  overflow-y: auto;
}

.match-info-card {
  margin-bottom: 20px;
}

.score-text {
  font-size: 14px;
  font-weight: bold;
}

.items-compare {
  margin-top: 20px;
}

.item-card {
  height: 100%;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
}

.lost-card :deep(.el-card__header) {
  background-color: #fef0f0;
  color: #f56c6c;
}

.found-card :deep(.el-card__header) {
  background-color: #f0f9ff;
  color: #67c23a;
}

.location-info {
  display: flex;
  align-items: center;
  gap: 4px;
}

.image-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.item-image {
  width: 80px;
  height: 80px;
  border-radius: 6px;
  cursor: pointer;
}

.feedback-content {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.feedback-content p {
  margin: 0;
  color: #606266;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>
