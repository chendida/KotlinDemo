package helloworld.zq.com.kotlindemo.mvp.contract

import helloworld.zq.com.kotlindemo.mvp.model.bean.ArticleResponseBody

interface KnowledgeContract {
    interface View : CommonContract.View{
        fun scrollToTop()

        fun setKnowledgeList(articles : ArticleResponseBody)
    }

    interface Presenter : CommonContract.Presenter<View>{
        fun requestKonwledgeList(page: Int,cid : Int)
    }
}