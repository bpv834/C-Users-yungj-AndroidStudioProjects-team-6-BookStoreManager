package com.lion.bookstore_manager.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lion.bookstore_manager.MainActivity
import com.lion.bookstore_manager.R
import com.lion.bookstore_manager.book_repository.BookRepository
import com.lion.bookstore_manager.databinding.FragmentInputBinding
import com.lion.bookstore_manager.util.BookType
import com.lion.bookstore_manager.util.FragmentName
import com.lion.bookstore_manager.view_model.BookViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class InputFragment : Fragment() {

    lateinit var fragmentInputBinding: FragmentInputBinding
    lateinit var mainActivity: MainActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentInputBinding = FragmentInputBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        mainActivity = activity as MainActivity

        settingToolbarInputFragment()

        return fragmentInputBinding.root
    }

    fun settingToolbarInputFragment() {
        fragmentInputBinding.apply {
            materialToolbarInputFragment.title = "책 등록화면"
            materialToolbarInputFragment.setNavigationOnClickListener {
                mainActivity.removeFragment(FragmentName.INPUT_FRAGMENT)
            }

            materialToolbarInputFragment.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menuSaveInputFragment -> {
                        inputDone()
                    }
                }
                true

            }
        }
    }

    fun inputDone() {
        fragmentInputBinding.apply {
            // 책 타입 조정
            val bookType: BookType = when (toggleGroupTypeInputFragment.checkedButtonId) {
                R.id.buttonTypePoemAndEssayInputFragment -> BookType.BOOK_TYPE_POEM_AND_ESSAY
                R.id.buttonTypeHumanityInputFragment -> BookType.BOOK_TYPE_HUMANITY
                R.id.buttonNatureInputFragment -> BookType.BOOK_TYPE_NATURE
                else -> BookType.BOOK_TYPE_ETC
            }

            // 제목
            val bookTitle = editTextTitleInputFragment.editText?.text.toString()
            // 저자
            val bookAuthor = editTextAuthorInputFragment.editText?.text.toString()
            // 재고량
            val bookInventory = editTextInventoryInputFragment.editText?.text.toString().toInt()

            val viewModel = BookViewModel(0, bookType, bookTitle, bookAuthor, bookInventory, "")

            // 데이터 저장
            CoroutineScope(Dispatchers.Main).launch {
                // 저장 작업 끝날때까지 대기
                val work1 = async(Dispatchers.IO){
                    // 저장
                    BookRepository.insertBookInfo(mainActivity, viewModel)
                    Log.d("test100", "$viewModel")

                }
                // 리턴없이 삽입이라 조인
                work1.join()
                mainActivity.removeFragment(FragmentName.INPUT_FRAGMENT)
            }
        }
    }

}