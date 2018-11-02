package helloworld.zq.com.kotlindemo.base

import android.support.v7.app.AppCompatActivity
import helloworld.zq.com.kotlindemo.constant.Constant
import helloworld.zq.com.kotlindemo.utils.Preference

abstract class BaseActivity : AppCompatActivity() {
    /**
     * check login
     */
    //protected var isLogin: Boolean by Preference(Constant.LOGIN_KEY, false)
}