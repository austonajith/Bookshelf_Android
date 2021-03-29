package com.app.bookshelf.ui.listener

import com.app.bookshelf.model.BooksListModel

interface OnBookClickListener {
    fun onBookClicked(model: BooksListModel?)
}