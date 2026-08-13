<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getProject, deleteProject, type Project } from '@/api/projectApi'

const route = useRoute()
const router = useRouter()

// プロジェクト情報
const project = ref<Project | null>(null)

// エラーメッセージ
const errorMessage = ref('')

// プロジェクト詳細を取得
onMounted(async () => {
  try {
    // URLからプロジェクトIDを取得
    const id = Number(route.params.id)

    // APIからプロジェクトを取得
    project.value = await getProject(id)
  } catch (error) {
    console.error(error)
    errorMessage.value = 'プロジェクトの取得に失敗しました。'
  }
})

const handleDelete = async () => {
  console.log('削除ボタンが押された')
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

// 編集画面へ移動
const handleEdit = () => {
  if (!project.value) {
    return
  }

  router.push(`/projects/${project.value.id}/edit`)
}

// 一覧に戻る
const handleBack = () => {
  router.push('/')
}
</script>

<template>
  <div class="project-detail">

    <!--
      TODO: 共通コンポーネント化
      戻るボタンは共通のButtonコンポーネントにする
    -->
    <button type="button" @click="handleBack">
      ← 一覧に戻る
    </button>


    <!-- エラー -->
    <p v-if="errorMessage">
      {{ errorMessage }}
    </p>


    <!-- プロジェクト情報 -->
    <div v-if="project">

      <!-- TODO: ProjectDetailHeader.vue に切り出す -->
      <h1>{{ project.name }}</h1>

      <p>
        {{ project.description }}
      </p>


      <!-- TODO: ProjectInfo.vue に切り出す -->
      <section>
        <h2>目標</h2>
        <p>{{ project.goal }}</p>

        <h2>意気込み</h2>
        <p>{{ project.motivation }}</p>
      </section>


      <!-- TODO: ProjectSchedule.vue に切り出す -->
      <section>
        <h2>プロジェクト情報</h2>

        <p>
          ステータス：
          {{ project.status }}
        </p>

        <p>
          優先度：
          {{ project.priority }}
        </p>

        <p>
          開始日：
          {{ project.startDate }}
        </p>

        <p>
          予定終了日：
          {{ project.plannedEndDate }}
        </p>

        <p>
          予定工数：
          {{ project.plannedHours }}時間
        </p>
      </section>


      <!-- TODO: ProgressBar.vue を追加 -->
      <section>
        <h2>進行度</h2>

        <!--
          ProgressBar.vue
          プロジェクトの進行度を視覚的に表示する
        -->
      </section>


      <!-- TODO: ProjectActionButtons.vue に切り出す -->
      <div>
        <button type="button" class="btn btn-primary" @click="handleEdit">
          編集
        </button>

        <button type="button" class="btn btn-danger" @click="handleDelete">
          プロジェクトを削除
        </button>
      </div>

    </div>

  </div>
</template>
