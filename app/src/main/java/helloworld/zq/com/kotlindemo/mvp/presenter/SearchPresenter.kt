package helloworld.zq.com.kotlindemo.mvp.presenter

import android.util.Log
import com.cxz.wanandroid.http.exception.ExceptionHandle
import com.cxz.wanandroid.http.function.RetryWithDelay
import helloworld.zq.com.kotlindemo.base.BasePresenter
import helloworld.zq.com.kotlindemo.mvp.contract.SearchContract
import helloworld.zq.com.kotlindemo.mvp.model.SearchModel
import helloworld.zq.com.kotlindemo.mvp.model.bean.SearchHistoryBean
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.litepal.LitePal

class SearchPresenter : BasePresenter<SearchContract.View>(),SearchContract.Presenter{

    override fun quearHistory() {
        Log.i("save","quearHistory()")
        doAsync {
            val historyBeans = LitePal.findAll(SearchHistoryBean::class.java)
            historyBeans.reverse()
            uiThread {
                Log.i("save","size = " + historyBeans.size)
                mView?.showHistoryData(historyBeans)
            }
        }
    }

    override fun saveSearchKey(key : String) {
        Log.i("save","key = " + key)
        doAsync {
            val historyBean = SearchHistoryBean(key.trim())
            val beans = LitePal.where("key = '${key.trim()}'").find(SearchHistoryBean::class.java)
            if (beans.size == 0) {
                historyBean.save()
            } else {
                deleteById(beans[0].id)
                historyBean.save()
            }
        }
    }

    override fun deleteById(id : Long) {
        doAsync {
            LitePal.delete(SearchHistoryBean::class.java,id)
        }
    }

    override fun clearAllHistory() {
        doAsync {
            LitePal.deleteAll(SearchHistoryBean::class.java)
            uiThread {

            }
        }
    }

    override fun getHotSearchData() {
        mView?.showLoading()
        val disposable = searchModel.searchHotData()
                .retryWhen(RetryWithDelay())
                .subscribe({results ->
                    mView?.apply {
                        if (results.errorCode != 0){
                            showError(results.errorMsg)
                        }else{
                            showHotSearchData(results.data)
                        }
                        hideLoading()
                    }
                },{t ->
                    mView?.apply {
                        hideLoading()
                        showError(ExceptionHandle.handleException(t))
                    }
                })
        addSubscription(disposable)
    }

    private val searchModel : SearchModel by lazy {
        SearchModel()
    }


}