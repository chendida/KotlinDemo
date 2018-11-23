package helloworld.zq.com.kotlindemo.ui.fragment

import android.os.Bundle
import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.base.BaseFragment

class AboutFragment : BaseFragment() {
    override fun lazyLoad() {
    }

    companion object {
        fun getInstance(bundle: Bundle) : AboutFragment{
            val fragment = AboutFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun attachLayoutRes(): Int = R.layout.fragment_about


    override fun initView() {
    }
}