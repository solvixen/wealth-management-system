<template>
  <el-menu :default-active="$route.path" router style="height: 100%">
    <el-menu-item index="/dashboard">仪表盘</el-menu-item>
    <el-menu-item index="/transactions">收支记录</el-menu-item>
    <el-menu-item index="/categories">分类管理</el-menu-item>
    <el-menu-item index="/budget">预算管理</el-menu-item>
    <el-menu-item index="/saving">储蓄目标</el-menu-item>
    <el-menu-item index="/statistics">统计分析</el-menu-item>
    <el-menu-item index="/advice">理财建议</el-menu-item>
  </el-menu>
</template>

<script setup>
</script>