package helloworld.zq.com.kotlindemo.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import helloworld.zq.com.kotlindemo.App
import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.adapter.ProjectListAdapter
import helloworld.zq.com.kotlindemo.base.BaseFragment
import helloworld.zq.com.kotlindemo.constant.Constant
import helloworld.zq.com.kotlindemo.mvp.contract.ProjectListContract
import helloworld.zq.com.kotlindemo.mvp.model.bean.Article
import helloworld.zq.com.kotlindemo.mvp.model.bean.ArticleResponseBody
import helloworld.zq.com.kotlindemo.mvp.presenter.ProjectListPresenter
import helloworld.zq.com.kotlindemo.ui.activity.ContentActivity
import helloworld.zq.com.kotlindemo.ui.activity.LoginActivity
import helloworld.zq.com.kotlindemo.utils.NetWorkUtil
import helloworld.zq.com.kotlindemo.utils.showSnackMsg
import helloworld.zq.com.kotlindemo.utils.showToast
import helloworld.zq.com.kotlindemo.widget.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_refresh_layout.*

class ProjectListFragment : BaseFragment(),ProjectListContract.View {

    private val mPresenter : ProjectListPresenter by lazy {
        ProjectListPresenter()
    }

    private val datas = mutableListOf<Article>()

    private var cid : Int = -1

    private var isRefresh = true

    private val projectAdapter : ProjectListAdapter by lazy{
        ProjectListAdapter(activity,datas)
    }

    private val linearLayoutManager : LinearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }
    override fun setProjectList(articles: ArticleResponseBody) {
        articles.datas.let {
            projectAdapter.run {
                if (isRefresh)
                    replaceData(it)
                else
                    addData(it)
                val size = it.size
                if (size < articles.size){
                    loadMoreEnd(isRefresh)
                }else{
                    loadMoreComplete()
                }
            }
        }
    }

    override fun scrollToTop() {
        recyclerView.run {
            if (linearLayoutManager.findFirstVisibleItemPosition() > 20){
                scrollToPosition(0)
            }else{
                smoothScrollToPosition(0)
            }
        }
    }

    override fun showCollectSuccess(success: Boolean) {
        if (success){
            showToast(resources.getString(R.string.collect_success))
        }
    }

    override fun showCancelCollectSuccess(success: Boolean) {
        if (success){
            showToast(resources.getString(R.string.cancel_collect_success))
        }
    }

    override fun showLoading() {
        swipeRefreshLayout.isRefreshing = isRefresh
    }

    override fun hideLoading() {
        swipeRefreshLayout?.isRefreshing = false
        if (isRefresh){
            projectAdapter.run {
                setEnableLoadMore(true)
            }
        }
    }

    override fun showError(errMsg: String) {
        projectAdapter.run {
            if (isRefresh)
                setEnableLoadMore(true)
            else
                loadMoreFail()
        }
        showToast(errMsg)
    }


    override fun attachLayoutRes(): Int = R.layout.fragment_refresh_layout

    companion object {
        fun getInstance(cid : Int) : ProjectListFragment{
            val fragment = ProjectListFragment()
            val args = Bundle()
            args.putInt(Constant.CONTENT_CID_KEY,cid)
            fragment.arguments = args
            return fragment
        }
    }

    private val recyclerViewItemDecoration by lazy {
        activity?.let { SpaceItemDecoration(it) }
    }

    override fun initView() {
        mPresenter.attachView(this)

        cid = arguments!!.getInt(Constant.CONTENT_CID_KEY) ?: -1
        swipeRefreshLayout.run {
            isRefreshing = true
            setOnRefreshListener(onRefreshListener)
        }

        recyclerView.run {
            layoutManager = linearLayoutManager
            adapter = projectAdapter
            itemAnimator = DefaultItemAnimator()
            recyclerViewItemDecoration?.let {
                addItemDecoration(it)
            }
        }

        projectAdapter.run {
            setOnLoadMoreListener(onRequestLoadMoreListener,recyclerView)
            onItemClickListener = this@ProjectListFragment.onItemClickListener
            onItemChildClickListener = this@ProjectListFragment.onItemChildClickListener
            setEmptyView(R.layout.fragment_empty_layout)
        }
    }

    private val onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener{_,view,position ->
        if (datas.size != 0){
            val data = datas[position]
            when (view.id) {
                R.id.item_project_list_like_iv -> {
                    if (isLogin) {
                        if (!NetWorkUtil.isNetworkAvailable(App.context)) {
                            showSnackMsg(resources.getString(R.string.no_network))
                            return@OnItemChildClickListener
                        }
                        val collect = data.collect
                        data.collect = !collect
                        projectAdapter.setData(position, data)
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

    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener{_,_,position ->
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
        val page = projectAdapter.data.size / 15 + 1
        mPresenter.requestProjectList(page,cid)
    }

    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        isRefresh = true
        projectAdapter.setEnableLoadMore(false)
        mPresenter.requestProjectList(1,cid)
    }

    override fun lazyLoad() {
        mPresenter.requestProjectList(1,cid)
    }
}