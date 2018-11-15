package helloworld.zq.com.kotlindemo.http

import com.cxz.wanandroid.http.interceptor.CacheInterceptor
import com.cxz.wanandroid.http.interceptor.HeaderInterceptor
import com.cxz.wanandroid.http.interceptor.SaveCookieInterceptor
import helloworld.zq.com.kotlindemo.App
import helloworld.zq.com.kotlindemo.BuildConfig
import helloworld.zq.com.kotlindemo.constant.Constant
import helloworld.zq.com.kotlindemo.constant.HttpConstant
import helloworld.zq.com.kotlindemo.http.api.ApiService
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object RetrofitHelper {
    private var retrofit : Retrofit? = null

    val service : ApiService by lazy {
        getRetrofit()!!.create(ApiService::class.java)
    }

    private fun getRetrofit() : Retrofit?{
        if (retrofit == null){
            synchronized(RetrofitHelper::class.java){
                if (retrofit == null){
                    retrofit = Retrofit.Builder()
                            .baseUrl(Constant.BASE_URL)
                            .client(getOkHttpClient())
                            .addConverterFactory(MoshiConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build()
                }
            }
        }
        return  retrofit
    }

    /**
     * 获取 OkHttpClient
     */
    private fun getOkHttpClient(): OkHttpClient{
        val builder = OkHttpClient().newBuilder()
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG){
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }else{
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        //设置 请求的缓存位置和大小
        val cacheFile = File(App.context.cacheDir,"cache")
        val cache = Cache(cacheFile,HttpConstant.MAX_CACHE_SIZE)

        builder.run {
            addInterceptor(httpLoggingInterceptor)
            addInterceptor(HeaderInterceptor())
            addInterceptor(SaveCookieInterceptor())
                    .addInterceptor(CacheInterceptor())
                    .cache(cache)
                    .connectTimeout(HttpConstant.DEFAULT_TIMEOUT,TimeUnit.SECONDS)
                    .readTimeout(HttpConstant.DEFAULT_TIMEOUT,TimeUnit.SECONDS)
                    .writeTimeout(HttpConstant.DEFAULT_TIMEOUT,TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)//错误重连
        }
        return builder.build()
    }
}