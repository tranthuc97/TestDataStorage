package com.thuctran.testdatastorage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.core.content.contentValuesOf
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.thuctran.testdatastorage.R
import com.thuctran.testdatastorage.StoryModel
import com.thuctran.testdatastorage.databinding.RecyclerviewActBinding

//truyền PT khởi tạo 2 TS context và listStory cho StoryAdapter
class StoryAdapter(private var context:Context, private var listStory:MutableList<StoryModel>) : RecyclerView.Adapter<StoryAdapter.StoryHolder>() {

     var storyLD:MutableLiveData<StoryModel> = MutableLiveData()     //click vào thì lưu cái story hiện tại đang sử dụng (hoặc thay đổi dữ liệu truyền sang View)

    //từ item_story trong res/layout ta ánh xạ thành StoryHolder(inflate)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryHolder {
        var view:View = LayoutInflater.from(context).inflate(R.layout.item_story,parent,false)      //ánh xạ thằng item_story ra
        return StoryHolder(view)    //trả về thằng StoryHolder(view)
    }

    //PT định nghĩa số lượng item để đúc
    override fun getItemCount(): Int {
        return listStory.size
    }

    //StoryHolder lấy ở thằng class bên dưới, đây là PT đưa vào số lượng itemdata, để dữ liệu tương ứng của từng data vào từng storyHolder
    override fun onBindViewHolder(holder: StoryHolder, position: Int) {
        var data:StoryModel = listStory[position]       //khai báo data chính là StoryModel trong danh sách ở vị trí thứ position
        holder.tvStoryName.text = data.NAME      //lấy thằng data trở tới NAME và set dữ liệu cho tv
        holder.tvStoryName.tag = data
        holder.lnBG.setBackgroundResource(if(data.isSELECTED) R.color.colorGray else R.color.white)     //nếu data được chọn thì set mau gray, ko thì sẽ là màu white
    }

    //class StoryHolder tự mình đặt tên là class được ánh xạ từ itemView, phải để là inner class để còn sử dụng lại được các TT của class cha
    inner class StoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView)    {
        //StoryHolder đã trỏ đến item_view nên có thể lấy các ĐT bên trong view đó ra
        var tvStoryName:TextView = itemView.findViewById(R.id.tv_story_name)     //khai báo tv_story_name (tên truyện)
        var lnBG:View = itemView.findViewById(R.id.ln_BG)
        init {              //chỗ này phải đặt init vì gọi hàm setOnclick ko nằm trong PT nào cả
            itemView.setOnClickListener{
                it.startAnimation(AnimationUtils.loadAnimation(context,androidx.appcompat.R.anim.abc_fade_in))
                clickItemStory(tvStoryName.tag as StoryModel)
            }
        }

        private fun clickItemStory(story: StoryModel) {
            story.isSELECTED = true     //click vào rồi cái story này mình set nó bằng true
            if(storyLD.value!=null){       //thằng story cũ set bằng false
                storyLD.value!!.isSELECTED = false
            }

            storyLD.postValue(story)
            //refresh toàn bộ item trong recyclerView mục đích để gọi lại PT setBackgroundResource
            notifyItemRangeChanged(0,listStory.size!!)


        }
    }

}