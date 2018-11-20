package helloworld.zq.com.kotlindemo.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import cn.bingoogolapple.bgabanner.BGABanner
import com.chad.library.adapter.base.BaseQuickAdapter
import helloworld.zq.com.kotlindemo.App
import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.adapter.HomeAdapter
import helloworld.zq.com.kotlindemo.base.BaseFragment
import helloworld.zq.com.kotlindemo.constant.Constant
import helloworld.zq.com.kotlindemo.mvp.contract.HomeContract
import helloworld.zq.com.kotlindemo.mvp.model.bean.Article
import helloworld.zq.com.kotlindemo.mvp.model.bean.ArticleResponseBody
import helloworld.zq.com.kotlindemo.mvp.model.bean.Banner
import helloworld.zq.com.kotlindemo.mvp.presenter.HomePresenter
import helloworld.zq.com.kotlindemo.ui.activity.ContentActivity
import helloworld.zq.com.kotlindemo.ui.activity.LoginActivity
import helloworld.zq.com.kotlindemo.utils.ImageLoader
import helloworld.zq.com.kotlindemo.utils.NetWorkUtil
import helloworld.zq.com.kotlindemo.utils.showSnackMsg
import helloworld.zq.com.kotlindemo.utils.showToast
import helloworld.zq.com.kotlindemo.widget.SpaceItemDecoration
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_home_banner.view.*
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
    private var bannerView: View? = null

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

    /**
     * Banner Adapter
     */
    private val bannerAdapter : BGABanner.Adapter<ImageView,String> by lazy {
        BGABanner.Adapter<ImageView,String>{bgaBanner,imageView,feedImageUrl,position ->
            ImageLoader.load(activity,feedImageUrl,imageView)
        }
    }

    /**
     * LinerLayoutManager
     */
    private val linearLayoutManager : LinearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }

    /**
     * isRefresh
     */
    private var isRefresh = true

    override fun scrollToTop() {
        recyclerView.run {
            if (linearLayoutManager.findFirstVisibleItemPosition() > 20){
                scrollToPosition(0)
            }else{
                smoothScrollToPosition(0)
            }
        }
    }

    @SuppressLint("CheckResult")
    override fun setBanner(banners: List<Banner>) {
        bannerDatas = banners as ArrayList<Banner>
        val bannerFeedList = ArrayList<String>()
        val bannerTitleList = ArrayList<String>()
        Observable.fromIterable(banners)
                .subscribe { list ->
                    bannerFeedList.add(list.imagePath)
                    bannerTitleList.add(list.title)
                }
        bannerView?.banner?.run {
            setAutoPlayAble(bannerFeedList.size > 1)
            setData(bannerFeedList,bannerTitleList)
            setAdapter(bannerAdapter)
        }
    }

    /**
     * bannerClickListener
     */
    private val bannerDelegate = BGABanner.Delegate<ImageView,String>{banner, itemView, model, position ->
        if (bannerDatas.size > 0){
            val data = bannerDatas[position]
            Intent(activity,ContentActivity::class.java).run {
                putExtra(Constant.CONTENT_URL_KEY, data.url)
                putExtra(Constant.CONTENT_TITLE_KEY, data.title)
                putExtra(Constant.CONTENT_ID_KEY, data.id)
                startActivity(this)
            }
        }
    }

    override fun setArticles(articles: ArticleResponseBody) {
        articles.datas.let {
            homeAdapter.run {
                if (isRefresh){
                    replaceData(it)
                }else{
                    addData(it)
                }
                val size = it.size
                if (size < articles.size){
                    loadMoreEnd(isRefresh)
                }else{
                    loadMoreComplete()
                }
            }
        }
    }

    override fun showCollectSuccess(success: Boolean) {
        if (success) {
            showToast(resources.getString(R.string.collect_success))
        }
    }

    override fun showCancelCollectSuccess(success: Boolean) {
        if (success) {
            showToast(resources.getString(R.string.cancel_collect_success))
        }
    }

    override fun showLoading() {
        swipeRefreshLayout.isRefreshing = isRefresh
    }

    override fun hideLoading() {
        swipeRefreshLayout?.isRefreshing = false
        if (isRefresh){
            homeAdapter.run {
                setEnableLoadMore(true)
            }
        }
    }

    override fun showError(errMsg: String) {
        homeAdapter.run {
            if (isRefresh)
                setEnableLoadMore(true)
            else
                loadMoreFail()
        }
        showToast(errMsg)
    }

    companion object {
        fun getInstance() : HomeFragment = HomeFragment()
    }
    override fun attachLayoutRes(): Int = R.layout.fragment_home

    override fun initView() {
        mPresenter.attachView(this)

        swipeRefreshLayout.run {
            isRefreshing = true
            setOnRefreshListener(onRefreshListener)
        }

        recyclerView.run {
            layoutManager = linearLayoutManager
            adapter = homeAdapter
            itemAnimator = DefaultItemAnimator()
            recyclerViewItemDecoration?.let { addItemDecoration(it) }
        }

        bannerView = layoutInflater.inflate(R.layout.item_home_banner,null)
        bannerView?.banner?.run {
            setDelegate(bannerDelegate)
        }

        homeAdapter.run {
            bindToRecyclerView(recyclerView)
            setOnLoadMoreListener(onRequestLoadMoreListener,recyclerView)
            onItemClickListener = this@HomeFragment.onItemClickListener
            onItemChildClickListener = this@HomeFragment.onItemChildClickListener
            setEmptyView(R.layout.fragment_empty_layout)
            addHeaderView(bannerView)
        }
    }


    /**
     * onItemChildClickListener
     */
    private val onItemChildClickListener =
            BaseQuickAdapter.OnItemChildClickListener{_,view,position ->
                if (datas.size != 0){
                    val data = datas[position]
                    when(view.id){
                        R.id.iv_like ->{
                            if (isLogin){
                                if (!NetWorkUtil.isNetworkAvailable(App.context)){
                                    showSnackMsg(resources.getString(R.string.no_network))
                                    return@OnItemChildClickListener
                                }
                                val collect = data.collect
                                data.collect = !collect
                                homeAdapter.setData(position,data)
                                if (collect){
                                    mPresenter.cancelCollectArticle(data.id)
                                }else{
                                    mPresenter.addCollectArticle(data.id)
                                }
                            }else{
                                Intent(activity,LoginActivity::class.java).run {
                                    startActivity(this)
                                }
                                showToast(resources.getString(R.string.login_tint))
                            }
                        }
                    }
                }
    }

    /**
     * onItemClickListener
     */
    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener{
        _,_,position->
        if (datas.size != 0){
            val data = datas[position]
            Intent(activity,ContentActivity::class.java).run {
                putExtra(Constant.CONTENT_URL_KEY, data.link)
                putExtra(Constant.CONTENT_TITLE_KEY, data.title)
                putExtra(Constant.CONTENT_ID_KEY, data.id)
                startActivity(this)
            }
        }
    }

    /**
     * onRequestLoadMoreListener
     */
    private val onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        isRefresh = false
        swipeRefreshLayout.isRefreshing = false
        val page = homeAdapter.data.size / 20
        mPresenter.requestArticles(page)
    }

    /**
     * onRefreshListener
     */
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener{
        isRefresh = true
        homeAdapter.setEnableLoadMore(false)
        mPresenter.requestHomeData()
    }

    override fun lazyLoad() {
        mPresenter.requestHomeData()
    }
}