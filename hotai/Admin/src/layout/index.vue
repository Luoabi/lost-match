<template>
  <div class="layout-container">
    <el-container>
      <!-- 侧边栏 -->
      <el-aside :width="sidebarWidth" :class="{ 'sidebar-mobile': isMobile }">
        <Sidebar />
      </el-aside>
      
      <!-- 遮罩层（移动端） -->
      <div 
        v-if="isMobile && sidebar.opened" 
        class="sidebar-mask"
        @click="closeSidebar"
      ></div>
      
      <!-- 主内容区 -->
      <el-container class="main-container">
        <!-- 顶部导航 -->
        <el-header class="header-container">
          <Navbar />
        </el-header>
        
        <!-- 内容区 -->
        <el-main class="main-content">
          <router-view v-slot="{ Component }">
            <transition name="fade-transform" mode="out-in">
              <keep-alive>
                <component :is="Component" />
              </keep-alive>
            </transition>
          </router-view>
        </el-main>
        
        <!-- 底部 -->
        <el-footer class="footer-container">
          <div class="footer-content">
            <span>© 2025 校园失物追寻平台</span>
            <span class="footer-divider">|</span>
            <span>基于多模态信息感知的智能匹配系统</span>
          </div>
        </el-footer>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { computed, onMounted, onUnmounted, ref } from 'vue'
import { useAppStore } from '@/store/modules/app'
import Sidebar from './components/Sidebar.vue'
import Navbar from './components/Navbar.vue'

const appStore = useAppStore()
const isMobile = ref(false)

const sidebar = computed(() => appStore.sidebar)

const sidebarWidth = computed(() => {
  if (isMobile.value) {
    return sidebar.value.opened ? '200px' : '0px'
  }
  return sidebar.value.opened ? '200px' : '64px'
})

const closeSidebar = () => {
  appStore.closeSidebar()
}

// 检测屏幕尺寸
const checkMobile = () => {
  isMobile.value = window.innerWidth < 768
  if (isMobile.value && sidebar.value.opened) {
    appStore.closeSidebar()
  }
}

onMounted(() => {
  checkMobile()
  window.addEventListener('resize', checkMobile)
})

onUnmounted(() => {
  window.removeEventListener('resize', checkMobile)
})
</script>

<style scoped>
.layout-container {
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.el-container {
  height: 100%;
}

.main-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  overflow: hidden;
}

.header-container {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  padding: 0;
  z-index: 10;
  height: 60px;
}

.el-aside {
  background: linear-gradient(180deg, #001529 0%, #002140 100%);
  transition: width 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  overflow-x: hidden;
  overflow-y: auto;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
  z-index: 100;
}

.sidebar-mobile {
  position: fixed;
  left: 0;
  top: 0;
  bottom: 0;
  z-index: 1000;
}

.sidebar-mask {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  z-index: 999;
  animation: fadeIn 0.3s;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

.main-content {
  background: linear-gradient(180deg, #f0f2f5 0%, #fafafa 100%);
  padding: 20px;
  overflow-y: auto;
  flex: 1;
}

.footer-container {
  background: #fff;
  border-top: 1px solid #e8e8e8;
  height: 50px;
  padding: 0 20px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.footer-content {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 13px;
  color: #909399;
}

.footer-divider {
  color: #dcdfe6;
}

/* 路由过渡动画 */
.fade-transform-leave-active,
.fade-transform-enter-active {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(-20px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(20px);
}

/* 响应式布局 */
@media screen and (max-width: 1200px) {
  .main-content {
    padding: 15px;
  }
}

@media screen and (max-width: 768px) {
  .main-content {
    padding: 10px;
  }
  
  .footer-content {
    font-size: 12px;
    flex-direction: column;
    gap: 4px;
  }
  
  .footer-divider {
    display: none;
  }
  
  .footer-container {
    height: auto;
    padding: 10px;
  }
}

@media screen and (max-width: 576px) {
  .main-content {
    padding: 8px;
  }
}
</style>
