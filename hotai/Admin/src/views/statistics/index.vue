<template>
  <div class="statistics-container">
    <el-row :gutter="20">
      <!-- 失物类型分布 -->
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <span>失物类型分布</span>
          </template>
          <div class="chart-container">
            <div v-for="item in itemTypeData" :key="item.type" class="chart-item">
              <span class="chart-label">{{ item.type }}</span>
              <el-progress
                :percentage="(item.count / maxItemCount) * 100"
                :format="() => item.count"
              />
            </div>
          </div>
        </el-card>
      </el-col>
      
      <!-- 高频丢失区域 -->
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <span>高频丢失区域</span>
          </template>
          <div class="chart-container">
            <div v-for="item in locationData" :key="item.location" class="chart-item">
              <span class="chart-label">{{ item.location }}</span>
              <el-progress
                :percentage="(item.count / maxLocationCount) * 100"
                :format="() => item.count"
                color="#67C23A"
              />
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 找回率趋势 -->
    <el-row :gutter="20" class="mt-20">
      <el-col :span="24">
        <el-card shadow="hover">
          <template #header>
            <span>找回率趋势</span>
          </template>
          <div class="trend-container">
            <div v-for="item in trendData" :key="item.date" class="trend-item">
              <div class="trend-date">{{ item.date }}</div>
              <div class="trend-rate">{{ item.rate }}%</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getItemTypeDistribution, getLocationDistribution, getRecoveryTrend } from '@/api/statistics'

const itemTypeData = ref([])
const locationData = ref([])
const trendData = ref([])

const maxItemCount = computed(() => {
  return Math.max(...itemTypeData.value.map(item => item.count), 1)
})

const maxLocationCount = computed(() => {
  return Math.max(...locationData.value.map(item => item.count), 1)
})

const fetchItemTypeData = async () => {
  try {
    const res = await getItemTypeDistribution()
    itemTypeData.value = res.data || []
  } catch (error) {
    console.error('获取失物类型数据失败', error)
    // 使用默认数据
    itemTypeData.value = [
      { type: '手机', count: 45 },
      { type: '钱包', count: 32 },
      { type: '钥匙', count: 28 },
      { type: '书籍', count: 25 },
      { type: '雨伞', count: 20 }
    ]
  }
}

const fetchLocationData = async () => {
  try {
    const res = await getLocationDistribution()
    locationData.value = res.data || []
  } catch (error) {
    console.error('获取位置数据失败', error)
    // 使用默认数据
    locationData.value = [
      { location: '图书馆', count: 52 },
      { location: '食堂', count: 38 },
      { location: '教学楼', count: 35 },
      { location: '宿舍楼', count: 28 },
      { location: '操场', count: 15 }
    ]
  }
}

const fetchTrendData = async () => {
  try {
    const res = await getRecoveryTrend()
    trendData.value = res.data || []
  } catch (error) {
    console.error('获取趋势数据失败', error)
    // 使用默认数据
    trendData.value = [
      { date: '01-01', rate: 65 },
      { date: '01-02', rate: 68 },
      { date: '01-03', rate: 72 },
      { date: '01-04', rate: 70 },
      { date: '01-05', rate: 75 },
      { date: '01-06', rate: 73 },
      { date: '01-07', rate: 78 }
    ]
  }
}

onMounted(() => {
  fetchItemTypeData()
  fetchLocationData()
  fetchTrendData()
})
</script>

<style scoped>
.statistics-container {
  padding: 20px;
}

.chart-container {
  padding: 20px 0;
}

.chart-item {
  margin-bottom: 20px;
}

.chart-label {
  display: inline-block;
  width: 100px;
  font-size: 14px;
  color: #666;
}

.trend-container {
  display: flex;
  justify-content: space-around;
  padding: 20px 0;
}

.trend-item {
  text-align: center;
}

.trend-date {
  font-size: 14px;
  color: #666;
  margin-bottom: 10px;
}

.trend-rate {
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
}
</style>
