<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  getTodo,
  deleteTodo,
  type Todo,
} from '@/api/todoApi'

const route = useRoute()
const router = useRouter()

// ToDo情報
const todo = ref<Todo | null>(null)

// エラーメッセージ
const errorMessage = ref('')

// ToDo詳細を取得
onMounted(async () => {
  try {
    const id = Number(route.params.id)

    todo.value = await getTodo(id)
  } catch (error) {
    console.error(error)
    errorMessage.value =
      'ToDoの取得に失敗しました。'
  }
})

// 編集画面へ移動
const handleEdit = () => {
  if (!todo.value) {
    return
  }

  router.push(`/todos/${todo.value.id}/edit`)
}

// ToDo削除
const handleDelete = async () => {
  if (!todo.value) {
    return
  }

  const confirmed = window.confirm(
    'このToDoを削除しますか？'
  )

  if (!confirmed) {
    return
  }

  try {
    await deleteTodo(todo.value.id)

    await router.push(
      `/projects/${todo.value.projectId}/todos`
    )
  } catch (error) {
    console.error(error)
    errorMessage.value =
      'ToDoの削除に失敗しました。'
  }
}

// ToDo一覧へ戻る
const handleBack = () => {
  if (!todo.value) {
    return
  }

  router.push(
    `/projects/${todo.value.projectId}/todos`
  )
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
      ← ToDo一覧へ戻る
    </button>

    <!-- エラー -->
    <p
      v-if="errorMessage"
      class="error-message"
    >
      {{ errorMessage }}
    </p>

    <!-- ToDo情報 -->
    <div v-if="todo">

      <!-- ヘッダー -->
      <section class="card detail-card detail-header">
        <p class="page-description">
          TODO DETAIL
        </p>

        <h1 class="detail-title">
          {{ todo.title }}
        </h1>
      </section>

      <!-- ToDo情報 -->
      <section class="card detail-card">

        <section class="detail-section">
          <h2 class="detail-section-title">
            ToDo情報
          </h2>

          <div class="info-grid">

            <div class="info-item">
              <span class="info-label">
                ステータス
              </span>

              <span>
                <span class="badge badge-primary">
                  {{ todo.status }}
                </span>
              </span>
            </div>

            <div class="info-item">
              <span class="info-label">
                予定工数
              </span>

              <span>
                {{ todo.plannedHours }}時間
              </span>
            </div>

            <div class="info-item">
              <span class="info-label">
                期限
              </span>

              <span>
                {{ todo.dueDate || '未設定' }}
              </span>
            </div>

          </div>
        </section>

        <!-- メモ -->
        <section class="detail-section">
          <h2 class="detail-section-title">
            メモ
          </h2>

          <p class="detail-description">
            {{ todo.memo || 'メモはありません。' }}
          </p>
        </section>

        <!-- 作成日時 -->
        <section class="detail-section">
          <h2 class="detail-section-title">
            登録情報
          </h2>

          <div class="info-grid">

            <div class="info-item">
              <span class="info-label">
                作成日時
              </span>

              <span>
                {{ todo.createdAt }}
              </span>
            </div>

            <div class="info-item">
              <span class="info-label">
                更新日時
              </span>

              <span>
                {{ todo.updatedAt }}
              </span>
            </div>

          </div>
        </section>

      </section>

      <!-- アクション -->
      <div class="detail-actions">

        <button
          type="button"
          class="btn btn-primary"
          @click="handleEdit"
        >
          編集
        </button>

        <button
          type="button"
          class="btn btn-danger"
          @click="handleDelete"
        >
          ToDoを削除
        </button>

      </div>

    </div>
  </div>
</template>

<style scoped>
.info-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 6px;

  padding: 16px;

  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);

  background-color: #fafbfc;
}

.info-label {
  font-size: 13px;
  color: var(--color-text-secondary);
  font-weight: 600;
}

@media (max-width: 768px) {
  .info-grid {
    grid-template-columns: 1fr;
  }
}
</style>
