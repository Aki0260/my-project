import { createRouter, createWebHistory } from 'vue-router'

import ProjectListView from '@/views/ProjectListView.vue'
import ProjectCreateView from '@/views/ProjectCreateView.vue'
import ProjectDetailView from '@/views/ProjectDetailView.vue'
import ProjectEditView from '@/views/ProjectEditView.vue'

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
    {
      path: '/projects/:id',
      component: ProjectDetailView,
    },
    {
      path: '/projects/:id/edit',
      component: ProjectEditView,
    },
  ],
})

export default router
