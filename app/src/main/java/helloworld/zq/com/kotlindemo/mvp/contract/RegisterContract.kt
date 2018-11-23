package helloworld.zq.com.kotlindemo.mvp.contract

import helloworld.zq.com.kotlindemo.base.IPresenter
import helloworld.zq.com.kotlindemo.base.IView
import helloworld.zq.com.kotlindemo.mvp.model.bean.LoginData

interface RegisterContract {
    interface View : IView{
        fun registerSuccess(data: LoginData)

        fun registerFail()
    }

    interface Presentr : IPresenter<View>{
        fun registerWanKotlin(username : String,password : String,rePassword : String)
    }
}