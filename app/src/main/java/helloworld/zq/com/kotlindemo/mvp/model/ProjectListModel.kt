package helloworld.zq.com.kotlindemo.mvp.model

import helloworld.zq.com.kotlindemo.http.RetrofitHelper
import helloworld.zq.com.kotlindemo.mvp.model.bean.ArticleResponseBody
import helloworld.zq.com.kotlindemo.mvp.model.bean.HttpResult
import helloworld.zq.com.kotlindemo.rx.SchedulerUtils
import io.reactivex.Observable

class ProjectListModel {

    fun requestProjectList(page : Int,cid : Int) : Observable<HttpResult<ArticleResponseBody>>{
        return RetrofitHelper.service.getProjectList(page,cid)
                .compose(SchedulerUtils.ioToMain())
    }
}