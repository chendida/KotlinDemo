package helloworld.zq.com.kotlindemo.ui.fragment

import android.os.Bundle
import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.base.BaseFragment

class SettingFragment : BaseFragment() {

    companion object {
        fun getInstance(bundle: Bundle) : SettingFragment{
            val fragment = SettingFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun attachLayoutRes(): Int = R.layout.fragment_setting

    override fun initView() {
    }

    override fun lazyLoad() {
    }
}