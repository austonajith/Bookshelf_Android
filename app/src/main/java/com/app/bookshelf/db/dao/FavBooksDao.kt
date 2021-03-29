package com.app.bookshelf.db.dao

import androidx.room.*
import com.app.bookshelf.db.dao.entity.BooksEntity
import com.app.bookshelf.db.dao.entity.FavoriteBooksEntity

@Dao
interface FavBooksDao {
    @Insert
    suspend fun insertFavoriteBooksData(data: FavoriteBooksEntity)

    @Query("DELETE FROM fav_books")
    suspend fun deleteAllFavBooksData()

    @Query("DELETE FROM fav_books WHERE isbn = :isbn")
    suspend fun deleteFavBooksWithId( isbn: String)

    @Query("SELECT * FROM fav_books WHERE isbn = :isbn LIMIT 1" )
    suspend fun getFavBooksDbData(isbn: String): FavoriteBooksEntity

    @Query("SELECT * FROM fav_books" )
    suspend fun getAllFavBooksDbData(): List<FavoriteBooksEntity>
}