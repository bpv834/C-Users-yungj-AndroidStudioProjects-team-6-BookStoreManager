package com.lion.bookstore_manager.book_repository

import android.content.Context
import android.util.Log
import com.lion.bookstore_manager.dao.BookDatabase
import com.lion.bookstore_manager.util.BookType
import com.lion.bookstore_manager.view_model.BookViewModel
import com.lion.bookstore_manager.vo.BookVO

class BookRepository {

    companion object {

        // 도서 정보를 저장하는 메서드
        fun insertBookInfo(context: Context, bookViewModel: BookViewModel) {
            // 데이터베이스 객체를 가져온다.
            val bookDatabase = BookDatabase.getInstance(context)

            // ViewModel에 있는 데이터를 VO에 담아준다.
            val bookIdx = bookViewModel.bookIdx
            val bookType = bookViewModel.bookType.number
            val bookName = bookViewModel.bookName
            val bookAuthor = bookViewModel.bookAuthor
            val bookInventory = bookViewModel.bookInventory
            val bookImgUrl = bookViewModel.bookImagePath
            val bookVO = BookVO(
                bookIdx = bookIdx,
                bookType = bookType,
                bookName = bookName,
                bookAuthor = bookAuthor,
                bookInventory = bookInventory,
                bookImagePath = bookImgUrl,
            )
            Log.d("test200", "${bookVO}")

            bookDatabase?.bookDAO()?.insertBookData(bookVO)
        }

        // 도서 정보 전체를 가져오는 메서드
        fun selectBookInfoAll(context: Context): MutableList<BookViewModel> {
            // 데이터 베이스 객체
            val bookDatabase = BookDatabase.getInstance(context)
            // 도서 데이터 전체를 가져온다
            val bookVoList = bookDatabase?.bookDAO()?.selectBookDataAll()
            // 도서 데이터를 담을 리스트
            val bookViewModelList = mutableListOf<BookViewModel>()
            // 도서 수 만큼 반복한다.
            bookVoList?.forEach {
                // 도서 데이터를 추출한다.
                val bookIdx = it.bookIdx
                val bookType = when (it.bookType) {
                    BookType.BOOK_TYPE_ALL.number -> BookType.BOOK_TYPE_ALL
                    BookType.BOOK_TYPE_Literature.number -> BookType.BOOK_TYPE_Literature
                    BookType.BOOK_TYPE_HUMANITY.number -> BookType.BOOK_TYPE_HUMANITY
                    BookType.BOOK_TYPE_NATURE.number -> BookType.BOOK_TYPE_NATURE
                    else -> {
                        BookType.BOOK_TYPE_ETC
                    }
                }
                val bookName = it.bookName
                val bookAuthor = it.bookAuthor
                val bookInventory = it.bookInventory
                val bookImagePath = it.bookImagePath


                // 객체에 담는다.
                val bookViewModel = BookViewModel(
                    bookIdx,
                    bookType,
                    bookName,
                    bookAuthor,
                    bookInventory,
                    bookImagePath
                )
                // 리스트에 담는다.
                bookViewModelList.add(bookViewModel)
            }
            return bookViewModelList
        }


        // 도서 정보 전체를 가져오는 메서드
        fun selectBookInfoByType(context: Context, type : Int): MutableList<BookViewModel> {
            // 데이터 베이스 객체
            val bookDatabase = BookDatabase.getInstance(context)
            // 도서 데이터 전체를 가져온다
            val bookVoList = bookDatabase?.bookDAO()?.selectBookDataByType(type)
            // 도서 데이터를 담을 리스트
            val filteredBookViewModelList = mutableListOf<BookViewModel>()
            // 도서 수 만큼 반복한다.
            bookVoList?.forEach {
                // 도서 데이터를 추출한다.
                val bookIdx = it.bookIdx
                val bookType = when (it.bookType) {
                    BookType.BOOK_TYPE_ALL.number -> BookType.BOOK_TYPE_ALL
                    BookType.BOOK_TYPE_Literature.number -> BookType.BOOK_TYPE_Literature
                    BookType.BOOK_TYPE_HUMANITY.number -> BookType.BOOK_TYPE_HUMANITY
                    BookType.BOOK_TYPE_NATURE.number -> BookType.BOOK_TYPE_NATURE
                    else -> {
                        BookType.BOOK_TYPE_ETC
                    }
                }
                val bookName = it.bookName
                val bookAuthor = it.bookAuthor
                val bookInventory = it.bookInventory
                val bookImagePath = it.bookImagePath


                // 객체에 담는다.
                val bookViewModel = BookViewModel(
                    bookIdx,
                    bookType,
                    bookName,
                    bookAuthor,
                    bookInventory,
                    bookImagePath
                )
                // 필터된 리스트에 담는다.
                filteredBookViewModelList.add(bookViewModel)
            }
            return filteredBookViewModelList
        }





        // 도서 한개의 정보를 가져온다.
        fun selectBookByAnimalIdx(context: Context, bookIdx: Int): BookViewModel {
            val bookDatabase = BookDatabase.getInstance(context)
            // 도서 한개 정보를 가져온다.
            val bookVo = bookDatabase?.bookDAO()?.selectBookDataByBookIdx(bookIdx)
            // 도서 객체에 담는다.

            val bookType = when (bookVo?.bookType) {
                BookType.BOOK_TYPE_ALL.number -> BookType.BOOK_TYPE_ALL
                BookType.BOOK_TYPE_Literature.number -> BookType.BOOK_TYPE_Literature
                BookType.BOOK_TYPE_HUMANITY.number -> BookType.BOOK_TYPE_HUMANITY
                BookType.BOOK_TYPE_NATURE.number -> BookType.BOOK_TYPE_NATURE
                else -> BookType.BOOK_TYPE_ETC
            }
            val bookName = bookVo?.bookName
            val bookAuthor = bookVo?.bookAuthor
            val bookInventory = bookVo?.bookInventory
            val bookImagePath = bookVo?.bookImagePath

            val bookViewModel = BookViewModel(
                bookIdx,
                bookType,
                bookName!!,
                bookAuthor!!,
                bookInventory!!,
                bookImagePath!!
            )
            return bookViewModel
        }

        // 도서 정보를 수정하는 메서드
        fun updateBookInfo(context: Context, bookViewModel: BookViewModel) {
            val bookDatabase = BookDatabase.getInstance(context)
            // VO에 객체에 담아준다
            val bookIdx = bookViewModel.bookIdx
            val bookType = bookViewModel.bookType.number
            val bookName = bookViewModel.bookName
            val bookAuthor = bookViewModel.bookAuthor
            val bookInventory = bookViewModel.bookInventory
            val bookImagePath = bookViewModel.bookImagePath

            val bookVO = BookVO(
                bookIdx,
                bookType,
                bookName,
                bookAuthor,
                bookInventory,
                bookImagePath
            )

            // 수정한다.
            bookDatabase?.bookDAO()?.updateBookData(bookVO)
        }

        // 도서 삭제 메서드
        fun deleteBookInfo(context: Context, bookIdx: Int) {
            val bookDatabase = BookDatabase.getInstance(context)
            // 삭제한다.
            bookDatabase?.bookDAO()?.deleteBookData(bookIdx)
        }




    }
}