package com.example.minigrocery.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * The single Room database instance for the whole app.
 * Singleton pattern — only ONE instance ever exists.
 *
 * @Database lists all Entity classes and sets a version number.
 * If you change any Entity, increment version and add a migration.
 */
@Database(
    entities = [CartEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // Room auto-generates the implementation of this
    abstract fun cartDao(): CartDao

    companion object {
        // @Volatile means every thread sees the latest value immediately
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // If instance exists, return it. Otherwise create it.
            // synchronized() ensures only ONE thread creates it at a time.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mini_grocery_database"
                )
                    .fallbackToDestructiveMigration() // dev only — wipes DB on version change
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}