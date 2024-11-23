package com.lion.bookstore_manager.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.lion.bookstore_manager.vo.BookVO

@Dao
interface BookDAO {
    // 도서 저장 하는 메서드
    @Insert
    fun insertBookData(bookVO: BookVO)

    // 도서 정보를 가져오는 메서드
    @Query("""
        select * from BookTable 
        order by bookIdx desc""")
    fun selectBookDataAll() : List<BookVO>

    // 도서 한권 정보를 가져오는 메서드
    @Query("""
        select * from BookTable
        where bookIdx = :bookIdx
    """)
    fun selectBookDataByBookIdx(bookIdx:Int) : BookVO

    // 도서 정보를 삭제하는 메서드
    @Delete
    fun deleteBookData(bookVO: BookVO)

    // 동물 정보를 수정하는 메서드
    @Update
    fun updateBookData(bookVO: BookVO)

}