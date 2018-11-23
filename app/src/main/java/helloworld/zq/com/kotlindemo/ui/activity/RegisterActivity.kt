package helloworld.zq.com.kotlindemo.ui.activity

import android.content.Intent
import android.view.View
import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.base.BaseActivity
import helloworld.zq.com.kotlindemo.constant.Constant
import helloworld.zq.com.kotlindemo.event.LoginEvent
import helloworld.zq.com.kotlindemo.mvp.contract.RegisterContract
import helloworld.zq.com.kotlindemo.mvp.model.bean.LoginData
import helloworld.zq.com.kotlindemo.mvp.presenter.RegisterPresenter
import helloworld.zq.com.kotlindemo.utils.DialogUtil
import helloworld.zq.com.kotlindemo.utils.Preference
import helloworld.zq.com.kotlindemo.utils.showToast
import kotlinx.android.synthetic.main.activity_register.*
import org.greenrobot.eventbus.EventBus

class RegisterActivity : BaseActivity(),RegisterContract.View {

    private var username : String by Preference(Constant.USERNAME_KEY,"")

    private var password : String by Preference(Constant.PASSWORD_KEY,"")

    /**
     * Presenter
     */
    private val mPresenter : RegisterPresenter by lazy {
        RegisterPresenter()
    }

    private val mDialog by lazy {
        DialogUtil.getWaitDialog(this,getString(R.string.register_ing))
    }


    override fun registerSuccess(data: LoginData) {
        showToast(getString(R.string.register_success))

        isLogin = true
        username = data.username
        password = data.password

        EventBus.getDefault().post(LoginEvent(true))
        finish()
    }

    override fun registerFail() {
        isLogin = false
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
        btn_register.setOnClickListener(onClickListener)
        tv_sign_in.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener {
        when(it.id){
            R.id.btn_register ->{
                register()
            }
            R.id.tv_sign_in ->{
                Intent(this@RegisterActivity,LoginActivity::class.java).run {
                    startActivity(this)
                }
                finish()
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
            }
        }
    }

    private fun register(){
        if (validate()){
            mPresenter.registerWanKotlin(et_username.text.toString(),et_password.text.toString()
            ,et_password2.text.toString())
        }
    }

    private fun validate() : Boolean{
        var valid = true
        val username : String = et_username.text.toString()
        val password : String = et_password.text.toString()
        val repassword : String  = et_password2.text.toString()
        if (username.isEmpty()){
            et_username.error = getString(R.string.username_not_empty)
            valid = false
        }else if (password.isEmpty()){
            et_password.error = getString(R.string.password_not_empty)
            valid = false
        }else if (repassword.isEmpty()){
            et_password2.error = getString(R.string.repassword_not_empty)
            valid = false
        }else if (password != repassword){
            et_password2.error = getString(R.string.password_can_not_match)
            valid = false
        }
        return valid
    }
    override fun start() {
    }

    override fun attachLayoutRes(): Int = R.layout.activity_register
}
