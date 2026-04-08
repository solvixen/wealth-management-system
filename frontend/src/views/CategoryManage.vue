<template>
  <div>
    <el-card>
      <el-button type="primary" @click="openAddDialog">新增分类</el-button>
    </el-card>
    <el-card style="margin-top: 20px">
      <el-tabs v-model="activeTab" @tab-click="loadData">
        <el-tab-pane label="支出分类" name="expense"></el-tab-pane>
        <el-tab-pane label="收入分类" name="income"></el-tab-pane>
      </el-tabs>
      <el-table :data="categories" stripe>
        <el-table-column prop="name" label="分类名称" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button size="small" @click="editCategory(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="editId ? '编辑分类' : '新增分类'" width="400px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="分类名称">
          <el-input v-model="form.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="类型" v-if="!editId">
          <el-radio-group v-model="form.type">
            <el-radio :label="0">支出</el-radio>
            <el-radio :label="1">收入</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveCategory">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getCategories, addCategory, updateCategory, deleteCategory as deleteCategoryApi } from '@/api/category'
import { ElMessage, ElMessageBox } from 'element-plus'

const activeTab = ref('expense')
const categories = ref([])
const dialogVisible = ref(false)
const editId = ref(null)
const form = ref({ name: '', type: 0 })

const loadData = async () => {
  const type = activeTab.value === 'expense' ? 0 : 1
  const res = await getCategories({ type })
  categories.value = res.data || []
}

const openAddDialog = () => {
  editId.value = null
  form.value = { name: '', type: activeTab.value === 'expense' ? 0 : 1 }
  dialogVisible.value = true
}

const editCategory = (row) => {
  editId.value = row.id
  form.value = { name: row.name, type: row.type }
  dialogVisible.value = true
}

const saveCategory = async () => {
  if (!form.value.name.trim()) {
    ElMessage.warning('请输入分类名称')
    return
  }
  if (editId.value) {
    await updateCategory(editId.value, { name: form.value.name })
    ElMessage.success('更新成功')
  } else {
    await addCategory({ name: form.value.name, type: form.value.type })
    ElMessage.success('添加成功')
  }
  dialogVisible.value = false
  loadData()
}

const handleDelete = (id) => {
  ElMessageBox.confirm('删除分类将同时删除该分类下的所有收支记录，确定吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await deleteCategoryApi(id)
    ElMessage.success('删除成功')
    loadData()
  }).catch(() => {})
}

onMounted(() => {
  loadData()
})
</script>