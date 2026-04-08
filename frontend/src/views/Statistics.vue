<template>
  <div>
    <el-card>
      <el-select v-model="selectedYear" @change="loadTrend">
        <el-option v-for="y in [2024,2025,2026]" :key="y" :label="y" :value="y" />
      </el-select>
      <el-select v-model="selectedMonth" @change="loadCategory" placeholder="选择月份" clearable>
        <el-option v-for="m in months" :key="m.value" :label="m.label" :value="m.value" />
      </el-select>
    </el-card>
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card>
          <div ref="trendChart" style="height: 400px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div ref="pieChart" style="height: 400px"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMonthlyTrend, getExpenseByCategory } from '@/api/analysis'
import * as echarts from 'echarts'

const selectedYear = ref(2026)
const selectedMonth = ref('')
const trendChart = ref(null)
const pieChart = ref(null)

const months = []
for (let i = 1; i <= 12; i++) {
  months.push({ value: `2026-${String(i).padStart(2,'0')}`, label: `${i}月` })
}

const loadTrend = async () => {
  const res = await getMonthlyTrend({ year: selectedYear.value })
  const data = res.data.trend
  const chart = echarts.init(trendChart.value)
  chart.setOption({
    title: { text: '月度收支趋势' },
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: data.map(d => d.month + '月') },
    yAxis: { type: 'value' },
    series: [
      { name: '收入', type: 'line', data: data.map(d => d.income), color: '#67C23A' },
      { name: '支出', type: 'line', data: data.map(d => d.expense), color: '#F56C6C' }
    ]
  })
}

const loadCategory = async () => {
  if (!selectedMonth.value) return
  const res = await getExpenseByCategory({ month: selectedMonth.value })
  const data = res.data
  const chart = echarts.init(pieChart.value)
  chart.setOption({
    title: { text: '支出构成' },
    tooltip: { trigger: 'item' },
    series: [{
      type: 'pie',
      radius: '50%',
      data: data.map(item => ({ name: item.categoryName, value: item.amount }))
    }]
  })
}

onMounted(() => {
  loadTrend()
})
</script>