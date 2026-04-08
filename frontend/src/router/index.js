import { createRouter, createWebHistory } from 'vue-router'
import Layout from '../components/Layout.vue'

const routes = [
    { path: '/login', component: () => import('../views/Login.vue') },
    { path: '/register', component: () => import('../views/Register.vue') },
    {
        path: '/',
        component: Layout,
        redirect: '/dashboard',
        children: [
            { path: 'dashboard', name: 'Dashboard', component: () => import('../views/Dashboard.vue') },
            { path: 'transactions', name: 'Transactions', component: () => import('../views/TransactionList.vue') },
            { path: 'categories', name: 'Categories', component: () => import('../views/CategoryManage.vue') },
            { path: 'budget', name: 'Budget', component: () => import('../views/BudgetManage.vue') },
            { path: 'saving', name: 'Saving', component: () => import('../views/SavingGoal.vue') },
            { path: 'statistics', name: 'Statistics', component: () => import('../views/Statistics.vue') },
            { path: 'advice', name: 'Advice', component: () => import('../views/Advice.vue') }
        ]
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token')
    if (to.path !== '/login' && to.path !== '/register' && !token) {
        next('/login')
    } else {
        next()
    }
})

export default router