<script setup lang="ts">
import { useRouter } from 'vue-router'
import type { Project } from '@/api/projectApi'

const props = defineProps<{
  project: Project
}>()

const router = useRouter()

const handleClick = () => {
  router.push(`/projects/${props.project.id}`)
}
</script>

<template>
  <div class="project-card" @click="handleClick">
    <h2>{{ project.name }}</h2>

    <p>{{ project.description }}</p>

    <p>目標：{{ project.goal }}</p>

    <p>意気込み：{{ project.motivation }}</p>

    <p>優先度：{{ project.priority }}</p>

    <p>ステータス：{{ project.status }}</p>

    <p>予定終了日：{{ project.plannedEndDate }}</p>
  </div>
</template>
