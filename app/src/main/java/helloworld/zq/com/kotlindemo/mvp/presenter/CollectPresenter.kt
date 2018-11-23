package helloworld.zq.com.kotlindemo.mvp.presenter

import com.cxz.wanandroid.http.exception.ExceptionHandle
import com.cxz.wanandroid.http.function.RetryWithDelay
import helloworld.zq.com.kotlindemo.base.BasePresenter
import helloworld.zq.com.kotlindemo.mvp.contract.CollectContract
import helloworld.zq.com.kotlindemo.mvp.model.CollectModel

class CollectPresenter : BasePresenter<CollectContract.View>(),CollectContract.Presenter {

    private val collectModel : CollectModel by lazy {
        CollectModel()
    }

    override fun getCollectList(page: Int) {
        mView?.showLoading()
        val disposable = collectModel.getCollectList(page)
                .retryWhen(RetryWithDelay())
                .subscribe({results ->
                    mView?.apply {
                        if (results.errorCode != 0){
                            showError(results.errorMsg)
                        }else{
                            setCollectList(results.data)
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

    override fun removeCollectArticle(id: Int, originId: Int) {
        mView?.showLoading()
        val disposable = collectModel.removeCollectionArticle(id,originId)
                .retryWhen(RetryWithDelay())
                .subscribe({results ->
                    mView?.apply {
                        if (results.errorCode != 0){
                            showError(results.errorMsg)
                            showRemoveCollectSuccess(false)
                        }else{
                            showRemoveCollectSuccess(true)
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