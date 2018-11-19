package helloworld.zq.com.kotlindemo.mvp.model

import helloworld.zq.com.kotlindemo.base.BaseModel
import helloworld.zq.com.kotlindemo.http.RetrofitHelper
import helloworld.zq.com.kotlindemo.mvp.model.bean.HttpResult
import helloworld.zq.com.kotlindemo.rx.SchedulerUtils
import io.reactivex.Observable

open class CommonModel : BaseModel(){
    fun addCollectArticle(id : Int) : Observable<HttpResult<Any>> {
        return RetrofitHelper.service
                .addCollectArticle(id)
                .compose(SchedulerUtils.ioToMain())
    }

    fun cancelCollectArticle(id : Int) : Observable<HttpResult<Any>>{
        return RetrofitHelper.service
                .cancelCollectArticle(id)
                .compose(SchedulerUtils.ioToMain())
    }
}