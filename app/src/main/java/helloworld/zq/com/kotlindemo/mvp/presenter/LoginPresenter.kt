package helloworld.zq.com.kotlindemo.mvp.presenter

import com.cxz.wanandroid.http.exception.ExceptionHandle
import com.cxz.wanandroid.http.function.RetryWithDelay
import helloworld.zq.com.kotlindemo.base.BasePresenter
import helloworld.zq.com.kotlindemo.mvp.contract.LoginContract
import helloworld.zq.com.kotlindemo.mvp.model.LoginModel

class LoginPresenter : BasePresenter<LoginContract.View>(),LoginContract.Presenter {

    override fun loginWanKotlin(username: String, password: String) {
        mView?.showLoading()
        val disposable = loginModel.loginWanKotlin(username,password)
                .retryWhen(RetryWithDelay())
                .subscribe({results ->
                    mView?.apply {
                        if (results.errorCode != 0){
                            showError(results.errorMsg)
                            loginFail()
                        }else{
                            loginSuccess(results.data)
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

    private val loginModel : LoginModel by lazy {
        LoginModel()
    }
}