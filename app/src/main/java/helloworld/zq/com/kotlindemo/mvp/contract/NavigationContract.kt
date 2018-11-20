package helloworld.zq.com.kotlindemo.mvp.contract

import helloworld.zq.com.kotlindemo.base.IPresenter
import helloworld.zq.com.kotlindemo.base.IView
import helloworld.zq.com.kotlindemo.mvp.model.bean.NavigationBean

interface NavigationContract {
    interface View : IView{
        fun setNavigationData(list: List<NavigationBean>)
    }

    interface Presenter : IPresenter<View>{
        fun requestNavigationList()
    }
}