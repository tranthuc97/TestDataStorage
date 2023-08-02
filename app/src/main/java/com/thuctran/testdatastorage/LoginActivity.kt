package com.thuctran.testdatastorage

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.thuctran.testdatastorage.databinding.LoginActBinding
import java.io.FileOutputStream
import java.io.InputStream
import java.io.UTFDataFormatException
import java.nio.charset.StandardCharsets.UTF_8

class LoginActivity : AppCompatActivity() {
    private val KEY_ACC: String = "KEY_ACC"
    private var binding:LoginActBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        initViews()
    }

    private fun initViews() {
        binding!!.ckRemember.setOnClickListener{
            doSaveAcc()
        }

        //tạo 1 thư mục "prefs_saving" để lưu dữ liệu
        var prefs:SharedPreferences  = getSharedPreferences("prefs_saving", MODE_PRIVATE)    //luôn để là kiểu MODE_PRIVATE để các ứng dụng khác ko thể truy cập

        var phone: String? = CommonUtils.INSTANCE.getPrefs(KEY_ACC)      //khi vào ứng dụng lấy luôn dữ liệu từ trong KEY_ACC (getString) nếu có và gán cho phone
        if(phone!=null){
            binding!!.edtPhone.setText(phone)           //set phone cho edtPhone
            binding!!.ckRemember.isChecked = true      //nếu ko có dữ liệu đã lưu thì phone nó Empty, và lúc này sẽ bỏ check, ngược lại nếu có dữ liệu thì nó sẽ hiện check luôn
        }

        binding!!.btSave.setOnClickListener{
            saveToDataStorage()         //PT lưu dữ liệu khi nhập vào ô phone
            savePhotoDataStorage()      //PT lưu ảnh vào trong kho
        }
        binding!!.btSave2.setOnClickListener{
            saveInExternalStorage()
        }

    }

    private fun doSaveAcc() {
        //nếu chưa nhập đầu vào thì toast lên 1 cái
        if(binding!!.edtPhone.text.toString().isEmpty()){
            Toast.makeText(this,"please input your phone to remember",Toast.LENGTH_SHORT).show()
            return
        }

        var prefs:SharedPreferences  = getSharedPreferences("prefs_saving", MODE_PRIVATE)
        if(binding!!.ckRemember.isChecked){
           CommonUtils.INSTANCE.savePrefs(KEY_ACC, binding!!.edtPhone.text.toString())   //nếu tích vào check thì sẽ lưu giữ liệu với tên là KEY_ACC (putString), gọi đến PT savePrefs() bên CommonUtils()
            //chỗ này nếu muốn lưu dữ liệu kiểu khác sẽ là putKDL()
        } else {
            CommonUtils.INSTANCE.clearPrefs(KEY_ACC)     //nếu ko tích check thì sẽ remove lại giữ liệu trong KEY_ACC để ko lưu trữ nưa
        }
    }

    private fun saveToDataStorage() {
        //nếu chưa nhập đầu vào thì toast lên 1 cái
        if(binding!!.edtPhone.text.toString().isEmpty()){
            Toast.makeText(this,"please input your phone to remember",Toast.LENGTH_SHORT).show()
            return
        }

        var dataPath:String = Environment.getDataDirectory().path+"/data/"+packageName  //khai báo đường dẫn trở tới data/data/packageName
        var filePath = dataPath+"/content.txt"      //tạo tên file content.txt

        var out = FileOutputStream(filePath)       //mở file cần ghi dữ liệu
        var buff: ByteArray = binding!!.edtPhone.text.toString().toByteArray(Charsets.UTF_8)    //khai báo một mảng buff
        out.write(buff,0,buff.size)     //ghi dữ liệu vào trong file, ghi các byte từ 0->buff.size
        out.close()     //ghi xong nhớ đóng
        Toast.makeText(this,"File is written",Toast.LENGTH_SHORT).show()
    }

    private fun savePhotoDataStorage() {
        var asset:AssetManager = assets
        var inPhoto: InputStream = asset.open("photo/Con gái.png")       //phở file theo đường dẫn
        var dataPath:String = Environment.getDataDirectory().path+"/data/"+packageName      //khai báo đường dẫn
        var photoPath = dataPath+"/Con gái.png"     //tạo file
        var out = FileOutputStream(photoPath)      //mở file cần ghi dữ liệu

        var buff:ByteArray = ByteArray(1024)     //khởi tạo mảng byte
        var len:Int = inPhoto.read(buff)        //đọc dữ liệu của ảnh khai báo ở trên (inPhoto) theo từng byte
        while(len>0){
            out.write(buff,0,buff.size)
            len = inPhoto.read()    //tiếp tục đọc đến khi len <= 0
        }

        out.close()
        inPhoto.close()     //mở đường dẫn ra phải đóng vào
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var rs:Int
        for (rs in grantResults){       //duyệt danh sách grantResult
            if(rs!=PackageManager.PERMISSION_GRANTED){      //không có cái rs nào bằng PackageManager.PERMISSION_GRANTED thì toat lên
                Toast.makeText(this,"Please allow this permisson to save data",Toast.LENGTH_SHORT).show()       //chưa có quyền thì sẽ toast lên
            }
        }
    }


    private fun saveInExternalStorage() {
        if(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){             //check nếu chưa có quyền ghi dữ liệu
            //thì yêu cầu quyền đọc và ghi với requestCode 101
            requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE),101)   //2 thằng này sẽ trả về grantResult
            return
        }

        var inPhoto: InputStream = App.INSTANCE.assets.open("photo/Con gái.png")       //phở file theo đường dẫn
        var dataPath:String = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!.path    //khai báo đường dẫn ở bên ngoài trong thư mục download (điểm khác so với thằng lưu trên DataSystemStorage
        var photoPath = dataPath+"/Con gái.png"     //tạo file
        var out = FileOutputStream(photoPath)      //mở file cần ghi dữ liệu

        var buff:ByteArray = ByteArray(1024)     //khởi tạo mảng byte
        var len:Int = inPhoto.read(buff)        //đọc dữ liệu của ảnh khai báo ở trên (inPhoto) theo từng byte
        while(len>0){
            out.write(buff,0,buff.size)
            len = inPhoto.read()    //tiếp tục đọc đến khi len <= 0
        }

        out.close()
        inPhoto.close()     //mở đường dẫn ra phải đóng vào
    }
}