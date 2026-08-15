<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import {
  createProject,
  type ProjectCreateRequest,
} from '@/api/projectApi'
import ProjectForm from '@/components/ProjectForm.vue'

const router = useRouter()

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

const handleSubmit = async (): Promise<void> => {
  errorMessage.value = ''

  if (
    !form.value.name ||
    !form.value.startDate ||
    !form.value.plannedEndDate ||
    form.value.plannedHours <= 0
  ) {
    errorMessage.value =
      'プロジェクト名、開始日、予定終了日、予定工数を入力してください。'
    return
  }

  try {
    isSubmitting.value = true

    await createProject(form.value)

    // 作成成功後は一覧画面へ
    await router.push('/')
  } catch (error) {
    console.error(error)
    errorMessage.value = 'プロジェクトの作成に失敗しました。'
  } finally {
    isSubmitting.value = false
  }
}

const handleCancel = () => {
  router.push('/')
}
</script>

<template>
  <div class="project-create-page">
    <div class="project-create-container">

      <!-- 一覧に戻る -->
      <button
        type="button"
        class="btn-back"
        @click="handleCancel"
      >
        ← 一覧に戻る
      </button>

      <!-- エラー -->
      <p
        v-if="errorMessage"
        class="error-message"
      >
        {{ errorMessage }}
      </p>

      <!-- 作成カード -->
      <div class="card create-card">

        <!-- ヘッダー -->
        <div class="create-header">
          <p class="page-eyebrow">
            PROJECT
          </p>

          <h1 class="detail-title">
            新しいプロジェクトを作成
          </h1>

          <p class="detail-description">
            プロジェクトの情報を入力して、計画を始めましょう。
          </p>
        </div>

        <!-- フォーム -->
        <ProjectForm
          :model-value="form"
          :is-submitting="isSubmitting"
          :error-message="''"
          submit-label="作成"
          @update:model-value="form = $event"
          @submit="handleSubmit"
          @cancel="handleCancel"
        />

      </div>

    </div>
  </div>
</template>

<style scoped>
.project-create-page {
  min-height: 100vh;
  padding: 48px 24px;
}

.project-create-container {
  max-width: 900px;
  margin: 0 auto;
}

.create-card {
  padding: 32px;
}

.create-header {
  padding-bottom: 28px;
  margin-bottom: 28px;
  border-bottom: 1px solid var(--color-border);
}

.create-header h1 {
  margin: 0;
}

.detail-description {
  margin: 10px 0 0;
  color: var(--color-text-secondary);
  line-height: 1.7;
}

@media (max-width: 700px) {
  .project-create-page {
    padding: 24px 16px;
  }

  .create-card {
    padding: 24px;
  }
}
</style>
