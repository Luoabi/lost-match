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
    ElMessage.info('个人中心功能开发中')
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
</style>
