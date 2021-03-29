package com.app.bookshelf.network

import com.app.bookshelf.model.BooksResponseModel
import retrofit2.Response

interface ApiHelper {
    suspend fun getBooksData(): Response<BooksResponseModel>
}