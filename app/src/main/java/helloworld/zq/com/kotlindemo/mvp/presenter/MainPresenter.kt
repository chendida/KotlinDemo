package helloworld.zq.com.kotlindemo.mvp.presenter

import com.cxz.wanandroid.http.exception.ExceptionHandle
import helloworld.zq.com.kotlindemo.base.BasePresenter
import helloworld.zq.com.kotlindemo.mvp.contract.MainContract
import helloworld.zq.com.kotlindemo.mvp.model.MainModel
import io.reactivex.internal.util.ExceptionHelper

class MainPresenter : BasePresenter<MainContract.View>(),MainContract.Presenter {

    private val mainModel : MainModel by lazy {
        MainModel()
    }
    override fun logout() {
        mView?.showLoading()
        val disposable = mainModel.logout()
                .subscribe({results ->
                    mView?.apply {
                        if (results.errorCode != 0){
                            showError(results.errorMsg)
                        }else{
                            showLogoutSuccess(success = true)
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