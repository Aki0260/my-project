<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { getProjects, type Project } from '@/api/projectApi'
import ProjectCard from '@/components/ProjectCard.vue'

// プロジェクト一覧.
//プロジェクトの値をリアクティブで持つ
const projects = ref<Project[]>([])

// コンポーネントがマウントされたときにプロジェクト一覧を取得
onMounted(async () => {
  //projectsはオブジェクトだから、valueを使ってアクセスする必要がある
  projects.value = await getProjects()
})
</script>

<template>
  <div>
    <h1>プロジェクト一覧</h1>
    <!-- v-forが自動的に.valueを外してくれる -->
    <ProjectCard
      v-for="project in projects"
      :key="project.id"
      :project="project"
    />
  </div>
</template>
