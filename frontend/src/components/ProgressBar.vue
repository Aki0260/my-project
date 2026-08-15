<script setup lang="ts">
defineProps<{
  progress: number
}>()
</script>

<template>
  <div class="progress-wrapper">
    <div class="progress-header">
      <span>進捗</span>
      <span>{{ progress }}%</span>
    </div>

    <div class="progress">
      <div
        class="progress-bar"
        :style="{ width: `${progress}%` }"
      ></div>
    </div>
  </div>
</template>

<style scoped>
.progress-wrapper {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.progress-header {
  display: flex;
  justify-content: space-between;
  font-size: 14px;
  font-weight: 600;
}
</style>
