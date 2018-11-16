package helloworld.zq.com.kotlindemo.ui.fragment

import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.base.BaseFragment

/**
 * 导航
 */
class NavigationFragment : BaseFragment() {
    companion object {
        fun getInstance() : NavigationFragment = NavigationFragment()
    }
    override fun attachLayoutRes(): Int = R.layout.fragment_navigation

    override fun initView() {
    }

    override fun lazyLoad() {
    }
}