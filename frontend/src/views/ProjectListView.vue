<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

import { getProjects, type Project } from '@/api/projectApi'
import ProjectCard from '@/components/ProjectCard.vue'

const router = useRouter()

const projects = ref<Project[]>([])

onMounted(async () => {
  projects.value = await getProjects()
})
</script>

<template>
  <div class="project-list-page">

    <div class="project-list-container">

      <!-- ヘッダー -->
      <header class="page-header">
        <div>
          <p class="page-eyebrow">PROJECTS</p>
          <h1>プロジェクト一覧</h1>
          <p class="page-description">
            現在進行中のプロジェクトを管理します。
          </p>
        </div>

        <button
          class="btn btn-primary"
          @click="router.push('/projects/new')"
        >
          ＋ 新規プロジェクト
        </button>
      </header>

      <!-- プロジェクト一覧 -->
      <div
        v-if="projects.length > 0"
        class="project-grid"
      >
        <ProjectCard
          v-for="project in projects"
          :key="project.id"
          :project="project"
        />
      </div>

      <!-- プロジェクトがない場合 -->
      <div
        v-else
        class="empty-state card"
      >
        <div class="empty-icon">
          ＋
        </div>

        <h2>プロジェクトがありません</h2>

        <p>
          最初のプロジェクトを作成してみましょう。
        </p>

        <button
          class="btn btn-primary"
          @click="router.push('/projects/new')"
        >
          プロジェクトを作成
        </button>
      </div>

    </div>
  </div>
</template>
<style scoped>
.project-list-page {
  min-height: 100vh;
  padding: 48px 24px;
}

.project-list-container {
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  gap: 24px;
  margin-bottom: 32px;
}

.page-eyebrow {
  margin: 0 0 6px;
  color: var(--color-primary);
  font-size: 13px;
  font-weight: 700;
  letter-spacing: 0.08em;
}

.page-header h1 {
  margin: 0;
  font-size: 32px;
}

.page-description {
  margin: 8px 0 0;
  color: var(--color-text-secondary);
}

.project-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.empty-state {
  padding: 60px 24px;
  text-align: center;
}

.empty-icon {
  width: 48px;
  height: 48px;
  margin: 0 auto 16px;
  display: flex;
  align-items: center;
  justify-content: center;

  border-radius: 50%;
  background-color: var(--color-primary-light);
  color: var(--color-primary);
  font-size: 24px;
  font-weight: 600;
}

.empty-state h2 {
  margin: 0;
  font-size: 20px;
}

.empty-state p {
  margin: 8px 0 20px;
  color: var(--color-text-secondary);
}

@media (max-width: 900px) {
  .project-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 600px) {
  .project-list-page {
    padding: 24px 16px;
  }

  .page-header {
    align-items: flex-start;
    flex-direction: column;
  }

  .project-grid {
    grid-template-columns: 1fr;
  }
}
</style>
