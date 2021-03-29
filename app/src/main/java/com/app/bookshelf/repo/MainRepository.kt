package com.app.bookshelf.repo

import android.service.voice.AlwaysOnHotwordDetector
import com.app.bookshelf.db.dao.BooksDatabase
import com.app.bookshelf.db.dao.entity.BooksEntity
import com.app.bookshelf.db.dao.entity.FavoriteBooksEntity
import com.app.bookshelf.db.dao.entity.MyCategoryEntity
import com.app.bookshelf.model.BooksResponseModel
import com.app.bookshelf.network.ApiHelper
import com.app.bookshelf.network.RetrofitBuilder

object MainRepository {

    var apiService = RetrofitBuilder.apiService

    suspend fun getBooksData() = apiService.getBooksData()

    suspend fun storeBookData(payload: ArrayList<BooksEntity>, database: BooksDatabase) {
        database.booksDao().deleteBooksData()
        database.booksDao().insertBooksData(payload)
    }

    suspend fun getBooksDbData(database: BooksDatabase): List<BooksEntity> {
        return database.booksDao().getBooksDbData()
    }

    suspend fun insertMyCategory(payload: ArrayList<MyCategoryEntity>, database: BooksDatabase) {
        return database.categoryDao().insertCategoryData(payload)
    }

    suspend fun deleteMyCategory(database: BooksDatabase) {
        return database.categoryDao().deleteCategoryData()
    }

    suspend fun getMyCategory(database: BooksDatabase): List<MyCategoryEntity> {
        return database.categoryDao().getCategoryData()
    }

    suspend fun insertFavoriteBooks(payload: FavoriteBooksEntity, database: BooksDatabase) {
        return database.favBooksDoa().insertFavoriteBooksData(payload)
    }

    suspend fun getFavoriteBooks(isbn: String, database: BooksDatabase): FavoriteBooksEntity {
        return database.favBooksDoa().getFavBooksDbData(isbn)
    }

    suspend fun getAllFavoriteBooks(database: BooksDatabase): List<FavoriteBooksEntity> {
        return database.favBooksDoa().getAllFavBooksDbData()
    }

    suspend fun deleteFavBookWithId(isbn:String, database: BooksDatabase) {
        return database.favBooksDoa().deleteFavBooksWithId(isbn)
    }

}