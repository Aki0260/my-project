import { createRouter, createWebHistory } from 'vue-router'

import ProjectListView from '@/views/ProjectListView.vue'
import ProjectCreateView from '@/views/ProjectCreateView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),

  routes: [
    {
      path: '/',
      component: ProjectListView,
    },
    {
      path: '/projects/new',
      component: ProjectCreateView,
    },
  ],
})

export default router
