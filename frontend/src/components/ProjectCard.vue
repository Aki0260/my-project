```vue
<script setup lang="ts">
import type { Project } from '@/api/projectApi'

//definePropsを使って、親コンポーネントから渡されるプロジェクトの値を受け取る
defineProps<{
  project: Project
}>()
</script>

<template>
  <div class="project-card">
    <h2>{{ project.name }}</h2>

    <p>{{ project.description }}</p>

    <p>目標：{{ project.goal }}</p>

    <p>意気込み：{{ project.motivation }}</p>

    <p>優先度：{{ project.priority }}</p>

    <p>ステータス：{{ project.status }}</p>

    <p>予定終了日：{{ project.plannedEndDate }}</p>
  </div>
</template>
