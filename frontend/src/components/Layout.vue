<template>
  <el-container style="height: 100vh">
    <el-aside width="200px">
      <SideMenu />
    </el-aside>
    <el-container>
      <el-header style="background-color: #409EFF; color: white; display: flex; align-items: center; justify-content: space-between">
        <h2>理财管理系统</h2>
        <el-button type="text" style="color: white" @click="logout">退出登录</el-button>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import SideMenu from './SideMenu.vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const logout = () => {
  localStorage.removeItem('token')
  ElMessage.success('已退出')
  router.push('/login')
}
</script>