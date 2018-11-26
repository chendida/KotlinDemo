package helloworld.zq.com.kotlindemo.mvp.contract

import helloworld.zq.com.kotlindemo.base.IPresenter
import helloworld.zq.com.kotlindemo.base.IView
import helloworld.zq.com.kotlindemo.mvp.model.bean.HotSearchBean
import helloworld.zq.com.kotlindemo.mvp.model.bean.SearchHistoryBean

interface SearchContract {
    interface View : IView{
        fun showHistoryData(historyBeans : MutableList<SearchHistoryBean>)

        fun showHotSearchData(hotSearchDatas : MutableList<HotSearchBean>)
    }

    interface Presenter : IPresenter<View>{
        fun quearHistory()

        fun saveSearchKey(key : String)

        fun deleteById(id : Long)

        fun clearAllHistory()

        fun getHotSearchData()
    }
}