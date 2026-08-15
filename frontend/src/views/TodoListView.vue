<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getTodos, type Todo } from '@/api/todoApi'

const route = useRoute()
const router = useRouter()

// ToDo一覧
const todos = ref<Todo[]>([])

// エラーメッセージ
const errorMessage = ref('')

// プロジェクトID
const projectId = Number(route.params.id)

// ToDo一覧を取得
onMounted(async () => {
  try {
    todos.value = await getTodos()
  } catch (error) {
    console.error(error)
    errorMessage.value = 'ToDoの取得に失敗しました。'
  }
})

// ToDo作成画面へ
const handleCreate = () => {
  router.push(`/projects/${projectId}/todos/new`)
}

// ToDo詳細画面へ
const handleDetail = (id: number) => {
  router.push(`/todos/${id}`)
}

// プロジェクト詳細へ戻る
const handleBack = () => {
  router.push(`/projects/${projectId}`)
}
</script>

<template>
  <div class="page">

    <!-- 戻る -->
    <button
      type="button"
      class="btn-back"
      @click="handleBack"
    >
      ← プロジェクト詳細へ戻る
    </button>

    <!-- ヘッダー -->
    <div class="page-header">
      <div>
        <p class="page-description">TODO</p>

        <h1 class="page-title">
          ToDo一覧
        </h1>
      </div>

      <button
        type="button"
        class="btn btn-primary"
        @click="handleCreate"
      >
        ＋ ToDoを追加
      </button>
    </div>

    <!-- エラー -->
    <p
      v-if="errorMessage"
      class="error-message"
    >
      {{ errorMessage }}
    </p>

    <!-- ToDoがない場合 -->
    <div
      v-else-if="todos.length === 0"
      class="empty-state"
    >
      <h2 class="empty-state-title">
        ToDoがありません
      </h2>

      <p class="empty-state-description">
        このプロジェクトのToDoを追加してみましょう。
      </p>

      <button
        type="button"
        class="btn btn-primary"
        @click="handleCreate"
      >
        ＋ ToDoを追加
      </button>
    </div>

    <!-- ToDo一覧 -->
    <div
      v-else
      class="todo-list"
    >
      <div
        v-for="todo in todos"
        :key="todo.id"
        class="todo-card"
        @click="handleDetail(todo.id)"
      >
        <div class="todo-card-header">
          <h2>
            {{ todo.title }}
          </h2>

          <span class="badge badge-primary">
            {{ todo.status }}
          </span>
        </div>

        <div class="todo-card-info">
          <span>
            予定工数：{{ todo.plannedHours }}時間
          </span>

          <span>
            期限：{{ todo.dueDate || '未設定' }}
          </span>
        </div>

        <p
          v-if="todo.memo"
          class="todo-memo"
        >
          {{ todo.memo }}
        </p>
      </div>
    </div>

  </div>
</template>

<style scoped>
.todo-list {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 18px;
}

.todo-card {
  padding: 22px;

  background-color: var(--color-surface);

  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);

  box-shadow: var(--shadow-sm);

  cursor: pointer;

  transition:
    transform 0.2s,
    box-shadow 0.2s,
    border-color 0.2s;
}

.todo-card:hover {
  transform: translateY(-2px);

  box-shadow: var(--shadow-md);

  border-color: #bfdbfe;
}

.todo-card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;

  gap: 12px;
}

.todo-card-header h2 {
  margin: 0;

  font-size: 18px;
}

.todo-card-info {
  display: flex;
  gap: 16px;

  margin-top: 14px;

  color: var(--color-text-secondary);

  font-size: 14px;
}

.todo-memo {
  margin: 14px 0 0;

  color: var(--color-text-secondary);

  font-size: 14px;
  line-height: 1.6;
}

@media (max-width: 768px) {
  .todo-list {
    grid-template-columns: 1fr;
  }

  .page-header {
    align-items: flex-start;
  }

  .todo-card-info {
    flex-direction: column;
    gap: 6px;
  }
}
</style>
