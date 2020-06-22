package com.bossdga.filmender.source.network

import android.content.Context
import com.bossdga.filmender.source.network.api.ConfigurationAPI
import com.bossdga.filmender.source.network.api.MovieAPI
import com.bossdga.filmender.source.network.api.TVShowAPI
import com.bossdga.filmender.util.LanguageUtils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


/**
 * API Client that provides access to a single instance of the Service
 */
object RetrofitService {
    private const val BASE_URL = "https://api.themoviedb.org/"
    private const val API_KEY = "76e820d1f6a93db9e0e12c04a69fc6d1"
    private lateinit var context: Context

    fun initWith(context: Context){
        this.context = context
    }

    private fun retrofitService(): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .client(createClient())
                .build()
    }

    private fun createClient(): OkHttpClient {
         return OkHttpClient.Builder().addInterceptor { chain ->
             val original = chain.request()
             val originalHttpUrl = original.url()

             val url = originalHttpUrl.newBuilder()
                 .addQueryParameter("api_key", API_KEY)
                 .addQueryParameter("language", LanguageUtils.getSystemLanguage())
                 .addQueryParameter("include_image_language", LanguageUtils.getSystemLanguage() + ",null").build()
                 //.addQueryParameter("region", LanguageUtils.getSystemCountry()).build()
             val request = original.newBuilder().url(url).build()
             println(">>>>>>>>>>>>>>> Request URL: " + request.url())
             chain.proceed(request)
         }.build()
    }

    val movieApi: MovieAPI by lazy {
        retrofitService().create(MovieAPI::class.java)
    }

    val tvShowApi: TVShowAPI by lazy {
        retrofitService().create(TVShowAPI::class.java)
    }

    val configurationApi: ConfigurationAPI by lazy {
        retrofitService().create(ConfigurationAPI::class.java)
    }
}