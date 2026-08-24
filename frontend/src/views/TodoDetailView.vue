<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  getTodo,
  deleteTodo,
  type Todo,
} from '@/api/todoApi'
import {
  getWorkLogs,
  createWorkLog,
  deleteWorkLog,
  type WorkLog,
  type WorkLogRequest,
} from '@/api/workLogApi'

const route = useRoute()
const router = useRouter()

// ToDo情報
const todo = ref<Todo | null>(null)

// 作業記録
const workLogs = ref<WorkLog[]>([])

// 実績工数
const actualHours = ref(0)

// 作業記録フォーム
const workLogForm = ref<WorkLogRequest>({
  workDate: '',
  hours: 0,
})

// 登録処理中かどうか
const isSubmitting = ref(false)

// エラーメッセージ
const errorMessage = ref('')

// ToDo詳細・工数を取得
onMounted(async () => {
  try {
    const id = Number(route.params.id)

    todo.value = await getTodo(id)

    await loadWorkLogs(id)
  } catch (error) {
    console.error(error)
    errorMessage.value =
      'ToDoの取得に失敗しました。'
  }
})

// 作業記録を取得
const loadWorkLogs = async (todoId: number): Promise<void> => {
  workLogs.value = await getWorkLogs(todoId)

  actualHours.value = workLogs.value.reduce(
    (total, workLog) => total + workLog.hours,
    0,
  )
}

// 残工数
const remainingHours = () => {
  if (!todo.value) {
    return 0
  }

  return Math.max(
    todo.value.plannedHours - actualHours.value,
    0,
  )
}

// 作業記録登録
const handleCreateWorkLog = async (): Promise<void> => {
  errorMessage.value = ''

  if (!todo.value) {
    return
  }

  if (
    !workLogForm.value.workDate ||
    workLogForm.value.hours <= 0
  ) {
    errorMessage.value =
      '作業日と工数を入力してください。'
    return
  }

  try {
    isSubmitting.value = true

    await createWorkLog(
      todo.value.id,
      workLogForm.value,
    )

    // 登録後に一覧を再取得
    await loadWorkLogs(todo.value.id)

    // フォームをリセット
    workLogForm.value = {
      workDate: '',
      hours: 0,
    }
  } catch (error) {
    console.error(error)
    errorMessage.value =
      '作業記録の登録に失敗しました。'
  } finally {
    isSubmitting.value = false
  }
}

// 作業記録削除
const handleDeleteWorkLog = async (
  workLogId: number,
): Promise<void> => {
  if (!todo.value) {
    return
  }

  const confirmed = window.confirm(
    'この作業記録を削除しますか？',
  )

  if (!confirmed) {
    return
  }

  try {
    await deleteWorkLog(
      todo.value.id,
      workLogId,
    )

    await loadWorkLogs(todo.value.id)
  } catch (error) {
    console.error(error)
    errorMessage.value =
      '作業記録の削除に失敗しました。'
  }
}

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
    'このToDoを削除しますか？',
  )

  if (!confirmed) {
    return
  }

  try {
    await deleteTodo(todo.value.id)

    await router.push(
      `/projects/${todo.value.projectId}/todos`,
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
    `/projects/${todo.value.projectId}/todos`,
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

        <!-- 工数管理 -->
        <section class="detail-section">

          <h2 class="detail-section-title">
            工数管理
          </h2>

          <!-- 工数サマリー -->
          <div class="hours-grid">

            <div class="hours-item">
              <span class="info-label">
                予定工数
              </span>

              <strong>
                {{ todo.plannedHours }}時間
              </strong>
            </div>

            <div class="hours-item">
              <span class="info-label">
                実績工数
              </span>

              <strong>
                {{ actualHours }}時間
              </strong>
            </div>

            <div class="hours-item">
              <span class="info-label">
                残工数
              </span>

              <strong>
                {{ remainingHours() }}時間
              </strong>
            </div>

          </div>

          <!-- 作業記録登録 -->
          <div class="work-log-form">

            <h3>
              作業記録を追加
            </h3>

            <div class="form-row">

              <div class="form-group">

                <label
                  for="workDate"
                  class="form-label"
                >
                  作業日
                </label>

                <input
                  id="workDate"
                  v-model="workLogForm.workDate"
                  class="form-control"
                  type="date"
                />

              </div>

              <div class="form-group">

                <label
                  for="hours"
                  class="form-label"
                >
                  工数（時間）
                </label>

                <input
                  id="hours"
                  v-model.number="workLogForm.hours"
                  class="form-control"
                  type="number"
                  min="0.1"
                  step="0.1"
                  placeholder="例：1.5"
                />

              </div>

            </div>

            <button
              type="button"
              class="btn btn-primary"
              :disabled="isSubmitting"
              @click="handleCreateWorkLog"
            >
              {{
                isSubmitting
                  ? '登録中...'
                  : '作業記録を追加'
              }}
            </button>

          </div>

          <!-- 作業記録一覧 -->
          <div class="work-log-list">

            <h3>
              作業記録
            </h3>

            <p
              v-if="workLogs.length === 0"
              class="empty-work-log"
            >
              作業記録はありません。
            </p>

            <div
              v-for="workLog in workLogs"
              :key="workLog.id"
              class="work-log-item"
            >

              <div>
                <span class="work-log-date">
                  {{ workLog.workDate }}
                </span>

                <span class="work-log-hours">
                  {{ workLog.hours }}時間
                </span>
              </div>

              <button
                type="button"
                class="btn btn-danger btn-small"
                @click="handleDeleteWorkLog(workLog.id)"
              >
                削除
              </button>

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

/* 工数サマリー */

.hours-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

.hours-item {
  display: flex;
  flex-direction: column;
  gap: 8px;

  padding: 18px;

  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);

  background-color: #fafbfc;
}

.hours-item strong {
  font-size: 22px;
}

/* 作業記録フォーム */

.work-log-form {
  margin-top: 28px;
  padding-top: 28px;

  border-top: 1px solid var(--color-border);
}

.work-log-form h3,
.work-log-list h3 {
  margin: 0 0 18px;

  font-size: 16px;
}

.work-log-form .form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;

  margin-bottom: 18px;
}

/* 作業記録一覧 */

.work-log-list {
  margin-top: 32px;
}

.work-log-item {
  display: flex;
  align-items: center;
  justify-content: space-between;

  padding: 14px 16px;

  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);

  background-color: #fafbfc;
}

.work-log-item + .work-log-item {
  margin-top: 10px;
}

.work-log-date {
  margin-right: 24px;

  color: var(--color-text-secondary);
}

.work-log-hours {
  font-weight: 600;
}

.empty-work-log {
  color: var(--color-text-secondary);
  font-size: 14px;
}

.btn-small {
  padding: 6px 12px;
  font-size: 13px;
}

@media (max-width: 768px) {
  .info-grid,
  .hours-grid {
    grid-template-columns: 1fr;
  }

  .work-log-form .form-row {
    grid-template-columns: 1fr;
  }
}
</style>
