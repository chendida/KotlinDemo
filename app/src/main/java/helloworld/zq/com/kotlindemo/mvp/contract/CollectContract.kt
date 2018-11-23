package helloworld.zq.com.kotlindemo.mvp.contract

import helloworld.zq.com.kotlindemo.base.IPresenter
import helloworld.zq.com.kotlindemo.base.IView
import helloworld.zq.com.kotlindemo.mvp.model.bean.CollectionArticle
import helloworld.zq.com.kotlindemo.mvp.model.bean.CollectionResponseBody

interface CollectContract {

    interface View : IView{
        fun setCollectList(articles : CollectionResponseBody<CollectionArticle>)

        fun showRemoveCollectSuccess(success : Boolean)
    }

    interface Presenter : IPresenter<View>{
        fun getCollectList(page : Int)

        fun removeCollectArticle(id : Int,originId : Int)
    }
}