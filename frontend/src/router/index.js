import { createRouter, createWebHistory } from 'vue-router'
import FavoriteList from '../views/FavoriteList.vue'
import AddFavorite from '../views/AddFavorite.vue'

const routes = [
    { path: '/', redirect: '/list' },
    { path: '/list', component: FavoriteList },
    { path: '/add', component: AddFavorite },
]

export default createRouter({
    history: createWebHistory(),
    routes,
})
