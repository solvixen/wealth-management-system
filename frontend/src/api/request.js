import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({
    baseURL: '/api',
    timeout: 10000
})

// 请求拦截器
request.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token')
        console.log('请求URL:', config.url, 'Token:', token)  // 调试日志
        if (token) {
            config.headers.Authorization = `Bearer ${token}`
        }
        return config
    },
    error => Promise.reject(error)
)

// 响应拦截器
request.interceptors.response.use(
    response => {
        if (response.data.code === 200) {
            return response.data
        } else {
            ElMessage.error(response.data.message || '请求失败')
            return Promise.reject(response.data)
        }
    },
    error => {
        console.error('响应错误:', error.response?.status, error.response?.data)
        if (error.response?.status === 401) {
            localStorage.removeItem('token')
            ElMessage.error('登录已过期，请重新登录')
            window.location.href = '/login'
        } else {
            ElMessage.error(error.message || '网络错误')
        }
        return Promise.reject(error)
    }
)

export default request