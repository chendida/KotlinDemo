package helloworld.zq.com.kotlindemo.mvp.contract

import helloworld.zq.com.kotlindemo.base.IPresenter
import helloworld.zq.com.kotlindemo.base.IView
import helloworld.zq.com.kotlindemo.mvp.model.bean.ProjectTreeBean

interface ProjectContract {
    interface View : IView{
        fun scrollToTop()

        fun setProjectTree(list: List<ProjectTreeBean>)
    }

    interface Presenter : IPresenter<View>{
        fun requestProjectTree()
    }
}