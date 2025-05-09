package com.snapshot

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context


class SnapShotApplication : Application() {

    override fun onCreate() {
        super.onCreate()
//        RetrofitClient.init(this)
        context = applicationContext
    }


    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var context: Context

        fun getContext() = context // -> context는 여기서 생성되고 사용할 때는 SnapShotApplication.getContext()로 가져와 사용하시기 바랍니다
        // -> 직접 localContext를 생성해 쓰면 아주 약간의 메로리 낭비와 오류발생 가능성이 높아집니다
    }
}