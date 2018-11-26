package helloworld.zq.com.kotlindemo.mvp.presenter

import com.cxz.wanandroid.http.exception.ExceptionHandle
import com.cxz.wanandroid.http.function.RetryWithDelay
import helloworld.zq.com.kotlindemo.mvp.contract.SearchListContract
import helloworld.zq.com.kotlindemo.mvp.model.SearchListModel

class SearchListPresenter : CommonPresenter<SearchListContract.View>(),SearchListContract.Presenter {

    private val searchListModel : SearchListModel by lazy {
        SearchListModel()
    }

    override fun queryBySearchKey(page: Int, key: String) {
        if (page == 0)
            mView?.showLoading()
        val disposable = searchListModel.queryBySearchKey(page,key)
                .retryWhen(RetryWithDelay())
                .subscribe({results ->
                    mView?.apply {
                        if (results.errorCode != 0){
                            showError(results.errorMsg)
                        }else{
                            showArticles(results.data)
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
}