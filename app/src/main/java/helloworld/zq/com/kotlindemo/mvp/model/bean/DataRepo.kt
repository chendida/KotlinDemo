package helloworld.zq.com.kotlindemo.mvp.model.bean

import com.squareup.moshi.Json

data class HttpResult<T>(@Json(name = "data") val data : T,
                         @Json(name = "errorCode") val errorCode : Int,
                         @Json(name = "errorMsg") val  errorMsg : String)