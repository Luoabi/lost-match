<template>
  <div class="navbar-container">
    <div class="left-menu">
      <el-icon class="hamburger" @click="toggleSidebar">
        <Fold v-if="sidebar.opened" />
        <Expand v-else />
      </el-icon>
      <div class="breadcrumb-container">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item :to="{ path: '/dashboard' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item v-if="currentRoute">{{ currentRoute }}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
    </div>
    
    <div class="right-menu">
      <!-- 全屏按钮 -->
      <el-tooltip content="全屏" placement="bottom">
        <el-icon class="icon-btn" @click="toggleFullscreen">
          <FullScreen />
        </el-icon>
      </el-tooltip>
      
      <!-- 刷新按钮 -->
      <el-tooltip content="刷新" placement="bottom">
        <el-icon class="icon-btn" @click="refreshPage">
          <Refresh />
        </el-icon>
      </el-tooltip>
      
      <!-- 用户下拉菜单 -->
      <el-dropdown @command="handleCommand" class="user-dropdown">
        <div class="avatar-container">
          <el-avatar :size="36" :src="userInfo?.avatar">
            <el-icon><User /></el-icon>
          </el-avatar>
          <span class="username">{{ userInfo?.username || '管理员' }}</span>
          <el-icon class="dropdown-icon"><CaretBottom /></el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="profile">
              <el-icon><User /></el-icon>
              <span>个人中心</span>
            </el-dropdown-item>
            <el-dropdown-item command="settings">
              <el-icon><Setting /></el-icon>
              <span>系统设置</span>
            </el-dropdown-item>
            <el-dropdown-item command="logout" divided>
              <el-icon><SwitchButton /></el-icon>
              <span>退出登录</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
    
    <!-- 个人中心弹窗 -->
    <el-dialog
      v-model="profileDialogVisible"
      title="个人中心"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-tabs v-model="activeTab">
        <!-- 个人信息 -->
        <el-tab-pane label="个人信息" name="info">
          <el-form :model="profileForm" label-width="100px" style="padding: 20px 20px 0">
            <el-form-item label="头像">
              <el-upload
                class="avatar-uploader"
                action="#"
                :show-file-list="false"
                :auto-upload="false"
                :on-change="handleAvatarChange"
                accept="image/*"
              >
                <img v-if="profileForm.avatar" :src="profileForm.avatar" class="avatar-preview" />
                <div v-else class="avatar-placeholder">
                  <el-icon><User /></el-icon>
                  <div class="upload-text">点击上传头像</div>
                </div>
              </el-upload>
              <div class="form-tip">支持jpg、png格式，建议尺寸100x100px，大小不超过2MB</div>
            </el-form-item>
            <el-form-item label="用户名">
              <el-input v-model="profileForm.username" placeholder="请输入用户名" />
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="profileForm.email" placeholder="请输入邮箱" />
            </el-form-item>
            <el-form-item label="手机号">
              <el-input v-model="profileForm.phone" placeholder="请输入手机号" />
            </el-form-item>
            <el-form-item label="角色">
              <el-tag>{{ userInfo?.role === 'admin' ? '管理员' : '普通用户' }}</el-tag>
            </el-form-item>
            <el-form-item label="注册时间">
              <span>{{ userInfo?.createTime || '-' }}</span>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        
        <!-- 修改密码 -->
        <el-tab-pane label="修改密码" name="password">
          <el-form :model="passwordForm" label-width="100px" style="padding: 20px 20px 0">
            <el-form-item label="原密码">
              <el-input
                v-model="passwordForm.oldPassword"
                type="password"
                placeholder="请输入原密码"
                show-password
              />
            </el-form-item>
            <el-form-item label="新密码">
              <el-input
                v-model="passwordForm.newPassword"
                type="password"
                placeholder="请输入新密码（至少6位）"
                show-password
              />
            </el-form-item>
            <el-form-item label="确认密码">
              <el-input
                v-model="passwordForm.confirmPassword"
                type="password"
                placeholder="请再次输入新密码"
                show-password
              />
            </el-form-item>
            <el-form-item>
              <el-alert
                title="密码修改后需要重新登录"
                type="warning"
                :closable="false"
                show-icon
              />
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
      
      <template #footer>
        <el-button @click="profileDialogVisible = false">取消</el-button>
        <el-button v-if="activeTab === 'info'" type="primary" @click="handleSaveProfile">
          保存信息
        </el-button>
        <el-button v-if="activeTab === 'password'" type="primary" @click="handleChangePassword">
          修改密码
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '@/store/modules/user'
import { useAppStore } from '@/store/modules/app'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const appStore = useAppStore()

const userInfo = computed(() => userStore.userInfo)
const sidebar = computed(() => appStore.sidebar)

const routeNameMap = {
  '/dashboard': '首页',
  '/lost-items': '失物管理',
  '/found-items': '拾获管理',
  '/match-records': '匹配记录',
  '/users': '用户管理',
  '/audit': '内容审核',
  '/logs': '操作日志',
  '/statistics': '数据统计',
  '/settings': '系统设置'
}

const currentRoute = computed(() => {
  return routeNameMap[route.path] || ''
})

const toggleSidebar = () => {
  appStore.toggleSidebar()
}

const toggleFullscreen = () => {
  if (!document.fullscreenElement) {
    document.documentElement.requestFullscreen()
    ElMessage.success('已进入全屏模式')
  } else {
    document.exitFullscreen()
    ElMessage.success('已退出全屏模式')
  }
}

const refreshPage = () => {
  window.location.reload()
}

// 个人中心弹窗
const profileDialogVisible = ref(false)
const profileForm = ref({
  username: '',
  email: '',
  phone: '',
  avatar: ''
})
const passwordForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})
const activeTab = ref('info')

const openProfileDialog = () => {
  profileForm.value = {
    username: userInfo.value?.username || '',
    email: userInfo.value?.email || '',
    phone: userInfo.value?.phone || '',
    avatar: userInfo.value?.avatar || ''
  }
  activeTab.value = 'info'
  profileDialogVisible.value = true
}

const handleAvatarChange = async (file) => {
  const isImage = file.raw.type.startsWith('image/')
  const isLt2M = file.raw.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!')
    return
  }

  try {
    // 这里需要导入 uploadFile API
    const { uploadFile } = await import('@/api/file')
    const res = await uploadFile(file.raw)
    
    console.log('=== 文件上传响应 ===')
    console.log('完整响应:', res)
    console.log('res.data:', res.data)
    console.log('res.data.url:', res.data.url)
    console.log('==================')
    
    if (res.code === 200) {
      profileForm.value.avatar = res.data.url
      console.log('设置的avatar值:', profileForm.value.avatar)
      ElMessage.success('头像上传成功')
    } else {
      ElMessage.error(res.message || '头像上传失败')
    }
  } catch (error) {
    console.error('头像上传失败', error)
    ElMessage.error('头像上传失败')
  }
}

const handleSaveProfile = async () => {
  try {
    console.log('=== 保存个人信息 ===')
    console.log('profileForm.value.avatar:', profileForm.value.avatar)
    
    const { updateUserProfile } = await import('@/api/user')
    const result = await updateUserProfile({
      id: userInfo.value.id,
      username: profileForm.value.username,
      email: profileForm.value.email,
      phone: profileForm.value.phone,
      avatar: profileForm.value.avatar
    })
    
    console.log('保存结果:', result)
    console.log('==================')
    
    // 更新本地用户信息
    await userStore.getUserInfo()
    ElMessage.success('个人信息更新成功')
    profileDialogVisible.value = false
  } catch (error) {
    console.error('更新失败:', error)
    ElMessage.error('更新失败')
  }
}

const handleChangePassword = async () => {
  if (!passwordForm.value.oldPassword) {
    ElMessage.warning('请输入原密码')
    return
  }
  if (!passwordForm.value.newPassword) {
    ElMessage.warning('请输入新密码')
    return
  }
  if (passwordForm.value.newPassword.length < 6) {
    ElMessage.warning('新密码长度不能少于6位')
    return
  }
  if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
    ElMessage.warning('两次输入的密码不一致')
    return
  }

  try {
    const { changePassword } = await import('@/api/user')
    await changePassword({
      id: userInfo.value.id,
      oldPassword: passwordForm.value.oldPassword,
      newPassword: passwordForm.value.newPassword
    })
    
    ElMessage.success('密码修改成功，请重新登录')
    passwordForm.value = {
      oldPassword: '',
      newPassword: '',
      confirmPassword: ''
    }
    
    // 延迟退出登录
    setTimeout(() => {
      userStore.logout()
      router.push('/login')
    }, 1500)
  } catch (error) {
    ElMessage.error(error.message || '密码修改失败')
  }
}

const handleCommand = (command) => {
  if (command === 'logout') {
    ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      userStore.logout()
      router.push('/login')
      ElMessage.success('退出成功')
    }).catch(() => {})
  } else if (command === 'profile') {
    openProfileDialog()
  } else if (command === 'settings') {
    router.push('/settings')
  }
}
</script>

<style scoped>
.navbar-container {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  background: #fff;
}

.left-menu {
  display: flex;
  align-items: center;
  gap: 20px;
  flex: 1;
  overflow: hidden;
}

.hamburger {
  font-size: 22px;
  cursor: pointer;
  transition: all 0.3s;
  color: #606266;
  padding: 8px;
  border-radius: 6px;
}

.hamburger:hover {
  background: #f5f7fa;
  color: #409EFF;
}

.breadcrumb-container {
  flex: 1;
  overflow: hidden;
}

.right-menu {
  display: flex;
  align-items: center;
  gap: 15px;
}

.icon-btn {
  font-size: 20px;
  cursor: pointer;
  transition: all 0.3s;
  color: #606266;
  padding: 8px;
  border-radius: 6px;
}

.icon-btn:hover {
  background: #f5f7fa;
  color: #409EFF;
}

.user-dropdown {
  cursor: pointer;
}

.avatar-container {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 6px 12px;
  border-radius: 8px;
  transition: all 0.3s;
}

.avatar-container:hover {
  background: #f5f7fa;
}

.username {
  font-size: 14px;
  color: #303133;
  font-weight: 500;
  max-width: 120px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.dropdown-icon {
  font-size: 12px;
  color: #909399;
  transition: all 0.3s;
}

.avatar-container:hover .dropdown-icon {
  color: #409EFF;
}

/* 下拉菜单项图标 */
:deep(.el-dropdown-menu__item) {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
}

:deep(.el-dropdown-menu__item .el-icon) {
  font-size: 16px;
}

/* 响应式 */
@media screen and (max-width: 768px) {
  .navbar-container {
    padding: 0 15px;
  }
  
  .breadcrumb-container {
    display: none;
  }
  
  .username {
    display: none;
  }
  
  .icon-btn {
    font-size: 18px;
    padding: 6px;
  }
  
  .right-menu {
    gap: 10px;
  }
}

@media screen and (max-width: 576px) {
  .navbar-container {
    padding: 0 10px;
  }
  
  .icon-btn:not(.hamburger) {
    display: none;
  }
}

/* 个人中心弹窗样式 */
.avatar-uploader {
  display: inline-block;
}

.avatar-preview {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
  cursor: pointer;
  border: 2px solid #dcdfe6;
  transition: all 0.3s;
}

.avatar-preview:hover {
  border-color: #409EFF;
  transform: scale(1.05);
}

.avatar-placeholder {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  border: 2px dashed #dcdfe6;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s;
  background: #fafafa;
}

.avatar-placeholder:hover {
  border-color: #409EFF;
  background: #f0f9ff;
}

.avatar-placeholder .el-icon {
  font-size: 32px;
  color: #8c939d;
  margin-bottom: 5px;
}

.upload-text {
  font-size: 12px;
  color: #8c939d;
}

.form-tip {
  margin-top: 8px;
  color: #909399;
  font-size: 12px;
  line-height: 1.5;
}

:deep(.el-dialog__body) {
  padding: 20px 20px 10px;
}

:deep(.el-tabs__content) {
  overflow: visible;
}
</style>
