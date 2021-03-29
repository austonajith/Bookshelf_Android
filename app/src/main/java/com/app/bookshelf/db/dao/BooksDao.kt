package com.app.bookshelf.db.dao

import androidx.room.*
import com.app.bookshelf.db.dao.entity.BooksEntity

@Dao
interface BooksDao {
    @Insert
    suspend fun insertBooksData(data: List<BooksEntity>)

    @Query("DELETE FROM all_books_data")
    suspend fun deleteBooksData()

    @Query("SELECT * FROM all_books_data")
    suspend fun getBooksDbData(): List<BooksEntity>
}