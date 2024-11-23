package com.lion.bookstore_manager.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.lion.bookstore_manager.MainActivity
import com.lion.bookstore_manager.R
import com.lion.bookstore_manager.databinding.ActivityMainBinding
import com.lion.bookstore_manager.databinding.FragmentMainBinding
import com.lion.bookstore_manager.databinding.RowMainBinding
import com.lion.bookstore_manager.util.FragmentName

class MainFragment : Fragment() {

    var testList = MutableList(50){ it+1 }




    lateinit var fragmentMainBinding: FragmentMainBinding

    lateinit var mainActivity : MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        fragmentMainBinding = FragmentMainBinding.inflate(inflater, container, false)

        settingToolbarMain()
        settingFabMain()
        settingRecyclerView()
        return fragmentMainBinding.root
    }

    fun settingToolbarMain() {
        fragmentMainBinding.apply {
            //w
           // 타이틀
            materialToolbarMainFragment.title = "등록 정보"
            materialToolbarMainFragment.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menuShowMapMainFragment ->{
                        mainActivity.replaceFragment(FragmentName.LOCATION_FRAGMENT,true,true,null)
                        true
                    }
                    else->{true}

                }
            }
        }
    }

    fun settingFabMain() {
        fragmentMainBinding.apply {
            fabMainFragment.setOnClickListener{
                mainActivity.replaceFragment(FragmentName.INPUT_FRAGMENT,true,true,null)
            }
        }
    }

    // RecyclerView를 구성하는 메서드
    fun settingRecyclerView(){
        fragmentMainBinding.apply {
            // 어뎁터
            recyclerViewMainFragment.adapter = RecyclerViewMainAdapter()
            // LayoutManager
            recyclerViewMainFragment.layoutManager = LinearLayoutManager(mainActivity)
            // 구분선
            val deco = MaterialDividerItemDecoration(mainActivity, MaterialDividerItemDecoration.VERTICAL)
            recyclerViewMainFragment.addItemDecoration(deco)
        }
    }

    // RecyclerView의 어뎁터
    inner class RecyclerViewMainAdapter : RecyclerView.Adapter<RecyclerViewMainAdapter.ViewHolderMain>(){
        // ViewHolder
        inner class ViewHolderMain(val rowMainBinding: RowMainBinding) : RecyclerView.ViewHolder(rowMainBinding.root),
            OnClickListener {
            override fun onClick(v: View?) {
                // 사용자가 누른 동물 인덱스 담아준다.
                val dataBundle = Bundle()
               // dataBundle.putInt("animalIdx", testList[adapterPosition].)
                // ShowFragment로 이동한다.
              mainActivity.replaceFragment(FragmentName.SHOW_FRAGMENT, true,true, dataBundle)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMain {

//            val rowMainBinding = RowMainBinding.inflate(layoutInflater)
//            rowMainBinding.root.layoutParams = ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//            )

            // RecyclerView는 자신에게 붙어진 항목 View의 크기를 자동으로 설정해주지 않는다.
            // 이에 ViewBinding 객체를 생성할 때 부모가 누구인지를 알려줘야지만 match_parent를 통해 크기를 설정할 수 있다.
            // 두 번째  : 부모를 지정한다. onCreateViewHolder 메서드의 parent 변수안에는 RecyclerView가 들어온다.
            // 이를 지정해준다.
            // 세 번째 : 생성된 View 객체를 부모에 붙힐 것인가를 설정한다. ReyclerView는 나중에 항목 View가 자동으로 붙는다.
            // 여기에서 true를 넣어주면 ViewBinding 객체가 생성될때 한번, ViewHolder를 반환할 때 한번 더 붙힐려고 하기 때문에
            // 오류가 발생한다.
            val rowMainBinding = RowMainBinding.inflate(layoutInflater, parent, false)

            val viewHolderMain = ViewHolderMain(rowMainBinding)

            // 리스너를 설정해준다.
            rowMainBinding.root.setOnClickListener(viewHolderMain)

            return viewHolderMain
        }

        override fun getItemCount(): Int {
            return testList.size
        }

        override fun onBindViewHolder(holder: ViewHolderMain, position: Int) {
            holder.rowMainBinding.textViewRowMain.text = testList[position].toString()
        }
    }



}