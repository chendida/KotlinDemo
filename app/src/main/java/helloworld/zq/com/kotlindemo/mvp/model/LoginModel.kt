package helloworld.zq.com.kotlindemo.mvp.model

import helloworld.zq.com.kotlindemo.base.BaseModel
import helloworld.zq.com.kotlindemo.http.RetrofitHelper
import helloworld.zq.com.kotlindemo.mvp.model.bean.HttpResult
import helloworld.zq.com.kotlindemo.mvp.model.bean.LoginData
import helloworld.zq.com.kotlindemo.rx.SchedulerUtils
import io.reactivex.Observable

class LoginModel : BaseModel() {

    fun loginWanKotlin(username : String,password : String) : Observable<HttpResult<LoginData>>{

        return RetrofitHelper.service.loginWanKotlin(username,password)
                .compose(SchedulerUtils.ioToMain())
    }
}