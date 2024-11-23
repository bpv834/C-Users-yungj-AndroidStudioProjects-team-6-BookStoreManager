package com.lion.bookstore_manager.view_model

import com.lion.bookstore_manager.util.BookType

data class BookViewModel(
    var bookIdx : Int,
    var bookType : BookType,
    var bookName : String,
    var bookAuthor : String,
    var bookInventory : Int,
    val bookImagePath: String,
)