package helloworld.zq.com.kotlindemo.mvp.presenter

import com.cxz.wanandroid.http.exception.ExceptionHandle
import com.cxz.wanandroid.http.function.RetryWithDelay
import helloworld.zq.com.kotlindemo.base.BasePresenter
import helloworld.zq.com.kotlindemo.mvp.contract.NavigationContract
import helloworld.zq.com.kotlindemo.mvp.model.NavigationModel

class NavigationPresenter : BasePresenter<NavigationContract.View>(),NavigationContract.Presenter {

    private val navigationModel : NavigationModel by lazy {
        NavigationModel()
    }

    override fun requestNavigationList() {
        mView?.showLoading()
        val disposable = navigationModel.getNavigationList()
                .retryWhen(RetryWithDelay())
                .subscribe({results ->
                    mView?.apply {
                        if (results.errorCode != 0){
                            showError(results.errorMsg)
                        }else{
                            setNavigationData(results.data)
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