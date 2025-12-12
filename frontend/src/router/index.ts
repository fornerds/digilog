import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import { authGuard, guestGuard } from './guards'

const routes: RouteRecordRaw[] = [
  {
    path: '/onboarding',
    name: 'Onboarding',
    component: () => import('@/pages/onboarding/OnboardingPage.vue'),
    beforeEnter: guestGuard,
  },
  {
    path: '/auth',
    component: () => import('@/layouts/AuthLayout.vue'),
    beforeEnter: guestGuard,
    children: [
      {
        path: 'login',
        name: 'Login',
        component: () => import('@/pages/auth/LoginPage.vue'),
      },
      {
        path: 'signup',
        name: 'Signup',
        component: () => import('@/pages/auth/SignupPage.vue'),
      },
    ],
  },
  {
    path: '/',
    component: () => import('@/layouts/DefaultLayout.vue'),
    children: [
      {
        path: '',
        name: 'Home',
        component: () => import('@/pages/home/HomePage.vue'),
      },
      {
        path: 'reports',
        name: 'Reports',
        component: () => import('@/pages/reports/ReportsPage.vue'),
        // beforeEnter: authGuard,
      },
      {
        path: 'reports/:reportId',
        name: 'ReportDetail',
        component: () => import('@/pages/reports/ReportDetailPage.vue'),
        // beforeEnter: authGuard,
      },
      {
        path: 'reports/:reportId/personal-color',
        name: 'PersonalColor',
        component: () => import('@/pages/reports/PersonalColorPage.vue'),
        // beforeEnter: authGuard,
      },
      {
        path: 'reports/:reportId/recommendations',
        name: 'Recommendations',
        component: () => import('@/pages/reports/RecommendationsPage.vue'),
        //beforeEnter: authGuard,
      },
      {
        path: 'community',
        name: 'Community',
        component: () => import('@/pages/community/CommunityPage.vue'),
      },
      {
        path: 'community/notice/:id',
        name: 'NoticeDetail',
        component: () => import('@/pages/community/NoticeDetailPage.vue'),
      },
      {
        path: 'community/:postId',
        name: 'PostDetail',
        component: () => import('@/pages/community/PostDetailPage.vue'),
      },
      {
        path: 'community/write',
        name: 'PostWrite',
        component: () => import('@/pages/community/PostWritePage.vue'),
        // beforeEnter: authGuard,
      },
      {
        path: 'mypage',
        name: 'MyPage',
        component: () => import('@/pages/mypage/MyPage.vue'),
        // beforeEnter: authGuard,
      },
      {
        path: 'mypage/profile',
        name: 'ProfileEdit',
        component: () => import('@/pages/mypage/ProfileEditPage.vue'),
        // beforeEnter: authGuard,
      },
      {
        path: 'mypage/liked-products',
        name: 'LikedProducts',
        component: () => import('@/pages/mypage/LikedProductsPage.vue'),
        // beforeEnter: authGuard,
      },
      {
        path: 'components/buttons',
        name: 'ButtonShowcase',
        component: () => import('@/pages/components/ButtonShowcasePage.vue'),
      },
      {
        path: 'test',
        name: 'Test',
        component: () => import('@/pages/test/TestPage.vue'),
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory('/digilog/'),
  routes,
})

export default router


