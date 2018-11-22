package helloworld.zq.com.kotlindemo.mvp.contract

import helloworld.zq.com.kotlindemo.base.IPresenter
import helloworld.zq.com.kotlindemo.base.IView
import helloworld.zq.com.kotlindemo.mvp.model.bean.LoginData

interface LoginContract {

    interface View : IView{
        fun loginSuccess(data: LoginData)

        fun loginFail()
    }

    interface Presenter : IPresenter<View>{
        fun loginWanKotlin(username : String,password : String)
    }
}