package helloworld.zq.com.kotlindemo.mvp.model

import helloworld.zq.com.kotlindemo.base.BaseModel
import helloworld.zq.com.kotlindemo.http.RetrofitHelper
import helloworld.zq.com.kotlindemo.mvp.model.bean.HttpResult
import helloworld.zq.com.kotlindemo.rx.SchedulerUtils
import io.reactivex.Observable

class MainModel : BaseModel() {

    fun logout() : Observable<HttpResult<Any>> {
        return RetrofitHelper.service.logout()
                .compose(SchedulerUtils.ioToMain())
    }
}