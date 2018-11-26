package helloworld.zq.com.kotlindemo.mvp.model

import helloworld.zq.com.kotlindemo.base.BaseModel
import helloworld.zq.com.kotlindemo.http.RetrofitHelper
import helloworld.zq.com.kotlindemo.mvp.model.bean.ArticleResponseBody
import helloworld.zq.com.kotlindemo.mvp.model.bean.HttpResult
import helloworld.zq.com.kotlindemo.rx.SchedulerUtils
import io.reactivex.Observable

class SearchListModel : BaseModel() {

    fun queryBySearchKey(page : Int,key : String) : Observable<HttpResult<ArticleResponseBody>>{
        return RetrofitHelper.service.queryBySearchKey(page,key)
                .compose(SchedulerUtils.ioToMain())
    }
}