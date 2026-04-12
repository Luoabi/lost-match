<template>
  <div class="dashboard-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card stat-card-1" @click="goToPage('/lost-items')">
          <div class="stat-icon-wrapper">
            <el-icon class="stat-icon"><Box /></el-icon>
          </div>
          <div class="stat-content">
            <p class="stat-value">{{ statistics.totalLost }}</p>
            <p class="stat-label">失物总数</p>
            <div class="stat-trend">
              <el-icon><Top /></el-icon>
              <span>今日 +{{ statistics.todayLost }}</span>
            </div>
          </div>
        </div>
      </el-col>
      
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card stat-card-2" @click="goToPage('/found-items')">
          <div class="stat-icon-wrapper">
            <el-icon class="stat-icon"><Present /></el-icon>
          </div>
          <div class="stat-content">
            <p class="stat-value">{{ statistics.totalFound }}</p>
            <p class="stat-label">拾获总数</p>
            <div class="stat-trend">
              <el-icon><Top /></el-icon>
              <span>今日 +{{ statistics.todayFound }}</span>
            </div>
          </div>
        </div>
      </el-col>
      
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card stat-card-3" @click="goToPage('/match-records')">
          <div class="stat-icon-wrapper">
            <el-icon class="stat-icon"><Connection /></el-icon>
          </div>
          <div class="stat-content">
            <p class="stat-value">{{ statistics.totalMatched }}</p>
            <p class="stat-label">匹配总数</p>
            <div class="stat-trend">
              <el-icon><Top /></el-icon>
              <span>今日 +{{ statistics.todayMatched }}</span>
            </div>
          </div>
        </div>
      </el-col>
      
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card stat-card-4">
          <div class="stat-icon-wrapper">
            <el-icon class="stat-icon"><TrendCharts /></el-icon>
          </div>
          <div class="stat-content">
            <p class="stat-value">{{ statistics.recoveryRate }}%</p>
            <p class="stat-label">找回率</p>
            <div class="stat-trend success">
              <el-icon><Top /></el-icon>
              <span>较上月 +2.3%</span>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
    
    <!-- 快捷操作 -->
    <el-row :gutter="20" class="mt-20">
      <el-col :span="24">
        <el-card shadow="hover" class="quick-actions-card">
          <template #header>
            <div class="card-header">
              <span><el-icon><Operation /></el-icon> 快捷操作</span>
            </div>
          </template>
          <div class="quick-actions">
            <div class="action-item" @click="goToPage('/lost-items')">
              <el-icon class="action-icon" color="#409EFF"><DocumentAdd /></el-icon>
              <span>发布失物</span>
            </div>
            <div class="action-item" @click="goToPage('/found-items')">
              <el-icon class="action-icon" color="#67C23A"><DocumentAdd /></el-icon>
              <span>发布拾获</span>
            </div>
            <div class="action-item" @click="goToPage('/match-records')">
              <el-icon class="action-icon" color="#E6A23C"><Connection /></el-icon>
              <span>查看匹配</span>
            </div>
            <div class="action-item" @click="goToPage('/statistics')">
              <el-icon class="action-icon" color="#F56C6C"><DataAnalysis /></el-icon>
              <span>数据统计</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 数据趋势 -->
    <el-row :gutter="20" class="mt-20">
      <el-col :span="24">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span><el-icon><TrendCharts /></el-icon> 本周数据趋势</span>
            </div>
          </template>
          <div class="trend-chart">
            <div v-for="(item, index) in weekTrend" :key="index" class="trend-item">
              <div class="trend-bar-wrapper">
                <div class="trend-bar" :style="{ height: item.height }"></div>
              </div>
              <div class="trend-label">{{ item.day }}</div>
              <div class="trend-value">{{ item.count }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getStatisticsOverview } from '@/api/statistics'

const router = useRouter()

const statistics = ref({
  totalLost: 0,
  totalFound: 0,
  totalMatched: 0,
  recoveryRate: 0,
  todayLost: 0,
  todayFound: 0,
  todayMatched: 0
})

const weekTrend = reactive([
  { day: '周一', count: 45, height: '60%' },
  { day: '周二', count: 52, height: '70%' },
  { day: '周三', count: 38, height: '50%' },
  { day: '周四', count: 61, height: '82%' },
  { day: '周五', count: 74, height: '100%' },
  { day: '周六', count: 28, height: '38%' },
  { day: '周日', count: 31, height: '42%' }
])

const fetchStatistics = async () => {
  try {
    const res = await getStatisticsOverview()
    statistics.value = res.data
  } catch (error) {
    console.error('获取统计数据失败', error)
  }
}

const goToPage = (path) => {
  router.push(path)
}

onMounted(() => {
  fetchStatistics()
})
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  padding: 24px;
  color: #fff;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.stat-card-1 {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-card-2 {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-card-3 {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-card-4 {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-icon-wrapper {
  width: 60px;
  height: 60px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-icon {
  font-size: 32px;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  margin: 0 0 8px 0;
}

.stat-label {
  font-size: 14px;
  opacity: 0.9;
  margin: 0 0 8px 0;
}

.stat-trend {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  opacity: 0.8;
}

.stat-trend.success {
  color: #fff;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
}

.quick-actions-card {
  border-radius: 12px;
}

.quick-actions {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 20px;
}

.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  padding: 24px;
  background: #f5f7fa;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s;
}

.action-item:hover {
  background: #e4e7ed;
  transform: translateY(-3px);
}

.action-icon {
  font-size: 36px;
}

.action-item span {
  font-size: 14px;
  color: #606266;
  font-weight: 500;
}

.trend-chart {
  display: flex;
  align-items: flex-end;
  justify-content: space-around;
  height: 200px;
  padding: 20px 0;
}

.trend-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  flex: 1;
}

.trend-bar-wrapper {
  width: 40px;
  height: 150px;
  display: flex;
  align-items: flex-end;
}

.trend-bar {
  width: 100%;
  background: linear-gradient(180deg, #409EFF 0%, #67C23A 100%);
  border-radius: 4px 4px 0 0;
  transition: all 0.3s;
}

.trend-item:hover .trend-bar {
  opacity: 0.8;
}

.trend-label {
  font-size: 12px;
  color: #909399;
}

.trend-value {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
}

.mt-20 {
  margin-top: 20px;
}
</style>
