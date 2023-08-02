package com.thuctran.testdatastorage

import android.app.Application

class App : Application() {
    companion object {
        private var instance:App? = null
        val INSTANCE:App
            get() = instance!!
    }

    private var storage:Storage? = null
    val STORAGE:Storage
        get() = storage!!

    override fun onCreate() {
        super.onCreate()
        instance = this
        storage = Storage()
    }
}