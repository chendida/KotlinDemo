package helloworld.zq.com.kotlindemo.http.api

import helloworld.zq.com.kotlindemo.mvp.model.bean.HttpResult
import io.reactivex.Observable
import retrofit2.http.GET

interface ApiService {
    /**
     * 退出登录
     * http://www.wanandroid.com/user/logout/json
     */
    @GET("user/logout/json")
    fun logout(): Observable<HttpResult<Any>>
}