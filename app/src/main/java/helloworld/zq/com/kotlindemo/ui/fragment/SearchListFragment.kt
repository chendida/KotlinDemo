package helloworld.zq.com.kotlindemo.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import helloworld.zq.com.kotlindemo.App
import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.adapter.HomeAdapter
import helloworld.zq.com.kotlindemo.base.BaseFragment
import helloworld.zq.com.kotlindemo.constant.Constant
import helloworld.zq.com.kotlindemo.mvp.contract.SearchListContract
import helloworld.zq.com.kotlindemo.mvp.model.bean.Article
import helloworld.zq.com.kotlindemo.mvp.model.bean.ArticleResponseBody
import helloworld.zq.com.kotlindemo.mvp.presenter.SearchListPresenter
import helloworld.zq.com.kotlindemo.ui.activity.ContentActivity
import helloworld.zq.com.kotlindemo.ui.activity.LoginActivity
import helloworld.zq.com.kotlindemo.utils.NetWorkUtil
import helloworld.zq.com.kotlindemo.utils.showSnackMsg
import helloworld.zq.com.kotlindemo.utils.showToast
import helloworld.zq.com.kotlindemo.widget.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_refresh_layout.*

class SearchListFragment : BaseFragment(),SearchListContract.View {

    private var mKey = ""

    private val mPresenter by lazy {
        SearchListPresenter()
    }

    private val datas = mutableListOf<Article>()

    private val searchListAdapter : HomeAdapter by lazy {
        HomeAdapter(activity,datas)
    }

    private val linearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }

    private val recyclerViewItemDecoration by lazy {
        activity?.let {
            SpaceItemDecoration(it)
        }
    }

    private var isRefresh = true


    override fun showArticles(articles: ArticleResponseBody) {
        articles.datas.let {
            searchListAdapter.run {
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

    override fun showLoading() {
        swipeRefreshLayout.isRefreshing = isRefresh
    }

    override fun hideLoading() {
        swipeRefreshLayout?.isRefreshing = false
        if (isRefresh){
            searchListAdapter.run {
                setEnableLoadMore(true)
            }
        }
    }

    override fun showError(errMsg: String) {
        searchListAdapter.run {
            if (isRefresh)
                setEnableLoadMore(true)
            else
                loadMoreFail()
        }
        showToast(errMsg)
    }

    companion object {
        fun getInstance(bundle: Bundle) : SearchListFragment{
            val fragment = SearchListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun attachLayoutRes(): Int = R.layout.fragment_search_list

    override fun initView() {
        mPresenter.attachView(this)
        mKey = arguments?.getString(Constant.SEARCH_KEY) ?: ""

        swipeRefreshLayout.run {
            isRefreshing = true
            setOnRefreshListener(onRefreshListener)
        }

        recyclerView.run {
            adapter = searchListAdapter
            layoutManager = linearLayoutManager
            itemAnimator = DefaultItemAnimator()
            recyclerViewItemDecoration?.let {
                addItemDecoration(it)
            }
        }

        searchListAdapter.run {
            setOnLoadMoreListener(onRequestLoadMoreListener,recyclerView)
            onItemClickListener = this@SearchListFragment.onItemClickListener
            onItemChildClickListener = this@SearchListFragment.onItemChildClickListener
            setEmptyView(R.layout.fragment_empty_layout)
        }
    }

    private val onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
        if (datas.size != 0){
            val data = datas[position]
            when(view.id){
                R.id.iv_like ->{
                    if (isLogin) {
                        if (!NetWorkUtil.isNetworkAvailable(App.context)) {
                            showSnackMsg(resources.getString(R.string.no_network))
                            return@OnItemChildClickListener
                        }
                        val collect = data.collect
                        data.collect = !collect
                        searchListAdapter.setData(position, data)
                        if (collect) {
                            mPresenter.cancelCollectArticle(data.id)
                        } else {
                            mPresenter.addCollectArticle(data.id)
                        }
                    } else {
                        Intent(activity, LoginActivity::class.java).run {
                            startActivity(this)
                        }
                        showToast(resources.getString(R.string.login_tint))
                    }
                }
            }
        }
    }

    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
        if (datas.size != 0){
            val data = datas[position]
            Intent(activity, ContentActivity::class.java).run {
                putExtra(Constant.CONTENT_URL_KEY, data.link)
                putExtra(Constant.CONTENT_TITLE_KEY, data.title)
                putExtra(Constant.CONTENT_ID_KEY, data.id)
                startActivity(this)
            }
        }
    }

    private val onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        isRefresh = false
        swipeRefreshLayout.isRefreshing = false
        val page = searchListAdapter.data.size / 20
        mPresenter.queryBySearchKey(page,mKey)
    }

    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        isRefresh = true
        searchListAdapter.setEnableLoadMore(false)
        mPresenter.queryBySearchKey(0,mKey)
    }

    override fun lazyLoad() {
        mPresenter.queryBySearchKey(0,mKey)
    }

    override fun showCollectSuccess(success: Boolean) {
        if (success){
            showToast(getString(R.string.collect_success))
        }
    }

    override fun showCancelCollectSuccess(success: Boolean) {
        if (success){
            showToast(getString(R.string.cancel_collect_success))
        }
    }
}