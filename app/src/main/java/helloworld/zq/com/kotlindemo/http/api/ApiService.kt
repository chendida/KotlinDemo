package helloworld.zq.com.kotlindemo.http.api

import helloworld.zq.com.kotlindemo.mvp.model.bean.Article
import helloworld.zq.com.kotlindemo.mvp.model.bean.ArticleResponseBody
import helloworld.zq.com.kotlindemo.mvp.model.bean.Banner
import helloworld.zq.com.kotlindemo.mvp.model.bean.HttpResult
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    /**
     * 获取轮播图
     * http://www.wanandroid.com/banner/json
     */
    @GET("banner/json")
    fun getBanners() : Observable<HttpResult<List<Banner>>>

    /**
     * 退出登录
     * http://www.wanandroid.com/user/logout/json
     */
    @GET("user/logout/json")
    fun logout(): Observable<HttpResult<Any>>


    /**
     * 收藏站内文章
     * http://www.wanandroid.com/lg/collect/1165/json
     *
     * @param id  article id
     */
    @POST("lg/collect/{id}/json")
    fun addCollectArticle(@Path("id") id : Int) : Observable<HttpResult<Any>>

    /**
     * 文章列表中取消收藏文章
     * http://www.wanandroid.com/lg/uncollect_originId/2333/json
     *
     * @param id
     */
    @POST("lg/uncollect_originId/{id}/json")
    fun cancelCollectArticle(@Path("id") id : Int) : Observable<HttpResult<Any>>


    /**
     * 获取首页置顶文章列表
     * http://www.wanandroid.com/article/top/json
     */
    @GET("article/top/json")
    fun getTopArticles() : Observable<HttpResult<MutableList<Article>>>

    /**
     * 获取文章列表
     * http://www.wanandroid.com/article/list/0/json
     *
     * @param pageNum
     */
    @GET("article/list/{pageNum}/json")
    fun getArticles(@Path("pageNum") pageNum : Int) : Observable<HttpResult<ArticleResponseBody>>
}