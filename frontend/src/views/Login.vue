<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2 style="text-align: center">理财管理系统登录</h2>
      <el-form :model="form" :rules="rules" ref="formRef" @submit.prevent="handleLogin">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="用户名" prefix-icon="User" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" prefix-icon="Lock" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" native-type="submit" style="width: 100%">登录</el-button>
        </el-form-item>
        <el-form-item>
          <el-button text @click="$router.push('/register')">还没有账号？立即注册</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { login } from '@/api/auth';

const router = useRouter();
const formRef = ref(null);
const form = reactive({
  username: '',
  password: ''
});
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
};

const handleLogin = async () => {
  try {
    await formRef.value.validate();
    const res = await login(form);
    if (res.code === 200) {
      localStorage.setItem('token', res.data);
      ElMessage.success('登录成功');
      await router.push('/dashboard');
    } else {
      ElMessage.error(res.message || '登录失败');
    }
  } catch (error) {
    console.error('登录出错', error);
    ElMessage.error('登录失败，请检查网络或联系管理员');
  }
};
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}
.login-card {
  width: 400px;
}
</style>