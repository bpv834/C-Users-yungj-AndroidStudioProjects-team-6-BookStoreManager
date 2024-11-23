package com.lion.bookstore_manager.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lion.bookstore_manager.MainActivity
import com.lion.bookstore_manager.R
import com.lion.bookstore_manager.databinding.FragmentInputBinding
import com.lion.bookstore_manager.databinding.FragmentShowBinding

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
        return fragmentShowBinding.root
    }

    fun settingView() {
        fragmentShowBinding.apply {
            editTextTitle.editText?.setText("sasdasd")

        }
    }

}