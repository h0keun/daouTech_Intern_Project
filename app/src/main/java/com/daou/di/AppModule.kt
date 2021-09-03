//package com.daou.di
//
//import com.daou.api.RequestToServer
//import com.daou.api.RetrofitService
//import okhttp3.JavaNetCookieJar
//import okhttp3.OkHttpClient
//import org.koin.dsl.module
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import java.net.CookieManager
//
//val appModule = module {
//
//
//}
//
//val retrofitModule = module {
//
////    single {
////        OkHttpClient.Builder()
////            .cookieJar(JavaNetCookieJar(CookieManager()))
////            .build()
////    }
////
////    single {
////        Retrofit.Builder()
////            .baseUrl(RequestToServer.BASE_URL)
////            .client(RequestToServer.okHttpClient)
////            .addConverterFactory(GsonConverterFactory.create())
////            .build()
////    }
////
////    single {
////        get<Retrofit>.create(
////            RetrofitService::class.java
////        }
////    }
//}