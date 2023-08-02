package com.thuctran.testdatastorage.adapter

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.thuctran.testdatastorage.App
import com.thuctran.testdatastorage.StoryModel
import com.thuctran.testdatastorage.databinding.RecyclerviewActBinding
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.nio.charset.StandardCharsets

class TestRecyclerView : AppCompatActivity() {
    private var binding:RecyclerviewActBinding? = null
    private var adapter:StoryAdapter? = null    //khai báo adapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RecyclerviewActBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        initViews()
    }

    private fun initViews() {
        var listStory:MutableList<StoryModel> = mutableListOf()  //khai báo 1 collection danh sách các phần tử thuộc đối tượng StoryModel

        val inStory: InputStream = this.assets.open("story/Con gái.txt")    //mở file dữ liệu trong asset, dùng this.assets để thay cho việc khởi tạo PT val assetMgr:AssetManager = assets
        //chỗ này có thể khai báo riêng đường dẫn: var storyPath:String = "story/"+Topic.getName()+".txt" để gọi đến từng tên truyện được khai báo trong Topic

        val isr = InputStreamReader(inStory, StandardCharsets.UTF_8)  //khởi tạo 1 reader để chuyền xuống dưới
        val reader = BufferedReader(isr)   //sử dụng BufferedReader sẽ hiệu quả hơn khi mở và đọc file text

        var name:String? = null     //khởi tạo name
        var content = StringBuilder()   //khởi tạo content:StringBuilder vì phải add từng dòng cho cả bài văn
        var line = reader.readLine()     //lấy reader ở trên đọc từng dòng (ở đây ko đc khai báo là kiểu String vì có thể dẫn đến reader.readLine() must not be null
        var model: StoryModel        //khởi tạo ĐT StoryModel, tí nữa sẽ add từng thằng này vào trong listStory

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

        adapter = StoryAdapter(this,listStory)
        binding!!.rvStory.adapter = adapter

        App.INSTANCE.STORAGE.listStory = listStory      //add thằng listStory vào trong storage để dùng chung
        adapter!!.storyLD.observe(this){            //lắng nghe sự thay đổi của storyLD, ở bên kia nó sẽ chuyền một thằng StoryModel ở vị trí positon sang
            if(it == null ) return@observe
            openStoryDetail(it)
        }
    }

    private fun openStoryDetail(storyModel: StoryModel) {
        App.INSTANCE.STORAGE.storyModel = storyModel


        startActivity(Intent(this,DetailStoryAct::class.java))      //chuyển sang màn hình detailStoryAct
    }
}