package com.lion.bookstore_manager.util

enum class FragmentName(var number:Int, var str:String){
    // 첫 화면
    MAIN_FRAGMENT(1, "MainFragment"),
    // 입력 화면
    INPUT_FRAGMENT(2, "InputFragment"),
    // 출력 화면
    SHOW_FRAGMENT(3, "ShowFragment"),
    // 수정 화면
    MODIFY_FRAGMENT(4, "ModifyFragment"),
    // 지도 화면
    LOCATION_FRAGMENT(5,"LocationFragment"),
}

enum class BookType(var number: Int, var str: String){
    // 전체
    BOOK_TYPE_ALL(1, "전체"),
    // 소설
    BOOK_TYPE_FICTION(2, "소설"),
    // 시/에세이
    BOOK_TYPE_POEM_AND_ESSAY(3, "시/에세이"),
    // 인문
    BOOK_TYPE_HUMANITY(4, "인문"),
    // 자연
    BOOK_TYPE_NATURE(5, "자연"),
    // 기타
    BOOK_TYPE_ETC(6, "기타"),

}

