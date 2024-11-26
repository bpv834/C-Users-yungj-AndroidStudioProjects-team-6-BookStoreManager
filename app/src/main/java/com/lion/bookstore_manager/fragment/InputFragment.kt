package com.lion.bookstore_manager.fragment

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.core.content.FileProvider
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
import java.io.File

class InputFragment : Fragment() {

    lateinit var fragmentInputBinding: FragmentInputBinding
    lateinit var mainActivity: MainActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentInputBinding = FragmentInputBinding.inflate(inflater, container, false)
        mainActivity = activity as MainActivity

        // 원본 사진 찍기용 런처 생성 메서드 호출
        createOriginalCameraLauncher()

        settingToolbarInputFragment()

        settingImageButtons()

        return fragmentInputBinding.root
    }

    fun settingToolbarInputFragment() {
        fragmentInputBinding.apply {
            materialToolbarInputFragment.title = "책 등록화면"
            materialToolbarInputFragment.setNavigationOnClickListener {
                mainActivity.contentUri= null
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

    // MainActivity에 있는 런처 초기화
    // 원본 사진찍기용 런처 생성
    fun createOriginalCameraLauncher(){
        mainActivity.originalCameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){

            // 사진을 선택하고 돌아왔다면
            if(it.resultCode == RESULT_OK && mainActivity.contentUri!=null){
                // uri를 통해 저장된 사진 데이터를 가져온다.
                val bitmap = BitmapFactory.decodeFile(mainActivity.contentUri!!.path)
                // 이미지의 회전 각도값을 가져온다.
                val degree = mainActivity.getDegree(mainActivity.contentUri!!)
                // 회전 값을 이용해 이미지를 회전시킨다.
                val rotateBitmap = mainActivity.rotateBitmap(bitmap, degree)
                // 크기를 조정한 이미지를 가져온다.
                val resizeBitmap = mainActivity.resizeBitmap(1024, rotateBitmap)

                // 이미지 뷰에 설정해준다.
                fragmentInputBinding.imageViewInputFragment.setImageBitmap(resizeBitmap)

              /*  // 사진 파일은 삭제한다.
                val file = File(mainActivity.contentUri.path!!)
                file.delete()*/
            }
        }
    }

    fun settingImageButtons() {
        fragmentInputBinding.apply {
            // 사진찍어 올리기
            buttonTakePictureInputFragment.setOnClickListener {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                // 촬영한 사진이 저장될 파일 이름
                val fileName = "/temp_${System.currentTimeMillis()}.jpg"
                // 경로 + 파일이름
                val picPath = "${mainActivity.filePath}${fileName}"
                val file = File(picPath)

                // 사진이 저장될 위치를 관리하는 Uri 객체를 생성
                mainActivity.contentUri = FileProvider.getUriForFile(mainActivity, "com.lion.getpictures.file_provider", file)

                // Activity를 실행한다.

                //MediaStore.EXTRA_OUTPUT: 촬영된 사진이 저장될 위치를 지정하는 데 사용됩니다.
                //contentUri: FileProvider를 통해 생성된 URI로, 안전하고 명확한 저장 경로를 제공합니다.
                //이 코드를 사용하면 앱이 사진을 직접 관리할 수 있어, 더 안정적이고 효율적인 파일 처리가 가능합니다.
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mainActivity.contentUri)
                // 초반에 초기화 한 런처를 실행시킨다.
                mainActivity.originalCameraLauncher.launch(cameraIntent)
            }
            // 앨범에서 가져오기

        }
    }





    fun inputDone() {
        fragmentInputBinding.apply {
            // 책 타입 조정
            val bookType: BookType = when (toggleGroupTypeInputFragment.checkedButtonId) {
                R.id.buttonTypeLiteratureInputFragment -> BookType.BOOK_TYPE_Literature
                R.id.buttonTypeHumanityInputFragment -> BookType.BOOK_TYPE_HUMANITY
                R.id.buttonTypeNatureInputFragment -> BookType.BOOK_TYPE_NATURE
                else -> BookType.BOOK_TYPE_ETC
            }

            // 제목
            val bookTitle = editTextTitleInputFragment.editText?.text.toString()
            // 저자
            val bookAuthor = editTextAuthorInputFragment.editText?.text.toString()
            // 재고량
            val bookInventory = editTextInventoryInputFragment.editText?.text.toString().toInt()
            // 이미지 내부 경로
            var filePath =""
            if(mainActivity.contentUri !=null) {
                 filePath = mainActivity.contentUri.toString()
            }

            val viewModel = BookViewModel(0, bookType, bookTitle, bookAuthor, bookInventory, filePath)

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
                mainActivity.contentUri= null
                mainActivity.removeFragment(FragmentName.INPUT_FRAGMENT)
            }
        }
    }

}