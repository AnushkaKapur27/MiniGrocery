package com.example.minigrocery.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO = Data Access Object.
 * All database queries live here.
 * Flow<> means Room will automatically emit updates
 * whenever the cart table changes — perfect for LiveData.
 */
@Dao
interface CartDao {

    // Returns ALL cart rows as a live stream
    // Any insert/update/delete auto-triggers a new emission
    @Query("SELECT * FROM cart_table")
    fun getAllCartItems(): Flow<List<CartEntity>>

    // Get a single item by productId (to check if it's already in cart)
    @Query("SELECT * FROM cart_table WHERE productId = :productId")
    suspend fun getCartItem(productId: Int): CartEntity?

    // INSERT — if same productId exists, replace it (update quantity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(item: CartEntity)

    // DELETE a specific item
    @Delete
    suspend fun deleteItem(item: CartEntity)

    // DELETE everything — used when order is placed
    @Query("DELETE FROM cart_table")
    suspend fun clearCart()

    // Get total item count for the badge on the toolbar
    @Query("SELECT SUM(quantity) FROM cart_table")
    fun getTotalItemCount(): Flow<Int?>
}