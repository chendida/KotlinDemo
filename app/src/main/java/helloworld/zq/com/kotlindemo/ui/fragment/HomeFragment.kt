package helloworld.zq.com.kotlindemo.ui.fragment

import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.base.BaseFragment
import helloworld.zq.com.kotlindemo.mvp.contract.HomeContract
import helloworld.zq.com.kotlindemo.mvp.model.bean.ArticleResponseBody
import helloworld.zq.com.kotlindemo.mvp.model.bean.Banner

/**
 * 首页
 */
class HomeFragment : BaseFragment(),HomeContract.View {
    override fun scrollToTop() {
    }

    override fun setBanner(banners: List<Banner>) {
    }

    override fun setArticles(articles: ArticleResponseBody) {
    }

    override fun showCollectSuccess(success: Boolean) {
    }

    override fun showCancelCollectSuccess(success: Boolean) {
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showError(errMsg: String) {
    }

    companion object {
        fun getInstance() : HomeFragment = HomeFragment()
    }
    override fun attachLayoutRes(): Int = R.layout.fragment_home

    override fun initView() {
    }

    override fun lazyLoad() {
    }
}