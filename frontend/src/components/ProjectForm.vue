<script setup lang="ts">
import type { ProjectCreateRequest } from '@/api/projectApi'

defineProps<{
  modelValue: ProjectCreateRequest
  isSubmitting: boolean
  errorMessage: string
  submitLabel: string
}>()

const emit = defineEmits<{
  'update:modelValue': [value: ProjectCreateRequest]
  submit: []
  cancel: []
}>()
</script>

<template>
  <form
    class="project-form card"
    @submit.prevent="emit('submit')"
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
          :value="modelValue.name"
          class="form-control"
          type="text"
          placeholder="例：個人開発プロジェクト"
          @input="emit('update:modelValue', {
            ...modelValue,
            name: ($event.target as HTMLInputElement).value
          })"
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
          :value="modelValue.description"
          class="form-control"
          placeholder="プロジェクトの概要を入力してください"
          @input="emit('update:modelValue', {
            ...modelValue,
            description: ($event.target as HTMLTextAreaElement).value
          })"
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
            :value="modelValue.goal"
            class="form-control"
            placeholder="このプロジェクトで達成したいこと"
            @input="emit('update:modelValue', {
              ...modelValue,
              goal: ($event.target as HTMLTextAreaElement).value
            })"
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
            :value="modelValue.motivation"
            class="form-control"
            placeholder="このプロジェクトへの意気込み"
            @input="emit('update:modelValue', {
              ...modelValue,
              motivation: ($event.target as HTMLTextAreaElement).value
            })"
          ></textarea>
        </div>
      </div>
    </section>

    <!-- プロジェクト計画 -->
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
            :value="modelValue.status"
            class="form-control"
            @change="emit('update:modelValue', {
              ...modelValue,
              status: ($event.target as HTMLSelectElement).value
            })"
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
            :value="modelValue.priority"
            class="form-control"
            @change="emit('update:modelValue', {
              ...modelValue,
              priority: ($event.target as HTMLSelectElement).value
            })"
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
            :value="modelValue.plannedHours"
            class="form-control"
            type="number"
            min="1"
            placeholder="例：50"
            @input="emit('update:modelValue', {
              ...modelValue,
              plannedHours: Number(($event.target as HTMLInputElement).value)
            })"
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
            :value="modelValue.startDate"
            class="form-control"
            type="date"
            @input="emit('update:modelValue', {
              ...modelValue,
              startDate: ($event.target as HTMLInputElement).value
            })"
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
            :value="modelValue.plannedEndDate"
            class="form-control"
            type="date"
            @input="emit('update:modelValue', {
              ...modelValue,
              plannedEndDate: ($event.target as HTMLInputElement).value
            })"
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

    <!-- ボタン -->
    <div class="form-actions">

      <button
        type="button"
        class="btn btn-secondary"
        @click="emit('cancel')"
      >
        キャンセル
      </button>

      <button
        type="submit"
        class="btn btn-primary"
        :disabled="isSubmitting"
      >
        {{ isSubmitting ? '処理中...' : submitLabel }}
      </button>

    </div>
  </form>
</template>
