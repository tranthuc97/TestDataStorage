package com.thuctran.testdatastorage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.thuctran.testdatastorage.R
import com.thuctran.testdatastorage.StoryModel

class DetailStoryAdapter(private var context:Context, private var listStory:MutableList<StoryModel>) : PagerAdapter() {


    //xác định số lượng item cần đúc
    override fun getCount(): Int {
        return listStory.size
    }

    //ánh xạ item_view vào code và đổ dữ liệu từ data vào
    override fun instantiateItem(viewPager: ViewGroup, position: Int): Any {
        var view:View = LayoutInflater.from(context).inflate(R.layout.item_detail_story,viewPager,false)
        var tvName:TextView = view.findViewById(R.id.tv_title)
        var tvContent:TextView = view.findViewById(R.id.tv_content)
        var data:StoryModel = listStory[position]

        tvName.text = data.NAME
        tvContent.text = data.CONTENT

        viewPager.addView(view)     //add cái view vừa ánh xạ vào trong viewPager
        return view

    }

    //so sánh 2 view cũ và mới, nếu view mới chiếm tỉ lệ quá 1/2 thì view mới xuất hiện, ngược lại thì giữ nguyên view cũ
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`     //so sánh view mới và view cũ
    }

    //Hủy itemView khi nó ra khỏi màn hình
    override fun destroyItem(viewPager: ViewGroup, position: Int, `object`: Any) {
        viewPager.removeView(`object` as View)      //view out ra khỏi màn hình thì sẽ remove nó
    }
}