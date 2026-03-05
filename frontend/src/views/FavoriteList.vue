<script setup>
import { ref, onMounted } from 'vue'
import { favoriteApi } from '../api/index.js'

// ── 狀態 ─────────────────────────────────────────────────────────────
const userId  = ref('A1234567890')   // TODO: 整合登入後替換
const list    = ref([])
const msg     = ref({ text: '', type: '' })
const loading = ref(false)

// ── 編輯 Modal ────────────────────────────────────────────────────────
const showEdit  = ref(false)
const editForm  = ref({})

// ── 商品清單 (從後端取得，供 Modal 下拉選單用) ─────────────────────
import { productApi } from '../api/index.js'
const products = ref([])

// ── 載入資料 ──────────────────────────────────────────────────────────
const fetchList = async () => {
  loading.value = true
  try {
    const res = await favoriteApi.getList(userId.value)
    list.value = res.data.data ?? []
  } catch (e) {
    showMsg(e.response?.data?.message ?? '載入失敗', 'error')
  } finally {
    loading.value = false
  }
}

const fetchProducts = async () => {
  const res = await productApi.getAll()
  products.value = res.data.data ?? []
}

onMounted(() => { fetchList(); fetchProducts() })

// ── 刪除 ──────────────────────────────────────────────────────────────
const remove = async (sn) => {
  if (!confirm('確定要刪除這筆喜好商品？')) return
  try {
    await favoriteApi.remove(sn, userId.value)
    showMsg('刪除成功', 'success')
    fetchList()
  } catch (e) {
    showMsg(e.response?.data?.message ?? '刪除失敗', 'error')
  }
}

// ── 開啟更改 Modal ────────────────────────────────────────────────────
const openEdit = (row) => {
  editForm.value = {
    sn:               row.sn,
    userId:           userId.value,
    productNo:        products.value.find(p => p.productName === row.productName)?.no ?? null,
    purchaseQuantity: row.purchaseQuantity,
    account:          row.account,
  }
  showEdit.value = true
}

// ── 送出更改 ──────────────────────────────────────────────────────────
const submitEdit = async () => {
  try {
    await favoriteApi.update(editForm.value.sn, {
      userId:           editForm.value.userId,
      productNo:        editForm.value.productNo,
      purchaseQuantity: editForm.value.purchaseQuantity,
      account:          editForm.value.account,
    })
    showEdit.value = false
    showMsg('更新成功', 'success')
    fetchList()
  } catch (e) {
    showMsg(e.response?.data?.message ?? '更新失敗', 'error')
  }
}

// ── 工具 ──────────────────────────────────────────────────────────────
const showMsg = (text, type) => {
  msg.value = { text, type }
  setTimeout(() => msg.value = { text: '', type: '' }, 3000)
}

const fmt = (val) =>
  Number(val).toLocaleString('zh-TW', { minimumFractionDigits: 2 })
</script>

<template>
  <div>
    <h1 class="page-title">📋 喜好金融商品清單</h1>

    <!-- 使用者 ID 輸入 -->
    <div class="card" style="display:flex;align-items:center;gap:1rem;">
      <label style="font-weight:600;white-space:nowrap;">使用者 ID：</label>
      <input v-model="userId" style="max-width:220px;padding:.5rem .8rem;border:1.5px solid #c8d4e8;border-radius:6px;" />
      <button class="btn btn-primary" @click="fetchList" :disabled="loading">
        {{ loading ? '查詢中...' : '查詢' }}
      </button>
    </div>

    <!-- 提示訊息 -->
    <div v-if="msg.text" :class="['alert', msg.type === 'success' ? 'alert-success' : 'alert-error']">
      {{ msg.text }}
    </div>

    <!-- 清單表格 -->
    <div class="card" style="overflow-x:auto;">
      <table>
        <thead>
          <tr>
            <th>SN</th><th>產品名稱</th><th>產品價格</th><th>手續費率</th>
            <th>購買數量</th><th>扣款帳號</th><th>總手續費</th><th>預計扣款</th>
            <th>聯絡信箱</th><th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="list.length === 0">
            <td colspan="10" style="text-align:center;color:#999;padding:2rem;">尚無喜好商品</td>
          </tr>
          <tr v-for="row in list" :key="row.sn">
            <td>{{ row.sn }}</td>
            <td>{{ row.productName }}</td>
            <td>{{ fmt(row.price) }}</td>
            <td>{{ (row.feeRate * 100).toFixed(4) }}%</td>
            <td>{{ row.purchaseQuantity }}</td>
            <td>{{ row.account }}</td>
            <td>{{ fmt(row.totalFee) }}</td>
            <td>{{ fmt(row.totalAmount) }}</td>
            <td>{{ row.email }}</td>
            <td style="white-space:nowrap;display:flex;gap:.4rem;">
              <button class="btn btn-warning btn-sm" @click="openEdit(row)">修改</button>
              <button class="btn btn-danger btn-sm" @click="remove(row.sn)">刪除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 編輯 Modal -->
    <div v-if="showEdit" class="modal-mask" @click.self="showEdit=false">
      <div class="modal-box">
        <h3>✏️ 修改喜好商品</h3>

        <div class="form-group">
          <label>產品</label>
          <select v-model="editForm.productNo">
            <option v-for="p in products" :key="p.no" :value="p.no">
              {{ p.productName }} (價格: {{ fmt(p.price) }})
            </option>
          </select>
        </div>

        <div class="form-group">
          <label>購買數量</label>
          <input v-model.number="editForm.purchaseQuantity" type="number" min="1" />
        </div>

        <div class="form-group">
          <label>扣款帳號</label>
          <input v-model="editForm.account" type="text" />
        </div>

        <div class="modal-actions">
          <button class="btn" @click="showEdit=false">取消</button>
          <button class="btn btn-primary" @click="submitEdit">確認修改</button>
        </div>
      </div>
    </div>
  </div>
</template>
