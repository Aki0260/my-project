<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { // APIを呼び出す関数と型をインポート
  createProject,
  type ProjectCreateRequest, //ProjectCreateRequest型をインポート
} from '@/api/projectApi'

// 画面遷移に使用
const router = useRouter() //routerを使って画面遷移を行うためのオブジェクトを取得

// フォームに入力するプロジェクト情報
const form = ref<ProjectCreateRequest>({ //ProjectCreateRequest型のオブジェクトを初期化
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

// 登録処理中かどうか
//isSubmittingは、プロジェクト作成の登録処理中かどうかを示すフラグで、初期値はfalseに設定されています。登録処理中は、ボタンの無効化や「作成中...」の表示などに使用されます。
const isSubmitting = ref(false)

// エラーメッセージ
//errorMessageは、プロジェクト作成時に発生したエラーのメッセージを格納するための変数で、初期値は空文字列に設定されています。エラーが発生した場合に、この変数にエラーメッセージを設定し、画面上に表示することができます。
const errorMessage = ref('')

// プロジェクトを作成
//handleSubmitは、プロジェクト作成の登録処理を行う関数です。フォームの入力値をチェックし、必要な項目が入力されていない場合はエラーメッセージを表示します。入力値が有効な場合は、APIを呼び出してプロジェクトを登録し、登録成功後にプロジェクト一覧画面に遷移します。登録処理中はisSubmittingフラグをtrueに設定し、処理が完了したらfalseに戻します。
const handleSubmit = async () : Promise<void> => { //()は、handleSubmit関数が引数を受け取らないことを示しています。返す型はPromise<void>で、非同期処理を行うことを示しています。
  //Promise<void>と何も書かれていなかったら、関数が予測してこの型にする。
  errorMessage.value = ''

  // 必須項目の簡単なチェック
  //!form.value.nameは、フォームのプロジェクト名が空であるかどうかをチェックしています。空の場合はエラーメッセージを設定し、処理を終了します。
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

    // APIを呼び出してプロジェクトを登録
    await createProject(form.value)

    // 登録成功後、プロジェクト一覧へ戻る
    await router.push('/')
  } catch (error) {
    console.error(error)
    errorMessage.value = 'プロジェクトの作成に失敗しました。'
  } finally {
    isSubmitting.value = false
  }
}

// キャンセル
const handleCancel = () => {
  router.push('/')
}
</script>

<template>
  <div class="project-create-page">
    <div class="project-create-container">

      <!-- ヘッダー -->
      <div class="page-header">
        <div>
          <p class="page-eyebrow">PROJECT</p>
          <h1>新しいプロジェクトを作成</h1>
          <p class="page-description">
            プロジェクトの情報を入力して、計画を始めましょう。
          </p>
        </div>
      </div>

      <!-- フォーム -->
      <form
        class="project-form card"
        @submit.prevent="handleSubmit"
      >

        <!-- 基本情報 -->
        <section class="form-section">
          <div class="section-header">
            <h2>基本情報</h2>
            <p>プロジェクトの概要を入力してください。</p>
          </div>

          <div class="form-group">
            <label
              for="name"
              class="form-label"
            >
              プロジェクト名
            </label>

            <input
              id="name"
              v-model="form.name"
              class="form-control"
              type="text"
              placeholder="例：個人開発プロジェクト"
            />
          </div>

          <div class="form-group">
            <label
              for="description"
              class="form-label"
            >
              説明
            </label>

            <textarea
              id="description"
              v-model="form.description"
              class="form-control"
              placeholder="プロジェクトの概要を入力してください"
            ></textarea>
          </div>

          <div class="form-row">
            <div class="form-group">
              <label
                for="goal"
                class="form-label"
              >
                目標
              </label>

              <textarea
                id="goal"
                v-model="form.goal"
                class="form-control"
                placeholder="このプロジェクトで達成したいこと"
              ></textarea>
            </div>

            <div class="form-group">
              <label
                for="motivation"
                class="form-label"
              >
                意気込み
              </label>

              <textarea
                id="motivation"
                v-model="form.motivation"
                class="form-control"
                placeholder="このプロジェクトへの意気込み"
              ></textarea>
            </div>
          </div>
        </section>

        <!-- 計画 -->
        <section class="form-section">
          <div class="section-header">
            <h2>プロジェクト計画</h2>
            <p>期間や優先度を設定します。</p>
          </div>

          <div class="form-row form-row-3">

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
                <option value="未着手">未着手</option>
                <option value="進行中">進行中</option>
                <option value="完了">完了</option>
                <option value="中止">中止</option>
              </select>
            </div>

            <div class="form-group">
              <label
                for="priority"
                class="form-label"
              >
                優先度
              </label>

              <select
                id="priority"
                v-model="form.priority"
                class="form-control"
              >
                <option value="高">高</option>
                <option value="中">中</option>
                <option value="低">低</option>
              </select>
            </div>

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
                placeholder="例：50"
              />
            </div>

          </div>

          <div class="form-row">

            <div class="form-group">
              <label
                for="startDate"
                class="form-label"
              >
                開始日
              </label>

              <input
                id="startDate"
                v-model="form.startDate"
                class="form-control"
                type="date"
              />
            </div>

            <div class="form-group">
              <label
                for="plannedEndDate"
                class="form-label"
              >
                予定終了日
              </label>

              <input
                id="plannedEndDate"
                v-model="form.plannedEndDate"
                class="form-control"
                type="date"
              />
            </div>

          </div>
        </section>

        <!-- エラー -->
        <p
          v-if="errorMessage"
          class="error-message"
        >
          {{ errorMessage }}
        </p>

        <!-- アクション -->
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
            {{ isSubmitting ? '作成中...' : 'プロジェクトを作成' }}
          </button>
        </div>

      </form>
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

.page-header {
  margin-bottom: 28px;
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
  line-height: 1.3;
}

.page-description {
  margin: 10px 0 0;
  color: var(--color-text-secondary);
}

.project-form {
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

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin-top: 20px;
}

.form-row-3 {
  grid-template-columns: 1fr 1fr 1fr;
}

.error-message {
  margin: 24px 0 0;
  padding: 12px 16px;
  border-radius: var(--radius-sm);
  background-color: #fef2f2;
  color: var(--color-danger);
  font-size: 14px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 32px;
  padding-top: 24px;
  border-top: 1px solid var(--color-border);
}

@media (max-width: 700px) {
  .project-create-page {
    padding: 24px 16px;
  }

  .project-form {
    padding: 20px;
  }

  .form-row,
  .form-row-3 {
    grid-template-columns: 1fr;
  }

  .page-header h1 {
    font-size: 26px;
  }
}
</style>
