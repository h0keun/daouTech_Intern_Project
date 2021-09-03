//package com.daou
//
//import android.app.Application
//import android.content.Context
//import com.daou.di.appModule
//import com.daou.di.retrofitModule
//import org.koin.android.ext.koin.androidContext
//import org.koin.android.ext.koin.androidFileProperties
//import org.koin.android.ext.koin.androidLogger
//import org.koin.core.context.startKoin
//
//class MyApplication : Application() {
//
//    init {
//        instance = this
//    }
//
//    companion object {
//        lateinit var instance: MyApplication
//        fun applicationContext() : Context {
//            return instance.applicationContext
//        }
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//
//        startKoin {
//            // 로그찍어보기 가능
//            androidLogger()
//            // Android Context 넘겨줌
//            androidContext(this@MyApplication)
//            // assets/koin.properties 파일에서 프로퍼티를 가져옴
//            androidFileProperties()
//            // 모듈리스트
//
//            modules(listOf(
//                appModule,
//            retrofitModule)
//            )
//        }
//    }
//}