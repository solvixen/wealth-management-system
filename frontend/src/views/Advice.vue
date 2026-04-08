<template>
  <el-card>
    <template #header>智能理财建议</template>
    <el-timeline>
      <el-timeline-item v-for="(item, idx) in adviceList" :key="idx" :timestamp="new Date().toLocaleDateString()" placement="top">
        <el-card shadow="hover">
          <p>{{ item }}</p>
        </el-card>
      </el-timeline-item>
    </el-timeline>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAdvice } from '@/api/analysis'

const adviceList = ref([])
onMounted(async () => {
  const res = await getAdvice()
  adviceList.value = res.data || []
})
</script>