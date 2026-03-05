import axios from 'axios'

const api = axios.create({
    baseURL: '/api',
    timeout: 10000,
    headers: { 'Content-Type': 'application/json' },
})

export const favoriteApi = {
    getList: (userId) => api.get('/favorites', { params: { userId } }),
    add: (data) => api.post('/favorites', data),
    update: (sn, data) => api.put(`/favorites/${sn}`, data),
    remove: (sn, userId) => api.delete(`/favorites/${sn}`, { params: { userId } }),
}

export const productApi = {
    getAll: () => api.get('/products'),
}
