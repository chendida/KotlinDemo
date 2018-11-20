package helloworld.zq.com.kotlindemo.ui.fragment

import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.base.BaseFragment
import helloworld.zq.com.kotlindemo.mvp.contract.NavigationContract
import helloworld.zq.com.kotlindemo.mvp.model.bean.NavigationBean

/**
 * 导航
 */
class NavigationFragment : BaseFragment(),NavigationContract.View {
    override fun setNavigationData(list: List<NavigationBean>) {
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showError(errMsg: String) {
    }

    companion object {
        fun getInstance() : NavigationFragment = NavigationFragment()
    }
    override fun attachLayoutRes(): Int = R.layout.fragment_navigation

    override fun initView() {
    }

    override fun lazyLoad() {
    }
}