<script setup lang="ts">
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  createTodo,
  type TodoRequest,
} from '@/api/todoApi'

const route = useRoute()
const router = useRouter()

// プロジェクトID
const projectId = Number(route.params.id)

// フォーム
const form = ref<TodoRequest>({
  projectId,
  title: '',
  plannedHours: 0,
  status: '未着手',
  dueDate: '',
  memo: '',
})

// 登録処理中かどうか
const isSubmitting = ref(false)

// エラーメッセージ
const errorMessage = ref('')

// ToDo作成
const handleSubmit = async (): Promise<void> => {
  errorMessage.value = ''

  // 必須項目チェック
  if (
    !form.value.title ||
    form.value.plannedHours <= 0
  ) {
    errorMessage.value =
      'ToDo名と予定工数を入力してください。'
    return
  }

  try {
    isSubmitting.value = true

    await createTodo(form.value)

    // 作成成功後、ToDo一覧へ戻る
    await router.push(`/projects/${projectId}/todos`)
  } catch (error) {
    console.error(error)
    errorMessage.value =
      'ToDoの作成に失敗しました。'
  } finally {
    isSubmitting.value = false
  }
}

// キャンセル
const handleCancel = () => {
  router.push(`/projects/${projectId}/todos`)
}
</script>

<template>
  <div class="page">

    <!-- 戻る -->
    <button
      type="button"
      class="btn-back"
      @click="handleCancel"
    >
      ← ToDo一覧へ戻る
    </button>

    <!-- ヘッダー -->
    <div class="page-header">
      <div>
        <p class="page-description">TODO</p>

        <h1 class="page-title">
          新しいToDoを作成
        </h1>

        <p class="page-description">
          プロジェクトで行う作業を登録します。
        </p>
      </div>
    </div>

    <!-- フォーム -->
    <form
      class="card todo-form"
      @submit.prevent="handleSubmit"
    >

      <!-- 基本情報 -->
      <section class="form-section">

        <div class="section-header">
          <h2>基本情報</h2>

          <p>
            ToDoの内容を入力してください。
          </p>
        </div>

        <!-- ToDo名 -->
        <div class="form-group">

          <label
            for="title"
            class="form-label"
          >
            ToDo名
          </label>

          <input
            id="title"
            v-model="form.title"
            class="form-control"
            type="text"
            placeholder="例：API設計を行う"
          />

        </div>

        <!-- 予定工数 -->
        <div class="form-group">

          <label
            for="plannedHours"
            class="form-label"
          >
            予定工数（時間）
          </label>

          <input
            id="plannedHours"
            v-model.number="form.plannedHours"
            class="form-control"
            type="number"
            min="1"
            placeholder="例：3"
          />

        </div>

      </section>

      <!-- 計画 -->
      <section class="form-section">

        <div class="section-header">
          <h2>計画</h2>

          <p>
            ステータスと期限を設定します。
          </p>
        </div>

        <div class="form-row">

          <!-- ステータス -->
          <div class="form-group">

            <label
              for="status"
              class="form-label"
            >
              ステータス
            </label>

            <select
              id="status"
              v-model="form.status"
              class="form-control"
            >
              <option value="未着手">
                未着手
              </option>

              <option value="進行中">
                進行中
              </option>

              <option value="完了">
                完了
              </option>

              <option value="中止">
                中止
              </option>
            </select>

          </div>

          <!-- 期限 -->
          <div class="form-group">

            <label
              for="dueDate"
              class="form-label"
            >
              期限
            </label>

            <input
              id="dueDate"
              v-model="form.dueDate"
              class="form-control"
              type="date"
            />

          </div>

        </div>

        <!-- メモ -->
        <div class="form-group">

          <label
            for="memo"
            class="form-label"
          >
            メモ
          </label>

          <textarea
            id="memo"
            v-model="form.memo"
            class="form-control"
            placeholder="作業内容や注意事項など"
          ></textarea>

        </div>

      </section>

      <!-- エラー -->
      <p
        v-if="errorMessage"
        class="error-message"
      >
        {{ errorMessage }}
      </p>

      <!-- ボタン -->
      <div class="form-actions">

        <button
          type="button"
          class="btn btn-secondary"
          @click="handleCancel"
        >
          キャンセル
        </button>

        <button
          type="submit"
          class="btn btn-primary"
          :disabled="isSubmitting"
        >
          {{ isSubmitting ? '作成中...' : 'ToDoを作成' }}
        </button>

      </div>

    </form>

  </div>
</template>

<style scoped>
.todo-form {
  padding: 32px;
}

.form-section + .form-section {
  margin-top: 36px;
  padding-top: 36px;
  border-top: 1px solid var(--color-border);
}

.section-header {
  margin-bottom: 24px;
}

.section-header h2 {
  margin: 0;

  font-size: 20px;
}

.section-header p {
  margin: 6px 0 0;

  color: var(--color-text-secondary);

  font-size: 14px;
}

.form-group + .form-group {
  margin-top: 20px;
}

@media (max-width: 700px) {
  .todo-form {
    padding: 20px;
  }

  .form-row {
    grid-template-columns: 1fr;
  }
}
</style>
