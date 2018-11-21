package helloworld.zq.com.kotlindemo.mvp.contract

import helloworld.zq.com.kotlindemo.mvp.model.bean.ArticleResponseBody

interface ProjectListContract {
    interface View : CommonContract.View{
        fun scrollToTop()

        fun setProjectList(articles : ArticleResponseBody)
    }

    interface Presenter : CommonContract.Presenter<View>{
        fun requestProjectList(page : Int,cid : Int)
    }
}