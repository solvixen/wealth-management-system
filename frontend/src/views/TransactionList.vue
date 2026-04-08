<template>
  <div>
    <el-card>
      <el-button type="primary" @click="dialogVisible = true">记一笔</el-button>
      <el-select v-model="filterMonth" placeholder="选择月份" clearable style="width: 150px; margin-left: 10px" @change="loadData">
        <el-option v-for="m in months" :key="m.value" :label="m.label" :value="m.value" />
      </el-select>
    </el-card>
    <el-card style="margin-top: 20px">
      <el-table :data="transactions" stripe>
        <el-table-column prop="transactionDate" label="日期" width="120" />
        <el-table-column prop="categoryName" label="分类" />
        <el-table-column prop="amount" label="金额">
          <template #default="{ row }">
            <span :style="{ color: row.type === 1 ? '#67C23A' : '#F56C6C' }">
              {{ row.type === 1 ? '+' : '-' }} ¥{{ row.amount }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="note" label="备注" />
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button size="small" @click="editRow(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteRow(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 记账对话框 -->
    <el-dialog v-model="dialogVisible" :title="editId ? '编辑记录' : '新增记录'" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="类型">
          <el-radio-group v-model="form.type">
            <el-radio :label="0">支出</el-radio>
            <el-radio :label="1">收入</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.categoryId" placeholder="请选择分类">
            <el-option v-for="cat in categories" :key="cat.id" :label="cat.name" :value="cat.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="金额">
          <el-input-number v-model="form.amount" :precision="2" :min="0.01" />
        </el-form-item>
        <el-form-item label="日期">
          <el-date-picker v-model="form.transactionDate" type="date" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.note" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { getTransactions, addTransaction, updateTransaction, deleteTransaction } from '@/api/transaction'
import { getCategories } from '@/api/category'
import { ElMessage, ElMessageBox } from 'element-plus'

const transactions = ref([])
const categories = ref([])
const filterMonth = ref('')
const dialogVisible = ref(false)
const editId = ref(null)
const form = ref({ type: 0, categoryId: null, amount: 0, transactionDate: '', note: '' })

const months = []
const now = new Date()
for (let i = 0; i < 12; i++) {
  const d = new Date(now.getFullYear(), now.getMonth() - i, 1)
  const value = `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}`
  months.push({ value, label: value })
}

const loadData = async () => {
  const res = await getTransactions({ month: filterMonth.value })
  // 补充分类名称
  const catMap = {}
  categories.value.forEach(c => { catMap[c.id] = c.name })
  transactions.value = res.data.map(t => ({ ...t, categoryName: catMap[t.categoryId] || '未知' }))
}

const loadCategories = async () => {
  const res = await getCategories()
  categories.value = res.data
}

const save = async () => {
  if (!form.value.categoryId) {
    ElMessage.warning('请选择分类')
    return
  }
  if (editId.value) {
    await updateTransaction(editId.value, form.value)
    ElMessage.success('更新成功')
  } else {
    await addTransaction(form.value)
    ElMessage.success('添加成功')
  }
  dialogVisible.value = false
  loadData()
}

const editRow = (row) => {
  editId.value = row.id
  form.value = { ...row }
  dialogVisible.value = true
}

const deleteRow = (id) => {
  ElMessageBox.confirm('确定删除吗？', '提示').then(async () => {
    await deleteTransaction(id)
    ElMessage.success('删除成功')
    loadData()
  })
}

onMounted(() => {
  loadCategories()
  loadData()
})
watch(filterMonth, () => loadData())
</script>