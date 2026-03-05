# 金融商品喜好紀錄系統 — 系統規格書

> **文件版本**：v2.0 (H2 資料庫版)  
> **最後更新**：2026-03-05  
> **專案路徑**：`d:\project\FinancialProductPreferenceRecording`

---

## 一、系統概述

本系統為一套金融商品喜好紀錄平台，使用者可透過 Web 介面對個人喜好的金融商品進行新增、查詢、修改與刪除（CRUD）操作，並記錄預計購買數量、扣款帳號及預估費用。本專案採 H2 記憶體資料庫架構，支援「開箱即用」無需額外安裝外部資料庫軟體。

---

## 二、系統架構

採用**三層式架構（Three-Tier Architecture）**加上**記憶體資料庫**：

```
┌───────────────────────────────────┐
│  展示層 (Presentation)            │  Vue.js 3 + Vite (port 5173)
├───────────────────────────────────┤
│  業務層 (Business Logic)          │  Spring Boot 3 (port 8080)
│   ├─ Controller 層 (RESTful API)  │
│   ├─ Service 層 (商業邏輯計算)    │
│   └─ Repository 層 (JPA 查詢)     │
├───────────────────────────────────┤
│  資料層 (Data)                    │  H2 In-Memory Database
└───────────────────────────────────┘
```

---

## 三、技術規格

| 類別 | 技術 |
|------|------|
| 前端框架 | Vue.js 3 (Composition API) |
| 前端建置工具 | Vite 5 |
| HTTP Client | Axios 1.x |
| 路由 | Vue Router 4 |
| 後端框架 | Spring Boot 3.2 |
| ORM | Spring Data JPA / Hibernate |
| 建置工具 | Maven |
| 資料庫 | H2 In-Memory Database |
| 資料庫存取 | JPA 自定義 JPQL 查詢與 CRUD 操作 |
| 安全防護 | XSS Filter (防 XSS)、JPA Parameter Binding (防 SQLi) |
| API 風格 | RESTful |

---

## 四、資料模型

### 4.1 Users（使用者）

| 欄位 | 型態 | 說明 |
|------|------|------|
| UserID | VARCHAR(20) PK | 使用者 ID |
| UserName | VARCHAR(50) | 使用者名稱 |
| Email | VARCHAR(100) | 使用者電子郵件 |
| Account | VARCHAR(50) | 扣款帳號 |

### 4.2 Products（產品）

| 欄位 | 型態 | 說明 |
|------|------|------|
| No | INT PK AUTO_INCREMENT | 產品流水號 |
| ProductName | VARCHAR(100) | 產品名稱 |
| Price | DECIMAL(18,2) | 產品價格 |
| FeeRate | DECIMAL(8,6) | 手續費率（0.001425 = 0.1425%） |

### 4.3 LikeList（喜好清單）

| 欄位 | 型態 | 說明 |
|------|------|------|
| SN | INT PK AUTO_INCREMENT | 流水序號 |
| UserID | VARCHAR(20) FK | 使用者 ID |
| ProductNo | INT FK | 產品流水號 |
| PurchaseQuantity | INT | 購買數量 |
| Account | VARCHAR(50) | 扣款帳號 |
| TotalFee | DECIMAL(18,2) | 總手續費（台幣，Java 端計算） |
| TotalAmount | DECIMAL(18,2) | 預計扣款總金額（Java 端計算） |

> **計算公式**（由 Java Service 端執行）：
> - `TotalFee = Price × PurchaseQuantity × FeeRate` (四捨五入至小數第二位)
> - `TotalAmount = Price × PurchaseQuantity + TotalFee` (四捨五入至小數第二位)

---

## 五、RESTful API 規格

Base URL：`http://localhost:8080/api`

| 方法 | 路徑 | 說明 | Request Body / Params |
|------|------|------|----------------------|
| `POST` | `/favorites` | 新增喜好商品 | JSON: `{ userId, productNo, purchaseQuantity, account }` |
| `GET` | `/favorites?userId=xxx` | 查詢所有喜好清單 | Query param: `userId` |
| `DELETE` | `/favorites/{sn}?userId=xxx` | 刪除指定喜好商品 | Path: `sn`，Query: `userId` |
| `PUT` | `/favorites/{sn}` | 更改指定喜好商品 | Path: `sn`，JSON: `{ userId, productNo, purchaseQuantity, account }` |
| `GET` | `/products` | 取得所有產品清單 | 無 |

### 統一回應格式

```json
{
  "success": true,
  "message": "Success",
  "data": { ... }
}
```

---

## 六、常見功能模組差異（與舊版相異）

1. **去除 Stored Procedure**：不再於資料庫底層綁定預存程序運算。所有檢查（如使用者是否存在、產品金額等）以及運算邏輯全都移至 `LikeListService.java` 中進行。
2. **自動建表與塞入測資**：啟動 Spring Boot 時會自動讀取 `src/main/resources/schema.sql` 建立 H2 資料表結構，並執行 `src/main/resources/data.sql` 灌入初始測資。

---

## 七、專案目錄結構

```
FinancialProductPreferenceRecording/
├── backend/
│   ├── pom.xml                      ← 移除 MySQL，加入 h2
│   └── src/main/
│       ├── java/com/finance/preference/
│       │   ├── FinancialPreferenceApplication.java
│       │   ├── config/              ← CORS + XSS
│       │   ├── controller/          ← RESTful Controller
│       │   ├── dto/                 
│       │   ├── entity/              ← JPA Entity 定義
│       │   ├── exception/
│       │   ├── repository/          ← LikeListRepository / UserRepository
│       │   └── service/             ← LikeListService (Java商業邏輯實作)
│       └── resources/
│           ├── application.properties ← 指向 JDBC H2 Mem
│           ├── schema.sql           ← H2 資料表定義 (DDL)
│           └── data.sql             ← H2 測試資料寫入 (DML)
└── frontend/
    ├── package.json
    ├── vite.config.js               ← Proxy /api → :8080
    └── src/                         ← Vue.js (組成元件、路由與 API)
```

---

## 八、本機啟動步驟（開箱即用版）

### 1. 後端 (Spring Boot + H2)

```bash
cd backend
mvn spring-boot:run
```
> 後端啟動後，H2 資料庫即自動載入並運行於記憶體中，您可以直接前往 `http://localhost:8080/h2-console` 觀看資料庫內容。
> * **JDBC URL**: `jdbc:h2:mem:finance_db`
> * **User Name**: `sa`
> * **Password**: (留空)

### 2. 前端 (Vue 3 + Vite)

```bash
cd frontend
npm install
npm run dev
# 開啟瀏覽器：http://localhost:5173
```

---

## 九、提交與交接說明

直接將整個專案資料夾打包或上傳至 GitHub 交接給任何人即可。
接收者**完全不需要安裝 MySQL** 或執行另外的 SQL，只需確保有 Java 與 Node.js 環境，下達 `mvn spring-boot:run` 即可直接進行展示及測試（Zero Configuration）。
