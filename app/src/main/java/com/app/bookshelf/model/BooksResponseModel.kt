package com.app.bookshelf.model

class BooksResponseModel : ArrayList<BooksResponseModel.BooksResponseModelItem>(){
    data class BooksResponseModelItem(
        var title: String?,
        var isbn: String?,
        var pageCount: Int?,
        var publishedDate: PublishedDate?,
        var thumbnailUrl: String?,
        var shortDescription: String?,
        var longDescription: String?,
        var status: String?,
        var authors: List<String?>?,
        var categories: List<String?>?
    ) {
        data class PublishedDate(
            var `$date`: String?
        )
    }
}