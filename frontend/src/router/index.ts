import { createRouter, createWebHistory } from 'vue-router'

import ProjectListView from '@/views/ProjectListView.vue'
import ProjectCreateView from '@/views/ProjectCreateView.vue'
import ProjectDetailView from '@/views/ProjectDetailView.vue'
import ProjectEditView from '@/views/ProjectEditView.vue'

import TodoListView from '@/views/TodoListView.vue'
import TodoCreateView from '@/views/TodoCreateView.vue'
import TodoDetailView from '@/views/TodoDetailView.vue'
import TodoEditView from '@/views/TodoEditView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),

  routes: [
    // =========================
    // Project
    // =========================
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

    // =========================
    // Todo
    // =========================
    {
      path: '/projects/:id/todos',
      component: TodoListView,
    },
    {
      path: '/projects/:id/todos/new',
      component: TodoCreateView,
    },
    {
      path: '/todos/:id',
      component: TodoDetailView,
    },
    {
      path: '/todos/:id/edit',
      component: TodoEditView,
    },
  ],
})

export default router
