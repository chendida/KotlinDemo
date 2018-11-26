package helloworld.zq.com.kotlindemo.mvp.contract

import helloworld.zq.com.kotlindemo.mvp.model.bean.ArticleResponseBody

interface SearchListContract {
    interface View : CommonContract.View{
        fun showArticles(articles : ArticleResponseBody)
    }

    interface Presenter : CommonContract.Presenter<View>{
        fun queryBySearchKey(page : Int,key : String)
    }
}