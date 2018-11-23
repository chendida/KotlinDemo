package helloworld.zq.com.kotlindemo.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.adapter.CollectAdapter
import helloworld.zq.com.kotlindemo.base.BaseFragment
import helloworld.zq.com.kotlindemo.constant.Constant
import helloworld.zq.com.kotlindemo.event.RefreshEvent
import helloworld.zq.com.kotlindemo.mvp.contract.CollectContract
import helloworld.zq.com.kotlindemo.mvp.model.bean.CollectionArticle
import helloworld.zq.com.kotlindemo.mvp.model.bean.CollectionResponseBody
import helloworld.zq.com.kotlindemo.mvp.presenter.CollectPresenter
import helloworld.zq.com.kotlindemo.ui.activity.ContentActivity
import helloworld.zq.com.kotlindemo.utils.showToast
import helloworld.zq.com.kotlindemo.widget.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_refresh_layout.*
import org.greenrobot.eventbus.EventBus

class CollectFragment : BaseFragment(),CollectContract.View {
    /**
     * Presenter
     */
    private val mPresenter : CollectPresenter by lazy {
        CollectPresenter()
    }

    /**
     * datas
     */
    private val datas = mutableListOf<CollectionArticle>()

    private val linearLayoutManager : LinearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }

    private val recyclerViewItemDecoration by lazy {
        activity?.let {
            SpaceItemDecoration(it)
        }
    }

    private val collectAdapter : CollectAdapter by lazy {
        CollectAdapter(activity,datas)
    }

    private var isRefresh = true


    override fun setCollectList(articles: CollectionResponseBody<CollectionArticle>) {
        articles.datas.let {
            collectAdapter.run {
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

    override fun showRemoveCollectSuccess(success: Boolean) {
        if (success){
            showToast(getString(R.string.cancel_collect_success))
            EventBus.getDefault().post(RefreshEvent(true))
        }
    }

    override fun showLoading() {
        swipeRefreshLayout.isRefreshing = isRefresh
    }

    override fun hideLoading() {
        swipeRefreshLayout?.isRefreshing = false
        if (isRefresh){
            collectAdapter.run {
                setEnableLoadMore(true)
            }
        }
    }

    override fun showError(errMsg: String) {
        collectAdapter.run {
            if (isRefresh)
                setEnableLoadMore(true)
            else
                loadMoreFail()
        }
        showToast(errMsg)
    }

    companion object {
        fun getInstance(bundle: Bundle) : CollectFragment{
            val fragment = CollectFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun attachLayoutRes(): Int = R.layout.fragment_refresh_layout

    override fun initView() {
        mPresenter.attachView(this)

        swipeRefreshLayout.run {
            isRefreshing = true
            setOnRefreshListener(onRefreshListener)
        }

        recyclerView.run {
            adapter = collectAdapter
            layoutManager = linearLayoutManager
            itemAnimator = DefaultItemAnimator()
            recyclerViewItemDecoration?.let {
                addItemDecoration(it)
            }
        }

        collectAdapter.run {
            bindToRecyclerView(recyclerView)
            setOnLoadMoreListener(onRequestLoadMoreListener,recyclerView)
            onItemClickListener = this@CollectFragment.onItemClickListener
            onItemChildClickListener = this@CollectFragment.onItemChildClickListener
            setEmptyView(R.layout.fragment_empty_layout)
        }

    }

    private val onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener{ _,view,position ->
        if (datas.size != 0) {
            val data = datas[position]
            when(view.id) {
                R.id.iv_like -> {
                    collectAdapter.remove(position)
                    mPresenter.removeCollectArticle(data.id,data.originId)
                }
            }
        }
    }
    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener{_,_,position ->
        if (datas.size != 0){
            val data = datas[position]
            Intent(activity,ContentActivity::class.java).run {
                putExtra(Constant.CONTENT_URL_KEY,data.link)
                putExtra(Constant.CONTENT_TITLE_KEY,data.title)
                putExtra(Constant.CONTENT_ID_KEY,data.id)
                startActivity(this)
            }
        }
    }

    private val onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        isRefresh = false
        swipeRefreshLayout.isRefreshing = false
        val page = collectAdapter.data.size / 20
        mPresenter.getCollectList(page)
    }

    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        isRefresh = true
        collectAdapter.setEnableLoadMore(false)
        mPresenter.getCollectList(0)
    }

    override fun lazyLoad() {
        mPresenter.getCollectList(0)
    }
}