package helloworld.zq.com.kotlindemo.mvp.presenter

import com.cxz.wanandroid.http.exception.ExceptionHandle
import com.cxz.wanandroid.http.function.RetryWithDelay
import helloworld.zq.com.kotlindemo.base.BasePresenter
import helloworld.zq.com.kotlindemo.mvp.contract.CommonContract
import helloworld.zq.com.kotlindemo.mvp.model.CommonModel

open class CommonPresenter<V : CommonContract.View> : BasePresenter<V>(),
        CommonContract.Presenter<V>{

    private val mModel : CommonModel by lazy {
        CommonModel()
    }

    override fun addCollectArticle(id: Int) {
        val disposable = mModel.addCollectArticle(id)
                .retryWhen(RetryWithDelay())
                .subscribe({results ->
                    mView?.run {
                        if (results.errorCode != 0){
                            showError(results.errorMsg)
                        }else{
                            showCollectSuccess(true)
                        }
                    }
                },{t ->
                    mView?.apply {
                        hideLoading()
                        showError(ExceptionHandle.handleException(t))
                    }
                })
        addSubscription(disposable)
    }

    override fun cancelCollectArticle(id: Int) {
        val disposable = mModel.cancelCollectArticle(id)
                .retryWhen(RetryWithDelay())
                .subscribe({results ->
                    mView?.run {
                        if (results.errorCode != 0){
                            showError(results.errorMsg)
                        }else{
                            showCancelCollectSuccess(true)
                        }
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