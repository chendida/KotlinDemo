package helloworld.zq.com.kotlindemo.ui.activity

import android.content.Intent
import android.view.View
import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.base.BaseActivity
import helloworld.zq.com.kotlindemo.constant.Constant
import helloworld.zq.com.kotlindemo.event.LoginEvent
import helloworld.zq.com.kotlindemo.mvp.contract.LoginContract
import helloworld.zq.com.kotlindemo.mvp.model.bean.LoginData
import helloworld.zq.com.kotlindemo.mvp.presenter.LoginPresenter
import helloworld.zq.com.kotlindemo.utils.DialogUtil
import helloworld.zq.com.kotlindemo.utils.Preference
import helloworld.zq.com.kotlindemo.utils.showToast
import kotlinx.android.synthetic.main.activity_login.*
import org.greenrobot.eventbus.EventBus

class LoginActivity : BaseActivity(),LoginContract.View {
    private var username : String by Preference(Constant.USERNAME_KEY,"")

    private var password : String by Preference(Constant.PASSWORD_KEY,"")

    private val mPresenter : LoginPresenter by lazy {
        LoginPresenter()
    }

    override fun useEventBus(): Boolean = false

    override fun enableNetworkTip(): Boolean = false

    override fun loginSuccess(data: LoginData) {
        showToast(getString(R.string.login_success))

        isLogin = true
        username = data.username
        password = data.password

        EventBus.getDefault().post(LoginEvent(isLogin))
        finish()
    }

    override fun loginFail() {
    }

    private val mDialog by lazy {
        DialogUtil.getWaitDialog(this,getString(R.string.login_ing))
    }

    override fun showLoading() {
        mDialog.show()
    }

    override fun hideLoading() {
        mDialog.dismiss()
    }

    override fun showError(errMsg: String) {
        showToast(errMsg)
    }

    override fun initData() {
    }

    override fun initView() {
        mPresenter.attachView(this)
        et_username.setText(username)
        btn_login.setOnClickListener(onClickListener)
        tv_sign_up.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener {
        when(it.id){
            R.id.tv_sign_up ->{
                val intent = Intent(this@LoginActivity,RegisterActivity::class.java)
                startActivity(intent)
                finish()
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
            }
            R.id.btn_login ->{
                login()
            }
        }
    }

    private fun login(){
        if (validate()){
            mPresenter.loginWanKotlin(et_username.text.toString(),et_password.text.toString())
        }
    }

    private fun validate() : Boolean{
        var validate = true
        val username = et_username.text.toString()
        val password = et_password.text.toString()

        if (username.isEmpty()){
            et_username.error = getString(R.string.username_not_empty)
            validate = false
        }

        if (password.isEmpty()){
            et_password.error = getString(R.string.password_not_empty)
            validate = false
        }
        return validate
    }

    override fun start() {
    }

    override fun attachLayoutRes(): Int = R.layout.activity_login
}
