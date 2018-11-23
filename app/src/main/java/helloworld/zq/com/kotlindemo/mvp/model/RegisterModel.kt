package helloworld.zq.com.kotlindemo.mvp.model

import helloworld.zq.com.kotlindemo.base.BaseModel
import helloworld.zq.com.kotlindemo.http.RetrofitHelper
import helloworld.zq.com.kotlindemo.mvp.model.bean.HttpResult
import helloworld.zq.com.kotlindemo.mvp.model.bean.LoginData
import helloworld.zq.com.kotlindemo.rx.SchedulerUtils
import io.reactivex.Observable

class RegisterModel : BaseModel() {
    fun registerWanKotlin(username : String,password : String,rePassword : String)
            : Observable<HttpResult<LoginData>>{
        return RetrofitHelper.service.registerWanKotlin(username,password,rePassword)
                .compose(SchedulerUtils.ioToMain())
    }
}