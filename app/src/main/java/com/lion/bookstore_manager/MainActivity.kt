package com.lion.bookstore_manager

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
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


}