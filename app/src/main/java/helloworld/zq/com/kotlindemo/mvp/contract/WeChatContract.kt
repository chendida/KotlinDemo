package helloworld.zq.com.kotlindemo.mvp.contract

import helloworld.zq.com.kotlindemo.base.IPresenter
import helloworld.zq.com.kotlindemo.base.IView
import helloworld.zq.com.kotlindemo.mvp.model.bean.WXChapterBean

interface WeChatContract {
    interface View : IView{
        fun scrollToTop()

        fun showWXChapters(chapters : MutableList<WXChapterBean>)
    }

    interface Presenter : IPresenter<View>{
        fun getWXChapters()
    }
}