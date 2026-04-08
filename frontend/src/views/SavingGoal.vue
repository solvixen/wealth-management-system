<template>
  <div>
    <el-card>
      <el-button type="primary" @click="openAddDialog">新增储蓄目标</el-button>
      <el-button type="success" @click="handleRefreshProgress">刷新进度</el-button>
    </el-card>
    <el-card style="margin-top: 20px">
      <el-table :data="goals" stripe>
        <el-table-column prop="goalName" label="目标名称" />
        <el-table-column prop="targetAmount" label="目标金额" />
        <el-table-column prop="savedAmount" label="已存金额" />
        <el-table-column label="进度">
          <template #default="{ row }">
            <el-progress :percentage="getProgress(row)" :color="row.status === 1 ? '#67C23A' : '#409EFF'" />
          </template>
        </el-table-column>
        <el-table-column prop="deadline" label="截止日期" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '已完成' : '进行中' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button size="small" @click="editGoal(row)" :disabled="row.status === 1">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="editId ? '编辑储蓄目标' : '新增储蓄目标'" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="目标名称">
          <el-input v-model="form.goalName" placeholder="例如：买车基金" />
        </el-form-item>
        <el-form-item label="目标金额">
          <el-input-number v-model="form.targetAmount" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="截止日期">
          <el-date-picker v-model="form.deadline" type="date" value-format="YYYY-MM-DD" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveGoal">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import {
  getSavingGoals,
  addSavingGoal,
  updateSavingGoal,
  deleteSavingGoal as deleteSavingGoalApi,
  refreshProgress as refreshProgressApi
} from '@/api/saving'
import { ElMessage, ElMessageBox } from 'element-plus'

const goals = ref([])
const dialogVisible = ref(false)
const editId = ref(null)
const form = ref({ goalName: '', targetAmount: 0, deadline: '' })

const loadData = async () => {
  const res = await getSavingGoals()
  goals.value = res.data || []
}

const getProgress = (row) => {
  if (row.targetAmount <= 0) return 0
  const percent = (row.savedAmount / row.targetAmount) * 100
  return Math.min(percent, 100)
}

const openAddDialog = () => {
  editId.value = null
  form.value = { goalName: '', targetAmount: 0, deadline: '' }
  dialogVisible.value = true
}

const editGoal = (row) => {
  editId.value = row.id
  form.value = { goalName: row.goalName, targetAmount: row.targetAmount, deadline: row.deadline }
  dialogVisible.value = true
}

const saveGoal = async () => {
  if (!form.value.goalName.trim()) {
    ElMessage.warning('请输入目标名称')
    return
  }
  if (editId.value) {
    await updateSavingGoal(editId.value, form.value)
    ElMessage.success('更新成功')
  } else {
    await addSavingGoal(form.value)
    ElMessage.success('添加成功')
  }
  dialogVisible.value = false
  loadData()
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定删除该储蓄目标吗？', '提示').then(async () => {
    await deleteSavingGoalApi(id)
    ElMessage.success('删除成功')
    loadData()
  }).catch(() => {})
}

const handleRefreshProgress = async () => {
  await refreshProgressApi()
  ElMessage.success('进度已刷新')
  loadData()
}

onMounted(() => {
  loadData()
})
</script>