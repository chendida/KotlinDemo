package helloworld.zq.com.kotlindemo.mvp.presenter

import com.cxz.wanandroid.http.exception.ExceptionHandle
import com.cxz.wanandroid.http.function.RetryWithDelay
import helloworld.zq.com.kotlindemo.base.BasePresenter
import helloworld.zq.com.kotlindemo.mvp.contract.WeChatContract
import helloworld.zq.com.kotlindemo.mvp.model.WeChatModel

class WeChatPresenter : BasePresenter<WeChatContract.View>(),WeChatContract.Presenter{

    private val weChatModel : WeChatModel by lazy {
        WeChatModel()
    }

    override fun getWXChapters() {
        mView?.showLoading()
        val disposable = weChatModel.getWXChapters()
                .retryWhen(RetryWithDelay())
                .subscribe({results ->
                    mView?.apply {
                        if (results.errorCode != 0){
                            showError(results.errorMsg)
                        }else{
                            showWXChapters(results.data)
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