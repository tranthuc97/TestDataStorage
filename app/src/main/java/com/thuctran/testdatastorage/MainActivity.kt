package com.thuctran.testdatastorage

import android.annotation.SuppressLint
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.thuctran.testdatastorage.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.nio.charset.StandardCharsets

class MainActivity : AppCompatActivity() {
    private val TAG: String = MainActivity::javaClass.name
    private var binding: ActivityMainBinding? = null
    private var listStory:MutableList<StoryModel>? = null
    private var index:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        initViews()
    }

    private fun initViews() {
        initPhotoTopics()
        initStoryFiles()
        binding!!.btPrevious.setOnClickListener{
            //set nút trước <0 thì gán lại bằng cái cuối danh sách, tương tự với nút tiếp, xong rồi gọi đến PT setContentStory()
            index--
            if(index < 0){
                index = listStory!!.size-1
            }
            setContentStory()
        }

        binding!!.btNext.setOnClickListener{
            index++
            if(index > listStory!!.size - 1){
                index = 0
            }
            setContentStory()
        }
        }

    private fun initStoryFiles() {



            listStory = mutableListOf()  //khai báo 1 collection danh sách các phần tử thuộc đối tượng StoryModel

            val inStory:InputStream = App.INSTANCE.assets.open("story/Con gái.txt")    //mở file dữ liệu trong asset, dùng this.assets để thay cho việc khởi tạo PT val assetMgr:AssetManager = assets
            //chỗ này có thể khai báo riêng đường dẫn: var storyPath:String = "story/"+Topic.getName()+".txt" để gọi đến từng tên truyện được khai báo trong Topic

            val isr = InputStreamReader(inStory, StandardCharsets.UTF_8)  //khởi tạo 1 reader để chuyền xuống dưới
            val reader = BufferedReader(isr)   //sử dụng BufferedReader sẽ hiệu quả hơn khi mở và đọc file text

            var name:String? = null     //khởi tạo name
            var content = StringBuilder()   //khởi tạo content:StringBuilder vì phải add từng dòng cho cả bài văn
            var line = reader.readLine()     //lấy reader ở trên đọc từng dòng (ở đây ko đc khai báo là kiểu String vì có thể dẫn đến reader.readLine() must not be null
            var model:StoryModel        //khởi tạo ĐT StoryModel, tí nữa sẽ add từng thằng này vào trong listStory

            //vòng lặp while với line luôn khác null, nếu Line == null tức là đã đọc hết các dòng, vòng lặp sẽ dừng lại
            while(line!=null){
                if(name == null){
                    name = line //khi name chưa được khởi tạo thì gán luôn name (tiêu đề truyện) bằng dòng đầu tiên
                } else if(line.contains("','0');")){
                    model = StoryModel(name, content.toString(), false)    //add name và content làm dữ liệu đầu vào cho model
                    listStory!!.add(model)    //add model vào listStory
                    name = null    //add xong rồi thì reset name = null để chạy lại vòng mới
                    content = StringBuilder()      //add xong rồi thì khởi tạo lại content
                } else if(line.isNotEmpty()){
                    content.append(line).append("\n")   //dòng nào khác rỗng thì nối (append) vào trong content
                }
                    line = reader.readLine()    //tiếp tục đọc dòng tiếp theo

            }
            inStory.close()
            isr.close()
            reader.close()  //dọc xong rồi nhớ đóng lại
        Log.i(TAG,"kích thước"+listStory!!.size)
        Log.i(TAG,listStory.toString())
        setContentStory()
    }

    private fun setContentStory() {
       binding!!.tvName.text = listStory!!.get(index).NAME
       binding!!.tvContent.text = listStory!!.get(index).CONTENT
    }

    @SuppressLint("InflateParams")
    private fun initPhotoTopics() {
        //khai báo assetManager
        val assetMgr: AssetManager = assets
        val listFileName: Array<String> =
            App.INSTANCE.assets.list("photo/")!!    //khai báo tên danh sách file ảnh

        binding!!.lnList.removeAllViews()     //reset các view có trước đó
        for (photoPath: String in listFileName) {
            val itemView = LayoutInflater.from(this).inflate(R.layout.detail_act, null)
            val tvName: TextView = itemView.findViewById(R.id.tv_name)
            val ivPhoto: ImageView = itemView.findViewById(R.id.iv_photo)

            val inImg: Bitmap = BitmapFactory.decodeStream(assetMgr.open("photo/" + photoPath))   //khai báo 1 bitmap từ việc mở một đường dẫn file ảnh trong danh sách
            ivPhoto.setImageBitmap(inImg)     //set Bitmap cho ảnh để hiển thị
            tvName.text = photoPath.replace(
                ".png",
                ""
            ) //gọi hàm thay thế cái đuôi .jpg kia biến mất, hiển thị tên ảnh. đây là lấy từ chính cái đoạn tên đường dẫn photoPath kia

            //gắp cái vừa ánh xạ view vào view lnTopic
            binding!!.lnList.addView(itemView)
    }
}
}