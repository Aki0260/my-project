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

const handleSubmit = async () => {
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

      <div class="page-header">
        <p class="page-eyebrow">PROJECT</p>

        <h1>新しいプロジェクトを作成</h1>

        <p class="page-description">
          プロジェクトの情報を入力して、計画を始めましょう。
        </p>
      </div>

      <ProjectForm
        :model-value="form"
        :is-submitting="isSubmitting"
        :error-message="errorMessage"
        submit-label="作成"
        @update:model-value="form = $event"
        @submit="handleSubmit"
        @cancel="handleCancel"
      />

    </div>
  </div>
</template>
