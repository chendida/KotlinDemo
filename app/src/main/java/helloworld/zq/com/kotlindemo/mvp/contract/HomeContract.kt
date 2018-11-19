package helloworld.zq.com.kotlindemo.mvp.contract

import helloworld.zq.com.kotlindemo.mvp.model.bean.ArticleResponseBody
import helloworld.zq.com.kotlindemo.mvp.model.bean.Banner

interface HomeContract {
    interface View : CommonContract.View{
        fun scrollToTop()

        fun setBanner(banners : List<Banner>)

        fun setArticles(articles : ArticleResponseBody)
    }

    interface Presenter : CommonContract.Presenter<View>{
        fun requestHomeData()

        fun requestBanner()

        fun requestArticles(num : Int)
    }
}