package com.lion.bookstore_manager

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.google.android.material.transition.MaterialSharedAxis
import com.lion.bookstore_manager.databinding.ActivityMainBinding
import com.lion.bookstore_manager.fragment.InputFragment
import com.lion.bookstore_manager.fragment.LocationFragment
import com.lion.bookstore_manager.fragment.MainFragment
import com.lion.bookstore_manager.fragment.ModifyFragment
import com.lion.bookstore_manager.fragment.ShowFragment
import com.lion.bookstore_manager.util.FragmentName

class MainActivity : AppCompatActivity() {
    // 원본 사진 찍기용 런처
    lateinit var originalCameraLauncher: ActivityResultLauncher<Intent>

    // 촬영된 사진이 위치할 경로
    lateinit var filePath:String
    // 저장된 파일에 접근하기 위한 Uri
    var contentUri: Uri? = null


    lateinit var activityMainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 외부 저장소 경로를 가져온다.
        filePath = getExternalFilesDir(null).toString()

        replaceFragment(FragmentName.MAIN_FRAGMENT,false,false,null)
    }


        // 프래그먼트를 교체하는 함수
        fun replaceFragment(fragmentName: FragmentName, isAddToBackStack:Boolean, animate:Boolean, dataBundle: Bundle?){
            // 프래그먼트 객체
            val newFragment = when(fragmentName){

                FragmentName.MAIN_FRAGMENT -> MainFragment()
                FragmentName.INPUT_FRAGMENT -> InputFragment()
                FragmentName.SHOW_FRAGMENT -> ShowFragment()
                FragmentName.MODIFY_FRAGMENT -> ModifyFragment()
                FragmentName.LOCATION_FRAGMENT -> LocationFragment()

            }

            // bundle 객체가 null이 아니라면
            if(dataBundle != null){
                newFragment.arguments = dataBundle
            }

            // 프래그먼트 교체
            supportFragmentManager.commit {

                if(animate) {
                    newFragment.exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
                    newFragment.reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
                    newFragment.enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
                    newFragment.returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
                }

                replace(R.id.fragmentContainerView, newFragment)
                if(isAddToBackStack){
                    addToBackStack(fragmentName.str)
                }
            }
        }

        // 프래그먼트를 BackStack에서 제거하는 메서드
        fun removeFragment(fragmentName: FragmentName){
            supportFragmentManager.popBackStack(fragmentName.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }



    // 이미지를 회전시키는 메서드
    fun rotateBitmap(bitmap: Bitmap, degree:Int): Bitmap {
        // 회전 이미지를 구하기 위한 변환 행렬
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        // 회전 행렬을 적용하여 회전된 이미지를 생성한다.
        val resultBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, false)
        return resultBitmap
    }

    // 회전 각도값을 구하는 메서드
    fun getDegree(uri:Uri):Int{

        // 이미지의 태그 정보에 접근할 수 있는 객체를 생성한다.
        // andorid 10 버전 이상이라면
        val exifInterface = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            // 이미지 데이터를 가져올 수 있는 Content Provider의 Uri를 추출한다.
            val photoUri = MediaStore.setRequireOriginal(uri)
            // 컨텐츠 프로바이더를 통해 파일에 접근할 수 있는 스트림을 추출한다.
            val inputStream = contentResolver.openInputStream(photoUri)
            // ExifInterface 객체를 생성한다.
            ExifInterface(inputStream!!)
        } else {
            ExifInterface(uri.path!!)
        }

        // ExifInterface 정보 중 회전 각도 값을 가져온다
        val ori = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)

        // 회전 각도값을 담는다.
        val degree = when(ori){
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }

        return degree
    }

    // 이미지의 사이즈를 줄이는 메서드
    fun resizeBitmap(targetWidth:Int, bitmap: Bitmap): Bitmap {
        // 이미지의 축소/확대 비율을 구한다.
        val ratio = targetWidth.toDouble() / bitmap.width.toDouble()
        // 세로 길이를 구한다.
        val targetHeight = (bitmap.height.toDouble() * ratio).toInt()
        // 크기를 조절한 Bitmap 객체를 생성한다.
        val result = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, false)
        return result
    }


}