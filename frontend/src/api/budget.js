import request from './request'

export const setBudget = (data) => request.post('/budgets', data)
export const getBudgets = (params) => request.get('/budgets', { params })
export const getBudgetProgress = (params) => request.get('/budgets/progress', { params })