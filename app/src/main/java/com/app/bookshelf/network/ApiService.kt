package com.app.bookshelf.network

import com.app.bookshelf.model.BooksResponseModel
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("books/")
    suspend fun getBooksData(): Response<BooksResponseModel>
}