package com.app.bookshelf.db.dao.entity

import androidx.room.Entity
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey

@Entity(tableName = "fav_books")
data class FavoriteBooksEntity(
        @PrimaryKey
        var isbn: String,
        var title: String?,
        var pageCount: String?,
        var publishedDate: String?,
        var longDescription: String?,
        var status: String?,
        var authors: String?,
        var categories: String?,
        var imgUrl: String?,
        var shortDescription: String?
)
