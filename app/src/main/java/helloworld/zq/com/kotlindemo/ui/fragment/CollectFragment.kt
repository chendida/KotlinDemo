package helloworld.zq.com.kotlindemo.ui.fragment

import android.os.Bundle
import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.base.BaseFragment

class CollectFragment : BaseFragment() {

    companion object {
        fun getInstance(bundle: Bundle) : CollectFragment{
            val fragment = CollectFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun attachLayoutRes(): Int = R.layout.fragment_refresh_layout

    override fun initView() {
    }

    override fun lazyLoad() {
    }
}