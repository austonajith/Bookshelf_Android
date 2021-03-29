package com.app.bookshelf.model

import java.io.Serializable


data class BooksListModel
(var name: String?,
 var author: String?,
 var publishedDate: String?,
 var pageCount: String?,
 var imgUrl: String?,
 var category: String?,
 var isbn: String?,
 var shortDescription: String?,
 var longDescription: String?,
 var status: String?,
):Serializable
