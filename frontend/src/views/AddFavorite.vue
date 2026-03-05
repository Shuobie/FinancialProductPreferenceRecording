<script setup>
import { ref, onMounted, computed } from 'vue'
import { favoriteApi, productApi } from '../api/index.js'

const userId   = ref('A1236456789')  // TODO: 整合登入後替換
const products = ref([])
const msg      = ref({ text: '', type: '' })

const form = ref({
  userId:           userId.value,
  productNo:        null,
  purchaseQuantity: 1,
  account:          '',
})

// 自動同步 userId 到 form
const syncUserId = () => { form.value.userId = userId.value }

// 預覽計算（前端即時顯示，以後端實際計算為準）
const selectedProduct = computed(() =>
  products.value.find(p => p.no === form.value.productNo)
)
const previewFee = computed(() => {
  if (!selectedProduct.value || !form.value.purchaseQuantity) return 0
  const { price, feeRate } = selectedProduct.value
  return (price * form.value.purchaseQuantity * feeRate).toFixed(2)
})
const previewTotal = computed(() => {
  if (!selectedProduct.value || !form.value.purchaseQuantity) return 0
  const { price } = selectedProduct.value
  return (price * form.value.purchaseQuantity * 1 + Number(previewFee.value)).toFixed(2)
})

onMounted(async () => {
  const res = await productApi.getAll()
  products.value = res.data.data ?? []
})

const submit = async () => {
  if (!form.value.productNo || !form.value.account || form.value.purchaseQuantity < 1) {
    showMsg('請填寫所有欄位', 'error')
    return
  }
  try {
    await favoriteApi.add({ ...form.value })
    showMsg('新增成功！', 'success')
    form.value.productNo = null
    form.value.purchaseQuantity = 1
    form.value.account = ''
  } catch (e) {
    showMsg(e.response?.data?.message ?? '新增失敗', 'error')
  }
}

const showMsg = (text, type) => {
  msg.value = { text, type }
  setTimeout(() => msg.value = { text: '', type: '' }, 3000)
}

const fmt = (val) =>
  Number(val).toLocaleString('zh-TW', { minimumFractionDigits: 2 })
</script>

<template>
  <div>
    <h1 class="page-title">➕ 新增喜好金融商品</h1>

    <div v-if="msg.text" :class="['alert', msg.type === 'success' ? 'alert-success' : 'alert-error']">
      {{ msg.text }}
    </div>

    <div class="card" style="max-width:560px;">
      <div class="user-block">
        <label>使用者 UserID</label>
        <input v-model="userId" @input="syncUserId" placeholder="ex: A1234567890" />
      </div>

      <div class="form-group">
        <label>選擇金融商品</label>
        <select v-model="form.productNo">
          <option :value="null" disabled>-- 請選擇 --</option>
          <option v-for="p in products" :key="p.no" :value="p.no">
            {{ p.productName }} (單價: {{ fmt(p.price) }}，費率: {{ (p.feeRate*100).toFixed(4) }}%)
          </option>
        </select>
      </div>

      <div class="form-group">
        <label>購買數量</label>
        <input v-model.number="form.purchaseQuantity" type="number" min="1" />
      </div>

      <div class="form-group">
        <label>預計扣款帳號</label>
        <input v-model="form.account" type="text" placeholder="ex: 1111999666" />
      </div>

      <!-- 試算預覽 -->
      <div v-if="selectedProduct" style="background:#f0f4fb;border-radius:8px;padding:1rem;margin-bottom:1.2rem;font-size:.92rem;">
        <div style="color:#6b7a99;margin-bottom:.4rem;">💡 試算預覽（實際金額以後端計算為準）</div>
        <div>產品單價：<strong>{{ fmt(selectedProduct.price) }}</strong></div>
        <div>預估手續費：<strong>{{ previewFee }}</strong></div>
        <div>預計扣款總額：<strong>{{ previewTotal }}</strong></div>
      </div>

      <button class="btn btn-primary" @click="submit" style="width:100%">
        確認新增
      </button>
    </div>
  </div>
</template>
