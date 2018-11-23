package helloworld.zq.com.kotlindemo.mvp.model

import helloworld.zq.com.kotlindemo.base.BaseModel
import helloworld.zq.com.kotlindemo.http.RetrofitHelper
import helloworld.zq.com.kotlindemo.mvp.model.bean.CollectionArticle
import helloworld.zq.com.kotlindemo.mvp.model.bean.CollectionResponseBody
import helloworld.zq.com.kotlindemo.mvp.model.bean.HttpResult
import helloworld.zq.com.kotlindemo.rx.SchedulerUtils
import io.reactivex.Observable

class CollectModel : BaseModel() {
    fun getCollectList(page : Int) : Observable<HttpResult<CollectionResponseBody<CollectionArticle>>>{
        return RetrofitHelper.service.getCollectList(page)
                .compose(SchedulerUtils.ioToMain())
    }

    fun removeCollectionArticle(id : Int,originId : Int) : Observable<HttpResult<Any>>{
        return RetrofitHelper.service.removeCollectArticle(id,originId)
                .compose(SchedulerUtils.ioToMain())
    }
}