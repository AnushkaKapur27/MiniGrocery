# Mini Grocery Delivery App (Blinkit Style)

A modern Android grocery delivery application built using **Kotlin + XML layouts only**,
following **MVVM Architecture** and Android best practices.

Simulates a mini Blinkit-style grocery delivery experience with authentication, product
browsing, cart management, checkout flow, and local persistence using Room Database.

---

## Features

### Authentication
- Mobile number login with validation (10 digits, numeric only)
- OTP verification screen
- Fake OTP: `1234`
- Proper error messages for invalid input
- Simulated network delay for realism

### Home Screen
- Horizontal category list (All, Fruits, Vegetables, Dairy, Snacks, Beverages)
- Product listing using RecyclerView
- Search products by name or description
- Filter by category
- ADD / ✓ ADDED button state on product cards
- Cart badge showing item count

### Cart Screen
- View all cart items with emoji, name, unit, and price
- Increase / decrease quantity
- Item automatically removed when quantity reaches 0
- Bill summary: subtotal, delivery fee, total
- Free delivery above ₹500
- Empty cart state UI
- Cart persisted using Room Database (survives app restarts)

### Checkout Screen
- Delivery address input (name, address, city)
- Payment method selection: Cash on Delivery or Online Payment (mock)
- Form validation with proper error messages
- Random order ID generation

### Order Success Screen
- Displays generated order ID (e.g. QB-48291)
- Shows amount paid, payment method, delivery address
- Estimated delivery time
- Continue Shopping button

---

## Tech Stack

| Category | Technology |
|---|---|
| Language | Kotlin |
| UI | XML Layouts, Material Design Components |
| Architecture | MVVM (Model-View-ViewModel) |
| State management | LiveData |
| Async operations | Kotlin Coroutines |
| Local storage | Room Database |
| Navigation | Navigation Component (Single Activity) |
| View access | ViewBinding (no findViewById) |
| List rendering | RecyclerView + DiffUtil |
| Image loading | Glide (imported, ready for real images) |

---

## Project Structure

```
com.example.minigrocery
│
├── data/
│   ├── local/
│   │   ├── AppDatabase.kt        # Singleton Room DB instance
│   │   ├── CartDao.kt            # All SQL queries as Kotlin functions
│   │   └── CartEntity.kt         # The cart table (productId + quantity)
│   │
│   ├── model/
│   │   ├── Product.kt            # Grocery product data class
│   │   ├── Category.kt           # Category chip data class
│   │   └── CartItem.kt           # UI model: Product + quantity combined
│   │
│   └── repository/
│       ├── ProductRepository.kt  # Wraps SampleData (mock backend)
│       └── CartRepository.kt     # Cart logic, joins Room + SampleData
│
├── ui/
│   ├── auth/
│   │   ├── LoginFragment.kt      # Phone number input screen
│   │   └── OtpFragment.kt        # OTP verification screen
│   │
│   ├── home/
│   │   └── HomeFragment.kt       # Product list + search + categories
│   │
│   ├── cart/
│   │   └── CartFragment.kt       # Cart items + bill summary
│   │
│   ├── checkout/
│   │   └── CheckoutFragment.kt   # Address + payment + place order
│   │
│   └── success/
│       └── SuccessFragment.kt    # Order confirmed screen
│
├── adapter/
│   ├── CategoryAdapter.kt        # Horizontal category RecyclerView
│   ├── ProductAdapter.kt         # Vertical product list RecyclerView
│   └── CartAdapter.kt            # Cart items RecyclerView
│
├── viewmodel/
│   ├── AuthViewModel.kt          # Login + OTP logic and state
│   ├── HomeViewModel.kt          # Product filter + search logic
│   ├── CartViewModel.kt          # Cart operations via Room
│   └── CheckoutViewModel.kt      # Address validation + order ID
│
├── utils/
│   ├── SampleData.kt             # Mock product + category data (fake backend)
│   ├── Constants.kt              # FAKE_OTP, delivery fee, thresholds
│   └── Extensions.kt            # showToast(), show(), hide(), toCurrencyString()
│
└── MainActivity.kt               # Single activity shell, hosts NavHostFragment
```

---

## Architecture Overview

This project follows **MVVM (Model-View-ViewModel)** architecture with a Repository pattern.

```
┌─────────────────────────────────────┐
│           UI Layer                  │
│  Fragments + XML Layouts +          │
│  RecyclerView Adapters              │
│  Observes LiveData, sends events    │
└──────────────┬──────────────────────┘
               │ calls
┌──────────────▼──────────────────────┐
│         ViewModel Layer             │
│  Business logic, validation,        │
│  UI state via LiveData              │
│  Runs DB ops in viewModelScope      │
└──────────────┬──────────────────────┘
               │ calls
┌──────────────▼──────────────────────┐
│        Repository Layer             │
│  Single source of truth             │
│  Combines Room data + mock data     │
│  Abstracts data source from VM      │
└──────┬───────────────────┬──────────┘
       │                   │
┌──────▼──────┐   ┌────────▼─────────┐
│  Room DB    │   │   SampleData.kt  │
│  CartDao    │   │  (Mock backend)  │
│  CartEntity │   │  Products +      │
│  AppDatabase│   │  Categories      │
└─────────────┘   └──────────────────┘
```

---

## Room Database

Room persists the cart locally so items survive app restarts and process death.

### What is stored
Only two columns are stored in the `cart_table`:

| Column | Type | Description |
|---|---|---|
| productId | Int (Primary Key) | Links to a Product in SampleData |
| quantity | Int | How many of this item in cart |

Full product details (name, price, image) are NOT stored in Room.
They are looked up from `SampleData.kt` in `CartRepository` and joined at runtime.

### Why only store productId?
- Keeps the DB schema simple
- Product data never changes (mock data)
- In a real app, product data would come from an API cache

### Cart data flow
```
User taps ADD
     ↓
ProductAdapter callback
     ↓
HomeFragment → cartViewModel.addToCart(product)
     ↓
CartViewModel → viewModelScope.launch { repository.addToCart(product) }
     ↓
CartRepository:
  checks if item exists → inserts or updates quantity
     ↓
Room writes to SQLite
     ↓
CartDao Flow emits new list automatically
     ↓
CartRepository maps CartEntity → CartItem (joins with SampleData)
     ↓
CartViewModel LiveData updates
     ↓
UI redraws only changed items via DiffUtil
```

---

## Navigation Flow

Single Activity architecture. All screens are Fragments managed by Navigation Component.

```
Login Screen
     ↓ (valid phone → OTP sent)
OTP Screen
     ↓ (OTP = 1234 → verified)
Home Screen ←──────────────────┐
     ↓                         │
Cart Screen              (continue shopping)
     ↓                         │
Checkout Screen                │
     ↓                         │
Order Success Screen ──────────┘
```

Back stack is cleared after Login → Home so the user cannot press Back to return to Login.
Same for Success → Home.

---

## Validation Rules

| Field | Rule |
|---|---|
| Mobile number | Exactly 10 digits, numeric only |
| OTP | Must equal `1234` |
| Name (checkout) | Not blank, minimum 3 characters |
| Address (checkout) | Not blank, minimum 5 characters |
| City (checkout) | Not blank |

---

## Color Palette

| Purpose | Color |
|---|---|
| Primary Green | `#0C831F` |
| Dark Green | `#086B19` |
| Light Green (surface) | `#E8F5EA` |
| Accent Yellow | `#F8C200` |
| Accent Orange (badges) | `#F97316` |
| Background | `#F5F5F5` |
| Card background | `#FFFFFF` |
| Text Primary | `#1A1A1A` |
| Text Secondary | `#757575` |
| Error Red | `#D32F2F` |


---

## Setup Instructions

1. Open **Android Studio** (Hedgehog 2023.1.1 or newer recommended)
2. Select **Open** and choose the project folder
3. Wait for Gradle sync to complete
4. Connect an emulator or physical device (API 24+)
5. Click **Run**

### Requirements
- Android Studio Hedgehog or newer
- Android Gradle Plugin 8.7.x
- compileSdk 35
- minSdk 24

---

## Key Android Concepts Demonstrated

| Concept | Where used |
|---|---|
| MVVM Architecture | All screens — VM never references View |
| LiveData | All ViewModels expose LiveData, Fragments observe |
| Repository Pattern | `CartRepository`, `ProductRepository` |
| Room Database | Cart persistence across app restarts |
| Kotlin Coroutines | All Room operations run on background thread |
| Navigation Component | Single activity, fragment transactions handled automatically |
| ViewBinding | All Fragments — no `findViewById` anywhere |
| DiffUtil | All three RecyclerView adapters |
| Sealed Classes | `AuthViewModel.AuthState` models every possible UI state |
| RecyclerView | Category list (horizontal) + Product list + Cart list |
| Material Design | Cards, TextInputLayout, MaterialButton throughout |

