package com.thuctran.testdatastorage.adapter

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.thuctran.testdatastorage.App
import com.thuctran.testdatastorage.databinding.ActDetailStoryBinding

class DetailStoryAct : AppCompatActivity() {

    private var binding:ActDetailStoryBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        initViews()
    }

    private fun initViews() {
        var adapter = DetailStoryAdapter(this, App.INSTANCE.STORAGE.listStory!!)
        binding!!.vpStory.adapter = adapter

        var selectedIndex:Int = App.INSTANCE.STORAGE.listStory!!.indexOf(App.INSTANCE.STORAGE.storyModel)       //lấy vị trí index của thằng storyModel được chọn
        binding!!.vpStory.setCurrentItem(selectedIndex,true)      //khi mở ra thì set luôn vpStory ở cái thằng storyModel được chọn
        //muốn tự động vuốt sang trang khác, gọi lại setCurrentItem(selectedIndex,true)
    }
}