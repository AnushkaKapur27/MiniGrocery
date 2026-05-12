# Mini Grocery Delivery App (Blinkit Style)

A modern Android grocery delivery application built using **Kotlin + XML layouts only** following **MVVM Architecture** and Android best practices.

This project simulates a mini Blinkit-style grocery delivery experience with authentication, product browsing, cart management, checkout flow, and local persistence using Room Database.

---

# 📱 Features

## Authentication
- Mobile number login
- OTP verification
- Fake OTP support (`1234`)
- Input validation
- Proper error handling

## Home Screen
- Search products
- Horizontal category list
- Product listing using RecyclerView
- Add to cart functionality
- Cart badge count
- Filter by category

## Cart Screen
- View cart items
- Increase/decrease quantity
- Remove products
- Bill summary
- Empty cart state
- Persistent cart storage using Room DB

## Checkout Screen
- Delivery address input
- Payment method selection
- Form validation
- Mock order placement

## Order Success Screen
- Random order ID generation
- Delivery estimate
- Order summary
- Continue shopping functionality

---

# 🛠 Tech Stack

## Language
- Kotlin

## UI
- XML Layouts only
- Material Design Components
- RecyclerView
- ConstraintLayout

## Architecture
- MVVM Architecture
- Repository Pattern
- StateFlow / LiveData
- Coroutines

## Local Storage
- Room Database

## Navigation
- Navigation Component

## Additional Libraries
- Glide (image loading)
- ViewBinding
- DiffUtil

---

# 📂 Project Structure

```text
com.example.minigroceryapp
│
├── data
│   ├── local
│   │   ├── AppDatabase.kt
│   │   ├── CartDao.kt
│   │   └── CartEntity.kt
│   │
│   ├── model
│   │   ├── Product.kt
│   │   ├── Category.kt
│   │   ├── CartItem.kt
│   │   └── Order.kt
│   │
│   └── repository
│       ├── AuthRepository.kt
│       ├── ProductRepository.kt
│       └── CartRepository.kt
│
├── ui
│   ├── auth
│   │   ├── LoginFragment.kt
│   │   └── OtpFragment.kt
│   │
│   ├── home
│   │   ├── HomeFragment.kt
│   │   ├── ProductAdapter.kt
│   │   └── CategoryAdapter.kt
│   │
│   ├── cart
│   │   ├── CartFragment.kt
│   │   └── CartAdapter.kt
│   │
│   ├── checkout
│   │   └── CheckoutFragment.kt
│   │
│   └── success
│       └── OrderSuccessFragment.kt
│
├── viewmodel
│   ├── AuthViewModel.kt
│   ├── HomeViewModel.kt
│   ├── CartViewModel.kt
│   └── CheckoutViewModel.kt
│
├── utils
│   ├── Constants.kt
│   ├── Validators.kt
│   ├── UiState.kt
│   └── Extensions.kt
│
├── MainActivity.kt
└── GroceryApplication.kt
```

---

# 🎨 UI Design

## Design Style
- Blinkit-inspired grocery UI
- Clean white backgrounds
- Rounded Material Cards
- Smooth transitions
- Spacious layouts
- Responsive design

## Recommended Color Palette

| Purpose | Color |
|---|---|
| Primary Green | `#0C831F` |
| Dark Green | `#086B19` |
| Accent Yellow | `#FFD54F` |
| Background | `#FFFFFF` |
| Dark Background | `#121212` |
| Text Primary | `#212121` |
| Text Secondary | `#757575` |

---

# 🧱 Architecture Overview

This project follows **MVVM Architecture**.

## Layers

### UI Layer
Contains:
- Activities
- Fragments
- Adapters

Responsible for:
- Rendering UI
- Observing ViewModel state
- Handling user interactions

---

### ViewModel Layer
Responsible for:
- Business logic
- Managing UI state
- Calling repositories
- Exposing StateFlow/LiveData

Example:
```kotlin
class HomeViewModel(
    private val repository: ProductRepository
) : ViewModel()
```

---

### Repository Layer
Acts as a single source of truth.

Responsible for:
- Fetching products
- Managing cart data
- Communicating with Room DB

---

### Data Layer
Contains:
- Room database
- Models
- DAO interfaces

---

# 🗃 Room Database Implementation

## Why Room?
Room is used to persist cart data locally.

This ensures:
- Cart survives app restarts
- Offline support
- Structured local storage

---

## Database Flow

```text
UI
 ↓
ViewModel
 ↓
Repository
 ↓
DAO
 ↓
Room Database
```

---

## Example Cart Flow

1. User clicks "Add to Cart"
2. ViewModel calls Repository
3. Repository inserts item using DAO
4. Room updates database
5. Flow emits updated cart list
6. UI automatically refreshes

---

# 🔄 Screen Navigation Flow

```text
Login Screen
      ↓
OTP Verification
      ↓
Home Screen
      ↓
Cart Screen
      ↓
Checkout Screen
      ↓
Order Success Screen
```

---

# ✅ Validation Rules

## Mobile Number
- Must be 10 digits
- Digits only

## OTP
- Must match `1234`

## Checkout
- Address cannot be empty
- Payment method must be selected

---

# 🚀 Setup Instructions

## Clone Project
```bash
git clone <repo-url>
```

## Open in Android Studio
- Android Studio Hedgehog or newer recommended

## Sync Gradle
- Allow Gradle sync to complete

## Run App
- Connect emulator/device
- Click Run

---

# 🎯 Android Concepts Used

- MVVM Architecture
- RecyclerView
- Room Database
- Navigation Component
- Coroutines
- ViewBinding
- Material Design
- StateFlow / LiveData
