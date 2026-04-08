import request from './request'

export const getMonthlyTrend = (params) => request.get('/analysis/monthly-trend', { params })
export const getExpenseByCategory = (params) => request.get('/analysis/expense-by-category', { params })
export const getAdvice = () => request.get('/analysis/advice')