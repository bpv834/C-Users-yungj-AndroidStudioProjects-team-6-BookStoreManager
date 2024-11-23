package com.lion.bookstore_manager.vo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "BookTable")
data class BookVO(
    @PrimaryKey(autoGenerate = true)
    var bookIdx: Int=10,
    var bookType: Int = 0,
    var bookName: String = "",
    var bookAuthor: String = "",
    var bookInventory: Int = 0,
    val bookImagePath: String = "",

    )