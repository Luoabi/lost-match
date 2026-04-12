<template>
  <div class="sidebar-container">
    <!-- Logo -->
    <div class="logo-container" @click="goHome">
      <transition name="logo-fade" mode="out-in">
        <div v-if="!isCollapse" key="full" class="logo-full">
          <el-icon class="logo-icon"><Box /></el-icon>
          <h1 class="logo-title">失物追寻</h1>
        </div>
        <div v-else key="mini" class="logo-mini">
          <el-icon class="logo-icon-mini"><Box /></el-icon>
        </div>
      </transition>
    </div>
    
    <!-- 菜单 -->
    <el-scrollbar class="menu-scrollbar">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :unique-opened="true"
        :collapse-transition="false"
        background-color="transparent"
        text-color="rgba(255, 255, 255, 0.85)"
        active-text-color="#fff"
        router
      >
        <el-menu-item index="/dashboard" class="menu-item-custom">
          <el-icon><HomeFilled /></el-icon>
          <template #title>
            <span class="menu-title">首页</span>
          </template>
        </el-menu-item>
        
        <el-menu-item index="/lost-items" class="menu-item-custom">
          <el-icon><Box /></el-icon>
          <template #title>
            <span class="menu-title">失物管理</span>
          </template>
        </el-menu-item>
        
        <el-menu-item index="/found-items" class="menu-item-custom">
          <el-icon><Present /></el-icon>
          <template #title>
            <span class="menu-title">拾获管理</span>
          </template>
        </el-menu-item>
        
        <el-menu-item index="/match-records" class="menu-item-custom">
          <el-icon><Connection /></el-icon>
          <template #title>
            <span class="menu-title">匹配记录</span>
          </template>
        </el-menu-item>
        
        <el-menu-item index="/users" class="menu-item-custom">
          <el-icon><User /></el-icon>
          <template #title>
            <span class="menu-title">用户管理</span>
          </template>
        </el-menu-item>
        
        <el-menu-item index="/audit" class="menu-item-custom">
          <el-icon><DocumentChecked /></el-icon>
          <template #title>
            <span class="menu-title">内容审核</span>
          </template>
        </el-menu-item>
        
        <el-menu-item index="/logs" class="menu-item-custom">
          <el-icon><Document /></el-icon>
          <template #title>
            <span class="menu-title">操作日志</span>
          </template>
        </el-menu-item>
        
        <el-menu-item index="/statistics" class="menu-item-custom">
          <el-icon><DataAnalysis /></el-icon>
          <template #title>
            <span class="menu-title">数据统计</span>
          </template>
        </el-menu-item>
        
        <el-menu-item index="/settings" class="menu-item-custom">
          <el-icon><Setting /></el-icon>
          <template #title>
            <span class="menu-title">系统设置</span>
          </template>
        </el-menu-item>
      </el-menu>
    </el-scrollbar>
    
    <!-- 版本信息 -->
    <div v-if="!isCollapse" class="version-info">
      <span>v1.0.0</span>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAppStore } from '@/store/modules/app'

const route = useRoute()
const router = useRouter()
const appStore = useAppStore()

const activeMenu = computed(() => route.path)
const isCollapse = computed(() => !appStore.sidebar.opened)

const goHome = () => {
  router.push('/dashboard')
}
</script>

<style scoped>
.sidebar-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  background: linear-gradient(180deg, #001529 0%, #002140 100%);
}

.logo-container {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(0, 0, 0, 0.2);
  cursor: pointer;
  transition: all 0.3s;
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
}

.logo-container:hover {
  background: rgba(0, 0, 0, 0.3);
}

.logo-full {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-icon {
  font-size: 28px;
  color: #409EFF;
}

.logo-title {
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  margin: 0;
  letter-spacing: 1px;
}

.logo-mini {
  display: flex;
  align-items: center;
  justify-content: center;
}

.logo-icon-mini {
  font-size: 32px;
  color: #409EFF;
}

.logo-fade-enter-active,
.logo-fade-leave-active {
  transition: all 0.3s;
}

.logo-fade-enter-from,
.logo-fade-leave-to {
  opacity: 0;
  transform: scale(0.8);
}

.menu-scrollbar {
  flex: 1;
  overflow-x: hidden;
}

:deep(.el-scrollbar__view) {
  height: 100%;
}

.el-menu {
  border-right: none;
  padding: 10px 0;
}

.menu-item-custom {
  height: 50px;
  line-height: 50px;
  margin: 4px 8px;
  border-radius: 8px;
  transition: all 0.3s;
}

.menu-item-custom:hover {
  background: rgba(255, 255, 255, 0.1) !important;
}

.menu-item-custom.is-active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%) !important;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.menu-title {
  font-size: 14px;
  font-weight: 500;
}

:deep(.el-menu-item .el-icon) {
  font-size: 18px;
  margin-right: 8px;
}

:deep(.el-menu--collapse .el-menu-item .el-icon) {
  margin-right: 0;
}

.version-info {
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: rgba(255, 255, 255, 0.45);
  font-size: 12px;
  border-top: 1px solid rgba(255, 255, 255, 0.05);
}

/* 响应式 */
@media screen and (max-width: 768px) {
  .sidebar-container {
    box-shadow: 2px 0 8px rgba(0, 0, 0, 0.15);
  }
}
</style>
