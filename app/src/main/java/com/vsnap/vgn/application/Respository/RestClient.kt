package com.vsnap.vgn.application.Respository

import com.vsnap.vgn.application.Interface.ApiInterfaces
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class RestClient {
     var apiInterfaces: ApiInterfaces

    companion object {
        //const val BASE_URL = "http://smartcall.dialoutworks.com/v1/"
        const val BASE_URL = "http://vgntalkdesk.dialoutworks.com/v1/"
    }

    init {
        val client = OkHttpClient.Builder()
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        client.interceptors().add(interceptor)
        val client1: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(300, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .build()
        apiInterfaces = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client1)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterfaces::class.java)
    }
}