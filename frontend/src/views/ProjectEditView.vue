<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  getProject,
  updateProject,
  type Project,
  type ProjectCreateRequest,
} from '@/api/projectApi'
import ProjectForm from '@/components/ProjectForm.vue'

const route = useRoute()
const router = useRouter()

const project = ref<Project | null>(null)

const form = ref<ProjectCreateRequest>({
  name: '',
  description: '',
  goal: '',
  motivation: '',
  status: '未着手',
  priority: '中',
  startDate: '',
  plannedEndDate: '',
  plannedHours: 0,
})

const isSubmitting = ref(false)
const errorMessage = ref('')

// プロジェクトを取得
onMounted(async () => {
  try {
    const id = Number(route.params.id)

    project.value = await getProject(id)

    // 取得したプロジェクトをフォームにセット
    form.value = {
      name: project.value.name,
      description: project.value.description,
      goal: project.value.goal,
      motivation: project.value.motivation,
      status: project.value.status,
      priority: project.value.priority,
      startDate: project.value.startDate,
      plannedEndDate: project.value.plannedEndDate,
      plannedHours: project.value.plannedHours,
    }
  } catch (error) {
    console.error(error)
    errorMessage.value = 'プロジェクトの取得に失敗しました。'
  }
})

// 更新処理
const handleSubmit = async (): Promise<void> => {
  if (!project.value) {
    return
  }

  try {
    isSubmitting.value = true
    errorMessage.value = ''

    await updateProject(project.value.id, form.value)

    // 更新成功後、一覧画面へ戻る
    await router.push('/')
  } catch (error) {
    console.error(error)
    errorMessage.value = 'プロジェクトの更新に失敗しました。'
  } finally {
    isSubmitting.value = false
  }
}
// キャンセル
const handleCancel = () => {
  router.push(`/projects/${route.params.id}`)
}
</script>

<template>
  <div class="project-edit-page">
    <div class="project-edit-container">

      <!-- 戻る -->
      <button
        type="button"
        class="btn-back"
        @click="handleCancel"
      >
        ← プロジェクト詳細へ戻る
      </button>

      <!-- エラー -->
      <p
        v-if="errorMessage"
        class="error-message"
      >
        {{ errorMessage }}
      </p>

      <!-- 編集フォーム -->
      <div class="card edit-card">

        <!-- ヘッダー -->
        <div class="edit-header">
          <p class="page-eyebrow">
            PROJECT EDIT
          </p>

          <h1 class="detail-title">
            プロジェクトを編集
          </h1>

          <p class="detail-description">
            プロジェクトの情報を変更します。
          </p>
        </div>

        <!-- 共通フォーム -->
        <ProjectForm
          :model-value="form"
          :is-submitting="isSubmitting"
          :error-message="''"
          submit-label="変更を保存"
          @update:model-value="form = $event"
          @submit="handleSubmit"
          @cancel="handleCancel"
        />

      </div>

    </div>
  </div>
</template>
<style scoped>
.project-edit-page {
  min-height: 100vh;
  padding: 48px 24px;
}

.project-edit-container {
  max-width: 900px;
  margin: 0 auto;
}

.edit-card {
  padding: 32px;
}

.edit-header {
  padding-bottom: 28px;
  margin-bottom: 28px;
  border-bottom: 1px solid var(--color-border);
}

.edit-header h1 {
  margin: 0;
}

.detail-description {
  margin: 10px 0 0;
  color: var(--color-text-secondary);
  line-height: 1.7;
}
</style>
