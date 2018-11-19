package helloworld.zq.com.kotlindemo.ui.fragment

import android.view.View
import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.adapter.HomeAdapter
import helloworld.zq.com.kotlindemo.base.BaseFragment
import helloworld.zq.com.kotlindemo.mvp.contract.HomeContract
import helloworld.zq.com.kotlindemo.mvp.model.bean.Article
import helloworld.zq.com.kotlindemo.mvp.model.bean.ArticleResponseBody
import helloworld.zq.com.kotlindemo.mvp.model.bean.Banner
import helloworld.zq.com.kotlindemo.mvp.presenter.HomePresenter
import helloworld.zq.com.kotlindemo.widget.SpaceItemDecoration

/**
 * 首页
 */
class HomeFragment : BaseFragment(),HomeContract.View {

    /**
     * Presenter
     */
    private val mPresenter : HomePresenter by lazy {
        HomePresenter()
    }

    /**
     * datas
     */
    private val datas = mutableListOf<Article>()

    /**
     * banner datas
     */
    private lateinit var bannerDatas : ArrayList<Banner>

    /**
     * banner view
     */
    private var bannerView :View? = null

    /**
     * RecyclerView Divider
     */
    private val recyclerViewItemDecoration by lazy {
        activity?.let {
            SpaceItemDecoration(it)
        }
    }

    /**
     * Home Adapter
     */
    private val homeAdapter : HomeAdapter by lazy {
        HomeAdapter(activity,datas)
    }

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