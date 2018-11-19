package helloworld.zq.com.kotlindemo.mvp.contract

import helloworld.zq.com.kotlindemo.base.IPresenter
import helloworld.zq.com.kotlindemo.base.IView

/**
 * 公共的View接口
 */
interface CommonContract {

    interface View : IView{
        fun showCollectSuccess(success : Boolean)

        fun showCancelCollectSuccess(success: Boolean)
    }

    interface Presenter<in V : View> : IPresenter<V>{
        fun addCollectArticle(id : Int)

        fun cancelCollectArticle(id : Int)
    }
}