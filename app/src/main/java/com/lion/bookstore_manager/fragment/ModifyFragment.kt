package com.lion.bookstore_manager.fragment

import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lion.bookstore_manager.MainActivity
import com.lion.bookstore_manager.R
import com.lion.bookstore_manager.book_repository.BookRepository
import com.lion.bookstore_manager.databinding.FragmentModifyBinding
import com.lion.bookstore_manager.util.BookType
import com.lion.bookstore_manager.util.FragmentName
import com.lion.bookstore_manager.view_model.BookViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class ModifyFragment : Fragment() {

    lateinit var mainActivity : MainActivity
    lateinit var fragmentModifyBinding: FragmentModifyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        fragmentModifyBinding = FragmentModifyBinding.inflate(inflater)
        settingToolbar()
        settingView()
        return fragmentModifyBinding.root
    }

    fun settingToolbar() {
        fragmentModifyBinding.apply {
            toolbarModifyFragment.setNavigationOnClickListener {
                mainActivity.removeFragment(FragmentName.MODIFY_FRAGMENT)
            }
            toolbarModifyFragment.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menuFixModifyFragment->{
                        showDialogModify()
                    }
                }
                true
            }
        }
    }

    fun settingView() {
        fragmentModifyBinding.apply {
            // 데이터 추출
            val bookIdx = arguments?.getInt("bookIdx")
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO){
                    BookRepository.selectBookByAnimalIdx(mainActivity, bookIdx!!)
                }
                val bookModel = work1.await()

                val bookType = bookModel.bookType

                val bookName = bookModel.bookName
                val bookAuthor = bookModel.bookAuthor
                val bookInventory = bookModel.bookInventory
                val bookImagePath = bookModel.bookImagePath

                when(bookType){
                    BookType.BOOK_TYPE_ALL -> {}
                    BookType.BOOK_TYPE_Literature -> {toggleGroupTypeModifyFragment.check(R.id.buttonTypeLiteratureModifyFragment)}
                    BookType.BOOK_TYPE_HUMANITY -> {toggleGroupTypeModifyFragment.check(R.id.buttonTypeHumanityModifyFragment)}
                    BookType.BOOK_TYPE_NATURE -> {toggleGroupTypeModifyFragment.check(R.id.buttonTypeNatureModifyFragment)}
                    BookType.BOOK_TYPE_ETC -> {toggleGroupTypeModifyFragment.check(R.id.buttonTypeEtcModifyFragment)}
                }
                editTextTitleModifyFragment.editText?.setText(bookName)
                editTextAuthorModifyFragment.editText?.setText(bookAuthor)
                editTextInventoryModifyFragment.editText?.setText("$bookInventory")

                if(bookImagePath ==""){
                    // 이미지가 null인경우 기본이미지 설정
                    imageViewModifyFragment.setImageResource(R.drawable.ic_launcher_foreground)
                } else{
                    val contentUri = Uri.parse(bookModel.bookImagePath)
                    // ImageView에 URI를 설정
                    imageViewModifyFragment.setImageURI(contentUri)
                }


            }
        }
    }

    fun showDialogModify() {
        // 다이얼로그를 띄운다.
        val builder = MaterialAlertDialogBuilder(mainActivity)
        builder.setTitle("도서 정보 수정")
        builder.setMessage("도서 정보 수정시 복구가 불가능합니다")
        builder.setNegativeButton("취소", null)
        builder.setPositiveButton("수정"){ dialogInterface: DialogInterface, i: Int ->
            processingModifyBookData()
        }
        builder.show()
    }

    fun processingModifyBookData() {
        fragmentModifyBinding.apply {
            val bookIdx = arguments?.getInt("bookIdx")
            val bookType = when (toggleGroupTypeModifyFragment.checkedButtonId) {
                R.id.buttonTypeLiteratureModifyFragment-> BookType.BOOK_TYPE_Literature
                R.id.buttonTypeHumanityModifyFragment->BookType.BOOK_TYPE_HUMANITY
                R.id.buttonTypeNatureModifyFragment->BookType.BOOK_TYPE_NATURE
                else -> BookType.BOOK_TYPE_ETC
            }
            val bookName = editTextTitleModifyFragment.editText?.text.toString()
            val bookAuthor = editTextAuthorModifyFragment.editText?.text.toString()
            val bookInventory = editTextInventoryModifyFragment.editText?.text.toString().toInt()
            val bookImagePath = ""

            val bookViewModel = BookViewModel(bookIdx!!, bookType, bookName, bookAuthor, bookInventory, bookImagePath)

            // 수정 처리 메서드 호출
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO){
                    BookRepository.updateBookInfo(mainActivity, bookViewModel)
                }
                work1.join()
                // 이전 화면으로 돌아간다.
                mainActivity.removeFragment(FragmentName.MODIFY_FRAGMENT)
            }

        }
    }


}