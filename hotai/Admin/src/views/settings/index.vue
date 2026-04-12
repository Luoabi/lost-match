<template>
  <div class="settings-container">
    <el-row :gutter="20">
      <!-- 基础设置 -->
      <el-col :span="24">
        <el-card shadow="hover" class="setting-card">
          <template #header>
            <span><el-icon><Setting /></el-icon> 基础设置</span>
          </template>
          <el-form :model="basicForm" label-width="120px">
            <el-form-item label="网站名称">
              <el-input v-model="basicForm.siteName" placeholder="请输入网站名称" />
            </el-form-item>
            <el-form-item label="网站Logo">
              <el-upload
                class="logo-uploader"
                action="#"
                :show-file-list="false"
                :auto-upload="false"
              >
                <img v-if="basicForm.logo" :src="basicForm.logo" class="logo" />
                <el-icon v-else class="logo-uploader-icon"><Plus /></el-icon>
              </el-upload>
            </el-form-item>
            <el-form-item label="联系电话">
              <el-input v-model="basicForm.phone" placeholder="请输入联系电话" />
            </el-form-item>
            <el-form-item label="联系邮箱">
              <el-input v-model="basicForm.email" placeholder="请输入联系邮箱" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSaveBasic">保存</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      
      <!-- 物品类型管理 -->
      <el-col :span="12">
        <el-card shadow="hover" class="setting-card">
          <template #header>
            <div class="card-header">
              <span><el-icon><Box /></el-icon> 物品类型管理</span>
              <el-button type="primary" size="small" @click="handleAddType">
                <el-icon><Plus /></el-icon> 添加
              </el-button>
            </div>
          </template>
          <el-table :data="itemTypes" border style="width: 100%">
            <el-table-column prop="name" label="类型名称" />
            <el-table-column prop="sort" label="排序" width="80" align="center" />
            <el-table-column label="状态" width="80" align="center">
              <template #default="{ row }">
                <el-switch v-model="row.enabled" />
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150" align="center">
              <template #default="{ row, $index }">
                <el-button type="primary" size="small" @click="handleEditType(row)">
                  编辑
                </el-button>
                <el-button type="danger" size="small" @click="handleDeleteType($index)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      
      <!-- 地点管理 -->
      <el-col :span="12">
        <el-card shadow="hover" class="setting-card">
          <template #header>
            <div class="card-header">
              <span><el-icon><Location /></el-icon> 地点管理</span>
              <el-button type="primary" size="small" @click="handleAddLocation">
                <el-icon><Plus /></el-icon> 添加
              </el-button>
            </div>
          </template>
          <el-table :data="locations" border style="width: 100%">
            <el-table-column prop="name" label="地点名称" />
            <el-table-column prop="sort" label="排序" width="80" align="center" />
            <el-table-column label="状态" width="80" align="center">
              <template #default="{ row }">
                <el-switch v-model="row.enabled" />
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150" align="center">
              <template #default="{ row, $index }">
                <el-button type="primary" size="small" @click="handleEditLocation(row)">
                  编辑
                </el-button>
                <el-button type="danger" size="small" @click="handleDeleteLocation($index)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      
      <!-- 匹配算法设置 -->
      <el-col :span="24">
        <el-card shadow="hover" class="setting-card">
          <template #header>
            <span><el-icon><Connection /></el-icon> 匹配算法设置</span>
          </template>
          <el-form :model="matchForm" label-width="150px">
            <el-form-item label="图像相似度权重">
              <el-slider v-model="matchForm.imageWeight" :max="1" :step="0.1" show-input />
              <span class="form-tip">当前权重：{{ matchForm.imageWeight }}</span>
            </el-form-item>
            <el-form-item label="位置匹配度权重">
              <el-slider v-model="matchForm.locationWeight" :max="1" :step="0.1" show-input />
              <span class="form-tip">当前权重：{{ matchForm.locationWeight }}</span>
            </el-form-item>
            <el-form-item label="文本匹配度权重">
              <el-slider v-model="matchForm.textWeight" :max="1" :step="0.1" show-input />
              <span class="form-tip">当前权重：{{ matchForm.textWeight }}</span>
            </el-form-item>
            <el-form-item label="权重总和">
              <el-tag :type="weightSum === 1 ? 'success' : 'danger'">
                {{ weightSum.toFixed(1) }} {{ weightSum === 1 ? '(正确)' : '(需调整为1.0)' }}
              </el-tag>
            </el-form-item>
            <el-form-item label="最低匹配阈值">
              <el-input-number v-model="matchForm.threshold" :min="0" :max="1" :step="0.1" />
              <span class="form-tip">低于此分数的匹配结果将不显示</span>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSaveMatch">保存</el-button>
              <el-button @click="handleResetMatch">恢复默认</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      
      <!-- 其他设置 -->
      <el-col :span="24">
        <el-card shadow="hover" class="setting-card">
          <template #header>
            <span><el-icon><Tools /></el-icon> 其他设置</span>
          </template>
          <el-form :model="otherForm" label-width="150px">
            <el-form-item label="悬赏金额范围">
              <el-input-number v-model="otherForm.minReward" :min="0" />
              <span style="margin: 0 10px;">至</span>
              <el-input-number v-model="otherForm.maxReward" :min="0" />
              <span class="form-tip">元</span>
            </el-form-item>
            <el-form-item label="图片上传限制">
              <el-input-number v-model="otherForm.maxImages" :min="1" :max="9" />
              <span class="form-tip">张</span>
            </el-form-item>
            <el-form-item label="单张图片大小">
              <el-input-number v-model="otherForm.maxImageSize" :min="1" :max="10" />
              <span class="form-tip">MB</span>
            </el-form-item>
            <el-form-item label="自动关闭时间">
              <el-input-number v-model="otherForm.autoCloseDay" :min="1" :max="365" />
              <span class="form-tip">天（超过此天数未找回自动关闭）</span>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSaveOther">保存</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 添加/编辑类型弹窗 -->
    <el-dialog v-model="typeDialogVisible" :title="typeDialogTitle" width="500px">
      <el-form :model="typeForm" label-width="100px">
        <el-form-item label="类型名称">
          <el-input v-model="typeForm.name" placeholder="请输入类型名称" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="typeForm.sort" :min="0" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="typeForm.enabled" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="typeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveType">确定</el-button>
      </template>
    </el-dialog>
    
    <!-- 添加/编辑地点弹窗 -->
    <el-dialog v-model="locationDialogVisible" :title="locationDialogTitle" width="500px">
      <el-form :model="locationForm" label-width="100px">
        <el-form-item label="地点名称">
          <el-input v-model="locationForm.name" placeholder="请输入地点名称" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="locationForm.sort" :min="0" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="locationForm.enabled" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="locationDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveLocation">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getSystemConfig,
  updateSystemConfig,
  getItemTypes,
  addItemType,
  updateItemType,
  deleteItemType,
  getLocations,
  addLocation,
  updateLocation,
  deleteLocation
} from '@/api/settings'

// 基础设置
const basicForm = reactive({
  siteName: '校园失物追寻平台',
  logo: 'https://via.placeholder.com/100',
  phone: '400-123-4567',
  email: 'support@example.com'
})

// 物品类型
const itemTypes = ref([
  { name: '手机', sort: 1, enabled: true },
  { name: '钱包', sort: 2, enabled: true },
  { name: '钥匙', sort: 3, enabled: true },
  { name: '书籍', sort: 4, enabled: true },
  { name: '雨伞', sort: 5, enabled: true },
  { name: '水杯', sort: 6, enabled: true },
  { name: '耳机', sort: 7, enabled: true },
  { name: '充电宝', sort: 8, enabled: true }
])

// 地点
const locations = ref([
  { name: '图书馆', sort: 1, enabled: true },
  { name: '食堂', sort: 2, enabled: true },
  { name: '教学楼A栋', sort: 3, enabled: true },
  { name: '教学楼B栋', sort: 4, enabled: true },
  { name: '宿舍楼', sort: 5, enabled: true },
  { name: '操场', sort: 6, enabled: true },
  { name: '体育馆', sort: 7, enabled: true },
  { name: '实验楼', sort: 8, enabled: true }
])

// 匹配算法设置
const matchForm = reactive({
  imageWeight: 0.4,
  locationWeight: 0.3,
  textWeight: 0.3,
  threshold: 0.6
})

const weightSum = computed(() => {
  return matchForm.imageWeight + matchForm.locationWeight + matchForm.textWeight
})

// 其他设置
const otherForm = reactive({
  minReward: 0,
  maxReward: 1000,
  maxImages: 9,
  maxImageSize: 5,
  autoCloseDay: 30
})

// 类型弹窗
const typeDialogVisible = ref(false)
const typeDialogTitle = ref('添加类型')
const typeForm = reactive({
  name: '',
  sort: 0,
  enabled: true
})
const editingTypeIndex = ref(-1)

// 地点弹窗
const locationDialogVisible = ref(false)
const locationDialogTitle = ref('添加地点')
const locationForm = reactive({
  name: '',
  sort: 0,
  enabled: true
})
const editingLocationIndex = ref(-1)

// 初始化数据
const fetchSystemConfig = async () => {
  try {
    const res = await getSystemConfig()
    const config = res.data
    basicForm.siteName = config.siteName || '校园失物追寻平台'
    basicForm.phone = config.phone || ''
    basicForm.email = config.email || ''
    matchForm.imageWeight = parseFloat(config.imageWeight) || 0.4
    matchForm.locationWeight = parseFloat(config.locationWeight) || 0.3
    matchForm.textWeight = parseFloat(config.textWeight) || 0.3
    matchForm.threshold = parseFloat(config.matchThreshold) || 0.6
    otherForm.minReward = parseInt(config.minReward) || 0
    otherForm.maxReward = parseInt(config.maxReward) || 1000
    otherForm.maxImages = parseInt(config.maxImages) || 9
    otherForm.maxImageSize = parseInt(config.maxImageSize) || 5
    otherForm.autoCloseDay = parseInt(config.autoCloseDay) || 30
  } catch (error) {
    console.error('获取系统配置失败', error)
  }
}

const fetchItemTypes = async () => {
  try {
    const res = await getItemTypes()
    // 将后端的 enabled (0/1) 转换为布尔值
    itemTypes.value = (res.data || []).map(item => ({
      ...item,
      enabled: item.enabled === 1 || item.enabled === true
    }))
  } catch (error) {
    console.error('获取物品类型失败', error)
  }
}

const fetchLocations = async () => {
  try {
    const res = await getLocations()
    // 将后端的 enabled (0/1) 转换为布尔值
    locations.value = (res.data || []).map(item => ({
      ...item,
      enabled: item.enabled === 1 || item.enabled === true
    }))
  } catch (error) {
    console.error('获取地点失败', error)
  }
}

const handleSaveBasic = async () => {
  try {
    await updateSystemConfig({
      siteName: basicForm.siteName,
      phone: basicForm.phone,
      email: basicForm.email
    })
    ElMessage.success('基础设置保存成功')
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

const handleAddType = () => {
  typeDialogTitle.value = '添加类型'
  typeForm.name = ''
  typeForm.sort = itemTypes.value.length + 1
  typeForm.enabled = true
  editingTypeIndex.value = -1
  typeDialogVisible.value = true
}

const handleEditType = (row) => {
  typeDialogTitle.value = '编辑类型'
  typeForm.name = row.name
  typeForm.sort = row.sort
  typeForm.enabled = row.enabled
  editingTypeIndex.value = itemTypes.value.indexOf(row)
  typeDialogVisible.value = true
}

const handleSaveType = async () => {
  if (!typeForm.name) {
    ElMessage.warning('请输入类型名称')
    return
  }
  
  try {
    // 将布尔值转换为整数 (true -> 1, false -> 0)
    const data = {
      name: typeForm.name,
      sort: typeForm.sort,
      enabled: typeForm.enabled ? 1 : 0
    }
    
    if (editingTypeIndex.value >= 0) {
      const id = itemTypes.value[editingTypeIndex.value].id
      await updateItemType(id, data)
      ElMessage.success('类型更新成功')
    } else {
      await addItemType(data)
      ElMessage.success('类型添加成功')
    }
    typeDialogVisible.value = false
    fetchItemTypes()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleDeleteType = (index) => {
  ElMessageBox.confirm('确定要删除这个类型吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteItemType(itemTypes.value[index].id)
      ElMessage.success('删除成功')
      fetchItemTypes()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

const handleAddLocation = () => {
  locationDialogTitle.value = '添加地点'
  locationForm.name = ''
  locationForm.sort = locations.value.length + 1
  locationForm.enabled = true
  editingLocationIndex.value = -1
  locationDialogVisible.value = true
}

const handleEditLocation = (row) => {
  locationDialogTitle.value = '编辑地点'
  locationForm.name = row.name
  locationForm.sort = row.sort
  locationForm.enabled = row.enabled
  editingLocationIndex.value = locations.value.indexOf(row)
  locationDialogVisible.value = true
}

const handleSaveLocation = async () => {
  if (!locationForm.name) {
    ElMessage.warning('请输入地点名称')
    return
  }
  
  try {
    // 将布尔值转换为整数 (true -> 1, false -> 0)
    const data = {
      name: locationForm.name,
      sort: locationForm.sort,
      enabled: locationForm.enabled ? 1 : 0
    }
    
    if (editingLocationIndex.value >= 0) {
      const id = locations.value[editingLocationIndex.value].id
      await updateLocation(id, data)
      ElMessage.success('地点更新成功')
    } else {
      await addLocation(data)
      ElMessage.success('地点添加成功')
    }
    locationDialogVisible.value = false
    fetchLocations()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleDeleteLocation = (index) => {
  ElMessageBox.confirm('确定要删除这个地点吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteLocation(locations.value[index].id)
      ElMessage.success('删除成功')
      fetchLocations()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

const handleSaveMatch = async () => {
  if (weightSum.value !== 1) {
    ElMessage.warning('权重总和必须为1.0')
    return
  }
  try {
    await updateSystemConfig({
      imageWeight: matchForm.imageWeight,
      locationWeight: matchForm.locationWeight,
      textWeight: matchForm.textWeight,
      matchThreshold: matchForm.threshold
    })
    ElMessage.success('匹配算法设置保存成功')
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

const handleResetMatch = () => {
  matchForm.imageWeight = 0.4
  matchForm.locationWeight = 0.3
  matchForm.textWeight = 0.3
  matchForm.threshold = 0.6
  ElMessage.success('已恢复默认设置')
}

const handleSaveOther = async () => {
  try {
    await updateSystemConfig({
      minReward: otherForm.minReward,
      maxReward: otherForm.maxReward,
      maxImages: otherForm.maxImages,
      maxImageSize: otherForm.maxImageSize,
      autoCloseDay: otherForm.autoCloseDay
    })
    ElMessage.success('其他设置保存成功')
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

onMounted(() => {
  fetchSystemConfig()
  fetchItemTypes()
  fetchLocations()
})
</script>

<style scoped>
.settings-container {
  padding: 20px;
}

.setting-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: all 0.3s;
}

.logo-uploader:hover {
  border-color: #409EFF;
}

.logo-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  text-align: center;
  line-height: 100px;
}

.logo {
  width: 100px;
  height: 100px;
  display: block;
}

.form-tip {
  margin-left: 10px;
  color: #909399;
  font-size: 12px;
}
</style>
