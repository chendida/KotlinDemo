package helloworld.zq.com.kotlindemo.mvp.contract

import helloworld.zq.com.kotlindemo.base.IPresenter
import helloworld.zq.com.kotlindemo.base.IView

/**
 * @author chenxz
 * @date 2018/8/30
 * @desc
 */
interface MainContract {

    interface View : IView {
        fun showLogoutSuccess(success: Boolean)
    }

    interface Presenter : IPresenter<View> {

        fun logout()

    }

}