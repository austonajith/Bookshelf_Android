package com.app.bookshelf.db.dao.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "all_books_data")
data class BooksEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var title: String?,
    var isbn: String?,
    var pageCount: String?,
    var publishedDate: String?,
    var longDescription: String?,
    var status: String?,
    var authors: String?,
    var categories: String?,
    var imgUrl: String?,
    var shortDescription: String?
)
