package com.app.bookshelf.db.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.bookshelf.db.dao.entity.BooksEntity
import com.app.bookshelf.db.dao.entity.FavoriteBooksEntity
import com.app.bookshelf.db.dao.entity.MyCategoryEntity

@Database(entities = [
    BooksEntity::class,
    MyCategoryEntity::class,
    FavoriteBooksEntity::class
], version = 1)
abstract class BooksDatabase : RoomDatabase() {

    abstract fun booksDao(): BooksDao
    abstract fun categoryDao(): CategoryDao
    abstract fun favBooksDoa(): FavBooksDao

    companion object {
        private const val DATABASE_NAME = "bookshelf"

        @Volatile
        private var INSTANCE: BooksDatabase? = null

        fun getInstance(context: Context): BooksDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context): BooksDatabase {
            val db = Room.databaseBuilder(
                context.applicationContext,
                BooksDatabase::class.java,
                DATABASE_NAME
            )
            return db.build()
        }
    }
}