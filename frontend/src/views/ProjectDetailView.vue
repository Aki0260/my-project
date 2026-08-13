<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  getProject,
  deleteProject,
  type Project,
} from '@/api/projectApi'

const route = useRoute()
const router = useRouter()

// プロジェクト情報
const project = ref<Project | null>(null)

// エラーメッセージ
const errorMessage = ref('')

// プロジェクト詳細を取得
onMounted(async () => {
  try {
    const id = Number(route.params.id)

    project.value = await getProject(id)
  } catch (error) {
    console.error(error)
    errorMessage.value = 'プロジェクトの取得に失敗しました。'
  }
})

// 編集画面へ移動
const handleEdit = () => {
  if (!project.value) {
    return
  }

  router.push(`/projects/${project.value.id}/edit`)
}

// プロジェクト削除
const handleDelete = async () => {
  if (!project.value) {
    return
  }

  const confirmed = window.confirm(
    'このプロジェクトを削除しますか？'
  )

  if (!confirmed) {
    return
  }

  try {
    await deleteProject(project.value.id)

    await router.push('/')
  } catch (error) {
    console.error(error)
    errorMessage.value = 'プロジェクトの削除に失敗しました。'
  }
}

// 一覧に戻る
const handleBack = () => {
  router.push('/')
}
</script>

<template>
  <div class="page">
    <!-- 一覧に戻る -->
    <button
      type="button"
      class="btn-back"
      @click="handleBack"
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

    <div
      v-if="project"
      class="card detail-card"
    >

      <!-- ヘッダー -->
      <div class="detail-header">
        <p class="page-eyebrow">
          PROJECT DETAIL
        </p>

        <h1 class="detail-title">
          {{ project.name }}
        </h1>

        <p class="detail-description">
          {{ project.description }}
        </p>
      </div>

      <!-- 目標 -->
      <section class="detail-section">
        <h2 class="detail-section-title">
          目標
        </h2>

        <p>
          {{ project.goal }}
        </p>
      </section>

      <!-- 意気込み -->
      <section class="detail-section">
        <h2 class="detail-section-title">
          意気込み
        </h2>

        <p>
          {{ project.motivation }}
        </p>
      </section>

      <!-- プロジェクト情報 -->
      <section class="detail-section">
        <h2 class="detail-section-title">
          プロジェクト情報
        </h2>

        <div class="info-grid">

          <div class="info-item">
            <span class="info-label">
              ステータス
            </span>

            <span class="badge badge-primary">
              {{ project.status }}
            </span>
          </div>

          <div class="info-item">
            <span class="info-label">
              優先度
            </span>

            <span class="badge badge-neutral">
              {{ project.priority }}
            </span>
          </div>

          <div class="info-item">
            <span class="info-label">
              開始日
            </span>

            <span>
              {{ project.startDate }}
            </span>
          </div>

          <div class="info-item">
            <span class="info-label">
              予定終了日
            </span>

            <span>
              {{ project.plannedEndDate }}
            </span>
          </div>

          <div class="info-item">
            <span class="info-label">
              予定工数
            </span>

            <span>
              {{ project.plannedHours }}時間
            </span>
          </div>

        </div>
      </section>

      <!-- 進行度 -->
      <section class="detail-section">
        <h2 class="detail-section-title">
          進行度
        </h2>

        <div class="progress">
          <div
            class="progress-bar"
            style="width: 0%"
          ></div>
        </div>

        <p class="progress-text">
          進行度：0%
        </p>
      </section>

      <!-- アクション -->
      <div class="detail-actions">

        <button
          type="button"
          class="btn btn-danger"
          @click="handleDelete"
        >
          プロジェクトを削除
        </button>

        <button
          type="button"
          class="btn btn-primary"
          @click="handleEdit"
        >
          編集
        </button>

      </div>

    </div>
  </div>
</template>

<style scoped>
.info-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 16px;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 8px;

  padding: 16px;

  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);

  background-color: #fafbfc;
}

.info-label {
  font-size: 13px;
  font-weight: 600;
  color: var(--color-text-secondary);
}

.progress-text {
  margin: 10px 0 0;
  font-size: 14px;
  color: var(--color-text-secondary);
}

@media (max-width: 768px) {
  .info-grid {
    grid-template-columns: 1fr;
  }
}
</style>
