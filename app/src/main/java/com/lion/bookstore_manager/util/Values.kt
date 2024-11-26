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
    // 문학
    BOOK_TYPE_Literature(2, "문학"),
    // 인문
    BOOK_TYPE_HUMANITY(3, "인문"),
    // 자연
    BOOK_TYPE_NATURE(4, "자연"),
    // 기타
    BOOK_TYPE_ETC(5, "기타"),

}

