<template>
  <div id="layout">
    <header class="navbar">
      <div class="brand">💰 金融商品喜好紀錄系統</div>
      <nav>
        <router-link to="/list">喜好清單</router-link>
        <router-link to="/add">新增喜好</router-link>
      </nav>
    </header>
    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<style scoped>
.navbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 2rem;
  height: 60px;
  background: var(--color-primary);
  color: #fff;
  box-shadow: 0 2px 8px rgba(0,0,0,.25);
}
.brand { font-size: 1.2rem; font-weight: 700; }
nav { display: flex; gap: 1.5rem; }
nav a { color: #fff; text-decoration: none; font-weight: 500; opacity: .8; transition: opacity .2s; }
nav a.router-link-active, nav a:hover { opacity: 1; border-bottom: 2px solid #fff; }
.main-content { padding: 2rem; max-width: 1100px; margin: 0 auto; }
</style>
