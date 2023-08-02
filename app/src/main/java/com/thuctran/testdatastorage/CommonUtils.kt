package com.thuctran.testdatastorage

import android.content.Context
import android.content.SharedPreferences

class CommonUtils {

    private val PREE_FILE: String = "PREE_FILE"

    //khởi tạo tĩnh instance
    companion object {
        private val instance: CommonUtils = CommonUtils()   //tạo hẳn ra thế này để cái instance ko bị null
        val INSTANCE: CommonUtils
            get() = instance
    }

    //mọi dữ liệu thuộc KDL gì khi lưu vào thì nên để thành String để lưu cho nó dễ

    //PT dùng để lưu giữ liệu nhập vào
    fun savePrefs(key: String, value: String) {
        val prefs: SharedPreferences =
            App.INSTANCE.getSharedPreferences(PREE_FILE, Context.MODE_PRIVATE)
        prefs.edit().putString(key, value).apply()
    }

    //PT dùng để lấy dữ liệu đã lưu ra
    fun getPrefs(key: String): String? {
        val prefs: SharedPreferences =
            App.INSTANCE.getSharedPreferences(PREE_FILE, Context.MODE_PRIVATE)
        return prefs.getString(key, null)
    }

    //PT này dùng để nếu muốn xóa ko lưu dữ liệu nữa
    fun clearPrefs(key: String) {
        val prefs: SharedPreferences =
            App.INSTANCE.getSharedPreferences(PREE_FILE, Context.MODE_PRIVATE)
        prefs.edit().remove(key).apply()
    }

}