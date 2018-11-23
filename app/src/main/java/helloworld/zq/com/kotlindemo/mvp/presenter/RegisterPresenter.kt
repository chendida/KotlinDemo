package helloworld.zq.com.kotlindemo.mvp.presenter

import com.cxz.wanandroid.http.exception.ExceptionHandle
import com.cxz.wanandroid.http.function.RetryWithDelay
import helloworld.zq.com.kotlindemo.base.BasePresenter
import helloworld.zq.com.kotlindemo.mvp.contract.RegisterContract
import helloworld.zq.com.kotlindemo.mvp.model.RegisterModel

class RegisterPresenter : BasePresenter<RegisterContract.View>(),RegisterContract.Presentr {

    private val registerModel : RegisterModel by lazy {
        RegisterModel()
    }

    override fun registerWanKotlin(username: String, password: String, rePassword: String) {
        mView?.showLoading()
        val disposable = registerModel.registerWanKotlin(username,password,rePassword)
                .retryWhen(RetryWithDelay())
                .subscribe({results ->
                    mView?.apply {
                        if (results.errorCode != 0){
                            showError(results.errorMsg)
                            registerFail()
                        }else{
                            registerSuccess(results.data)
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