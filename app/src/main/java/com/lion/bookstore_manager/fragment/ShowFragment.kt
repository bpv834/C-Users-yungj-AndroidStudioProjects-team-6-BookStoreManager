package com.lion.bookstore_manager.fragment

import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lion.bookstore_manager.MainActivity
import com.lion.bookstore_manager.R
import com.lion.bookstore_manager.book_repository.BookRepository
import com.lion.bookstore_manager.databinding.FragmentShowBinding
import com.lion.bookstore_manager.util.FragmentName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File

class ShowFragment : Fragment() {

    lateinit var fragmentShowBinding: FragmentShowBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        fragmentShowBinding = FragmentShowBinding.inflate(inflater, container, false)

        settingView()
        settingToolbar()
        return fragmentShowBinding.root
    }

    fun settingView() {
        fragmentShowBinding.apply {
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO){
                    // idx 가져오기
                    val bookIdx = arguments?.getInt("bookIdx")
                    // 도서 데이터 가져온다.
                    BookRepository.selectBookByAnimalIdx(mainActivity, bookIdx!!)

                }
                val bookModel = work1.await()

                editTextTitle.editText?.setText(bookModel.bookName)
                editTextCategory.editText?.setText(bookModel.bookType.str)
                editTextAuthor.editText?.setText(bookModel.bookAuthor)
                editTextInventory.editText?.setText("${bookModel.bookInventory}")
                // Log.d("test300",bookModel.bookImagePath)
                if(bookModel.bookImagePath ==""){
                    // 이미지가 null인경우 기본이미지 설정
                    imageView.setImageResource(R.drawable.ic_launcher_foreground)
                } else{
                    val contentUri = Uri.parse(bookModel.bookImagePath)
                    // ImageView에 URI를 설정
                    imageView.setImageURI(contentUri)
                }
            }
        }
    }

    fun settingToolbar() {
        fragmentShowBinding.apply {
            // 뒤로가기
            materialToolbarInputFragment.setNavigationOnClickListener {
                mainActivity.removeFragment(FragmentName.SHOW_FRAGMENT)
            }

            // 메뉴 설정
            materialToolbarInputFragment.setOnMenuItemClickListener {

                when (it.itemId) {
                    // 삭제메뉴
                    R.id.menuDeleteBookShowFragment->{
                        val builder = MaterialAlertDialogBuilder(mainActivity)
                        builder.setTitle("도서 정보 삭제")
                        builder.setMessage("도서 정보 삭제시 복구 불가")
                        builder.setNegativeButton("취소",null)
                        builder.setPositiveButton("삭제"){ dialogInterface: DialogInterface, i: Int ->
                            // 삭제 메서드 호출
                            deleteBookData()
                        }
                        builder.show()
                    }
                    // 수정메뉴
                    else->{
                        val bookIdx = arguments?.getInt("bookIdx")
                        val dataBundle = Bundle()
                        dataBundle.putInt("bookIdx", bookIdx!!)

                        mainActivity.replaceFragment(FragmentName.MODIFY_FRAGMENT,true,true,dataBundle)
                    }
                }

                true
            }
        }
    }

    // 도서 삭제 메서드
    fun deleteBookData() {
        CoroutineScope(Dispatchers.Main).launch {
            val work1 =async(Dispatchers.IO){
                // 삭제한다.
                val bookIdx = arguments?.getInt("bookIdx")!!
                BookRepository.deleteBookInfo(mainActivity, bookIdx)
            }
            work1.join()
            // 학생 목록을 보는 화면으로 돌아간다.
            mainActivity.removeFragment(FragmentName.SHOW_FRAGMENT)
        }
    }

}