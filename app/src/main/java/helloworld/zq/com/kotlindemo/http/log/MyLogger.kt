package helloworld.zq.com.kotlindemo.http.log

import android.text.TextUtils
import com.orhanobut.logger.Logger
import helloworld.zq.com.kotlindemo.BuildConfig
import okhttp3.logging.HttpLoggingInterceptor

class MyLogger : HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        if (!TextUtils.isEmpty(message) && BuildConfig.DEBUG){
            Logger.t("WanKotlin")
            if (message.startsWith("{") || message.startsWith("[")) {
                Logger.json(message)
            } else {
                Logger.d(message)
            }
        }
    }
}