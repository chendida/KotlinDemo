package helloworld.zq.com.kotlindemo.mvp.model

import helloworld.zq.com.kotlindemo.http.RetrofitHelper
import helloworld.zq.com.kotlindemo.mvp.model.bean.Article
import helloworld.zq.com.kotlindemo.mvp.model.bean.ArticleResponseBody
import helloworld.zq.com.kotlindemo.mvp.model.bean.Banner
import helloworld.zq.com.kotlindemo.mvp.model.bean.HttpResult
import helloworld.zq.com.kotlindemo.rx.SchedulerUtils
import io.reactivex.Observable

class HomeModel : CommonModel() {
    fun requestBanner() : Observable<HttpResult<List<Banner>>>{
        return RetrofitHelper.service
                .getBanners()
                .compose(SchedulerUtils.ioToMain())
    }

    fun requestTopArticles() : Observable<HttpResult<MutableList<Article>>>{
        return RetrofitHelper.service.getTopArticles()
                .compose(SchedulerUtils.ioToMain())
    }

    fun requestArticles(num : Int) : Observable<HttpResult<ArticleResponseBody>>{
        return RetrofitHelper.service.getArticles(num)
                .compose(SchedulerUtils.ioToMain())
    }
}