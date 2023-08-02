package com.thuctran.testdatastorage

class StoryModel(private var name: String, private var content: String, private var isSelected:Boolean) {
    val NAME: String
        get() = name


    val CONTENT: String
        get() = content


    var  isSELECTED:Boolean      //PT này để biết mình có click vào thằng Story đc chọn này hay ko
        get() = isSelected
        set(value) {
            isSelected = value
        }

    override fun toString(): String {
        return name + ":" + content
    }
}