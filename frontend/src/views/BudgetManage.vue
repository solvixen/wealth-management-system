<template>
  <div>
    <el-card>
      <el-form inline>
        <el-form-item label="月份">
          <el-date-picker v-model="currentMonth" type="month" value-format="YYYY-MM" @change="loadData" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="openSetBudget(null)">设置总预算</el-button>
          <el-button type="primary" @click="openSetBudget()">设置分类预算</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card style="margin-top: 20px">
      <el-table :data="budgets" stripe>
        <el-table-column prop="categoryName" label="预算类型" />
        <el-table-column prop="amount" label="预算金额" />
        <el-table-column prop="actual" label="实际支出" />
        <el-table-column prop="remaining" label="剩余金额" />
        <el-table-column label="进度">
          <template #default="{ row }">
            <el-progress :percentage="row.percentage" :color="row.percentage > 100 ? '#F56C6C' : '#67C23A'" />
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="setTotal ? '设置总预算' : '设置分类预算'" width="400px">
      <el-form :model="budgetForm">
        <el-form-item label="分类" v-if="!setTotal">
          <el-select v-model="budgetForm.categoryId" placeholder="请选择分类">
            <el-option v-for="cat in expenseCats" :key="cat.id" :label="cat.name" :value="cat.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="预算金额">
          <el-input-number v-model="budgetForm.amount" :min="0" :precision="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitBudget">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { setBudget, getBudgets, getBudgetProgress } from '@/api/budget'
import { getCategories } from '@/api/category'
import { ElMessage } from 'element-plus'

const currentMonth = ref('')
const budgets = ref([])
const expenseCats = ref([])
const dialogVisible = ref(false)
const setTotal = ref(false)
const budgetForm = ref({ categoryId: null, amount: 0 })

const loadData = async () => {
  if (!currentMonth.value) return
  const res = await getBudgets({ month: currentMonth.value })
  const progress = await getBudgetProgress({ month: currentMonth.value })
  const actualMap = {}
  if (progress.data) {
    actualMap['总预算'] = progress.data.totalExpense || 0
  }
  // 简化处理
  budgets.value = res.data.map(b => {
    const actual = b.categoryId ? 0 : (progress.data?.totalExpense || 0)
    const remaining = b.amount - actual
    const percentage = b.amount > 0 ? (actual / b.amount) * 100 : 0
    return {
      categoryName: b.categoryId ? '分类预算' : '总预算',
      amount: b.amount,
      actual,
      remaining,
      percentage: Math.min(percentage, 200)
    }
  })
}

const openSetBudget = (isTotal) => {
  setTotal.value = isTotal === undefined ? false : true
  budgetForm.value = { categoryId: null, amount: 0 }
  dialogVisible.value = true
}

const submitBudget = async () => {
  await setBudget({
    categoryId: setTotal.value ? null : budgetForm.value.categoryId,
    month: currentMonth.value,
    amount: budgetForm.value.amount
  })
  ElMessage.success('预算设置成功')
  dialogVisible.value = false
  loadData()
}

onMounted(async () => {
  const now = new Date()
  currentMonth.value = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`
  const catRes = await getCategories({ type: 0 })
  expenseCats.value = catRes.data || []
  loadData()
})
</script>