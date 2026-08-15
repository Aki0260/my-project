<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  getTodo,
  updateTodo,
  type Todo,
  type TodoRequest,
} from '@/api/todoApi'

const route = useRoute()
const router = useRouter()

// ToDo情報
const todo = ref<Todo | null>(null)

// フォーム
const form = ref<TodoRequest>({
  projectId: 0,
  title: '',
  plannedHours: 0,
  status: '未着手',
  dueDate: '',
  memo: '',
})

// 更新処理中かどうか
const isSubmitting = ref(false)

// エラーメッセージ
const errorMessage = ref('')

// ToDoを取得
onMounted(async () => {
  try {
    const id = Number(route.params.id)

    todo.value = await getTodo(id)

    // 取得したToDoをフォームにセット
    form.value = {
      projectId: todo.value.projectId,
      title: todo.value.title,
      plannedHours: todo.value.plannedHours,
      status: todo.value.status,
      dueDate: todo.value.dueDate,
      memo: todo.value.memo,
    }
  } catch (error) {
    console.error(error)
    errorMessage.value =
      'ToDoの取得に失敗しました。'
  }
})

// 更新処理
const handleSubmit = async (): Promise<void> => {
  if (!todo.value) {
    return
  }

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

    await updateTodo(
      todo.value.id,
      form.value
    )

    // 更新成功後、ToDo一覧へ戻る
    await router.push(
      `/projects/${todo.value.projectId}/todos`
    )
  } catch (error) {
    console.error(error)
    errorMessage.value =
      'ToDoの更新に失敗しました。'
  } finally {
    isSubmitting.value = false
  }
}

// キャンセル
const handleCancel = () => {
  if (!todo.value) {
    return
  }

  router.push(
    `/todos/${todo.value.id}`
  )
}
</script>

<template>
  <div class="page">

    <!-- 詳細画面へ戻る -->
    <button
      type="button"
      class="btn-back"
      @click="handleCancel"
    >
      ← ToDo詳細へ戻る
    </button>

    <!-- ヘッダー -->
    <div class="page-header">
      <div>
        <p class="page-description">
          TODO
        </p>

        <h1 class="page-title">
          ToDoを編集
        </h1>

        <p class="page-description">
          ToDoの内容を変更します。
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
            ToDoの内容を変更してください。
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
            ステータスと期限を変更します。
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
          {{ isSubmitting ? '保存中...' : '変更を保存' }}
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
