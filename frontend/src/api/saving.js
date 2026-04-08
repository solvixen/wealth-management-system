import request from './request'

export const getSavingGoals = () => request.get('/saving-goals')
export const addSavingGoal = (data) => request.post('/saving-goals', data)
export const updateSavingGoal = (id, data) => request.put(`/saving-goals/${id}`, data)
export const deleteSavingGoal = (id) => request.delete(`/saving-goals/${id}`)
export const refreshProgress = () => request.post('/saving-goals/refresh-progress')