import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/store/modules/user'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

NProgress.configure({ showSpinner: false })

// 路由配置
const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录', requireAuth: false }
  },
  {
    path: '/',
    component: () => import('@/layout/index.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '首页', icon: 'HomeFilled' }
      },
      {
        path: 'lost-items',
        name: 'LostItems',
        component: () => import('@/views/lost-items/index.vue'),
        meta: { title: '失物管理', icon: 'Box' }
      },
      {
        path: 'found-items',
        name: 'FoundItems',
        component: () => import('@/views/found-items/index.vue'),
        meta: { title: '拾获管理', icon: 'Present' }
      },
      {
        path: 'match-records',
        name: 'MatchRecords',
        component: () => import('@/views/match-records/index.vue'),
        meta: { title: '匹配记录', icon: 'Connection' }
      },
      {
        path: 'users',
        name: 'Users',
        component: () => import('@/views/users/index.vue'),
        meta: { title: '用户管理', icon: 'User' }
      },
      {
        path: 'audit',
        name: 'Audit',
        component: () => import('@/views/audit/index.vue'),
        meta: { title: '内容审核', icon: 'DocumentChecked' }
      },
      {
        path: 'logs',
        name: 'Logs',
        component: () => import('@/views/logs/index.vue'),
        meta: { title: '操作日志', icon: 'Document' }
      },
      {
        path: 'statistics',
        name: 'Statistics',
        component: () => import('@/views/statistics/index.vue'),
        meta: { title: '数据统计', icon: 'DataAnalysis' }
      },
      {
        path: 'settings',
        name: 'Settings',
        component: () => import('@/views/settings/index.vue'),
        meta: { title: '系统设置', icon: 'Setting' }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/error/404.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  NProgress.start()
  
  const userStore = useUserStore()
  const token = userStore.token
  
  if (to.path === '/login') {
    next()
  } else {
    if (token) {
      next()
    } else {
      next('/login')
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})

export default router
