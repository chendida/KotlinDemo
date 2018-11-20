package helloworld.zq.com.kotlindemo.mvp.contract

import helloworld.zq.com.kotlindemo.base.IPresenter
import helloworld.zq.com.kotlindemo.base.IView
import helloworld.zq.com.kotlindemo.mvp.model.bean.KnowledgeTreeBody

interface KnowledgeTreeContract {
    interface View : IView {
        fun scrollToTop()

        fun setKnowledgeTree(list: List<KnowledgeTreeBody>)
    }

    interface Presenter : IPresenter<View>{
        fun requestKnowledgeTree()
    }
}