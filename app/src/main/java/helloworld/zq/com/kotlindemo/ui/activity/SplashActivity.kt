package helloworld.zq.com.kotlindemo.ui.activity

import android.content.Intent
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.base.BaseActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity() {
    private var alphaAnimation : AlphaAnimation? = null

    override fun useEventBus(): Boolean = false

    override fun enableNetworkTip(): Boolean = false

    override fun initData() {
    }

    override fun initView() {
        alphaAnimation = AlphaAnimation(0.3f,1.0f)
        alphaAnimation?.run {
            duration = 2000
            setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    jumpToMain()
                }

                override fun onAnimationStart(animation: Animation?) {
                }

            })
        }
        layout_splash.startAnimation(alphaAnimation)
    }

    override fun start() {
    }

    override fun attachLayoutRes(): Int = R.layout.activity_splash

    private fun jumpToMain(){
        startActivity(Intent(this,MainActivity::class.java))
        finish()
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
    }
}
