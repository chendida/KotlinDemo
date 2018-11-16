package helloworld.zq.com.kotlindemo.ui.fragment

import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.base.BaseFragment

/**
 * 首页
 */
class HomeFragment : BaseFragment() {
    companion object {
        fun getInstance() : HomeFragment = HomeFragment()
    }
    override fun attachLayoutRes(): Int = R.layout.fragment_home

    override fun initView() {
    }

    override fun lazyLoad() {
    }
}