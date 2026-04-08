<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card>
          <template #header>本月总收入</template>
          <div style="font-size: 28px; color: #67C23A">¥ {{ totalIncome }}</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <template #header>本月总支出</template>
          <div style="font-size: 28px; color: #F56C6C">¥ {{ totalExpense }}</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <template #header>本月结余</template>
          <div style="font-size: 28px" :style="{ color: netAmount >= 0 ? '#67C23A' : '#F56C6C' }">¥ {{ netAmount }}</div>
        </el-card>
      </el-col>
    </el-row>
    <el-row style="margin-top: 20px">
      <el-col :span="24">
        <el-card>
          <div ref="trendChart" style="height: 400px"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMonthlyTrend } from '@/api/analysis'
import { getTransactions } from '@/api/transaction'
import * as echarts from 'echarts'

const totalIncome = ref(0)
const totalExpense = ref(0)
const netAmount = ref(0)
const trendChart = ref(null)

onMounted(async () => {
  const now = new Date()
  const year = now.getFullYear()
  const month = String(now.getMonth() + 1).padStart(2, '0')
  // 获取本月收支总额
  const res = await getTransactions({ month: `${year}-${month}` })
  if (res.data) {
    totalIncome.value = res.data.filter(t => t.type === 1).reduce((sum, t) => sum + t.amount, 0)
    totalExpense.value = res.data.filter(t => t.type === 0).reduce((sum, t) => sum + t.amount, 0)
    netAmount.value = totalIncome.value - totalExpense.value
  }
  // 绘制月度趋势图
  const trendRes = await getMonthlyTrend({ year })
  const trendData = trendRes.data.trend
  const chart = echarts.init(trendChart.value)
  chart.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: trendData.map(t => t.month + '月') },
    yAxis: { type: 'value' },
    series: [
      { name: '收入', type: 'line', data: trendData.map(t => t.income), smooth: true, lineStyle: { color: '#67C23A' } },
      { name: '支出', type: 'line', data: trendData.map(t => t.expense), smooth: true, lineStyle: { color: '#F56C6C' } }
    ]
  })
})
</script>