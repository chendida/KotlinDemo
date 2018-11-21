package helloworld.zq.com.kotlindemo.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import helloworld.zq.com.kotlindemo.App
import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.adapter.KnowledgeAdapter
import helloworld.zq.com.kotlindemo.base.BaseFragment
import helloworld.zq.com.kotlindemo.constant.Constant
import helloworld.zq.com.kotlindemo.mvp.contract.KnowledgeContract
import helloworld.zq.com.kotlindemo.mvp.model.bean.Article
import helloworld.zq.com.kotlindemo.mvp.model.bean.ArticleResponseBody
import helloworld.zq.com.kotlindemo.mvp.presenter.KnowledgePresenter
import helloworld.zq.com.kotlindemo.ui.activity.ContentActivity
import helloworld.zq.com.kotlindemo.ui.activity.LoginActivity
import helloworld.zq.com.kotlindemo.utils.NetWorkUtil
import helloworld.zq.com.kotlindemo.utils.showSnackMsg
import helloworld.zq.com.kotlindemo.utils.showToast
import helloworld.zq.com.kotlindemo.widget.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_refresh_layout.*

/**
 * 公众号碎片显示的fragment
 */
class KnowledgeFragment : BaseFragment(),KnowledgeContract.View {

    /**
     * presenter
     */
    private val mPresenter : KnowledgePresenter by lazy {
        KnowledgePresenter()
    }
    /**
     * cid
     */
    private var cid : Int = 0

    /**
     * datas
     */
    private val datas = mutableListOf<Article>()

    /**
     * RecyclerView Divider
     */
    private val recyclerViewItemDecoration by lazy {
        activity?.let {
            SpaceItemDecoration(it)
        }
    }

    /**
     * Knowledge Adapter
     */
    private val knowledgeAdapter : KnowledgeAdapter by lazy {
        KnowledgeAdapter(activity,datas)
    }

    /**
     * linearLayoutManager
     */
    private val linearLayoutManager : LinearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }

    /**
     * isRefresh
     */
    private var isRefresh = true



    override fun setKnowledgeList(articles: ArticleResponseBody) {
        articles.datas.let {
            knowledgeAdapter.run {
                if (isRefresh)
                    replaceData(it)
                else
                    addData(it)
                val size = it.size
                if (size < articles.size)
                    loadMoreEnd(isRefresh)
                else
                    loadMoreComplete()
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
            knowledgeAdapter.run {
                setEnableLoadMore(true)
            }
        }
    }

    override fun showError(errMsg: String) {
        knowledgeAdapter.run {
            if (isRefresh)
                setEnableLoadMore(true)
            else
                loadMoreFail()
        }
        showError(errMsg)
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

    companion object {
        fun getInstance(cid : Int) : KnowledgeFragment{
            val fragment = KnowledgeFragment()
            val args = Bundle()
            args.putInt(Constant.CONTENT_CID_KEY,cid)
            fragment.arguments = args
            return fragment
        }
    }
    override fun attachLayoutRes(): Int = R.layout.fragment_refresh_layout

    override fun initView() {
        mPresenter.attachView(this)
        cid = arguments?.getInt(Constant.CONTENT_CID_KEY) ?: 0
        swipeRefreshLayout.run {
            isRefreshing = true
            setOnRefreshListener(onRefreshListener)
        }

        recyclerView.run {
            layoutManager = linearLayoutManager
            adapter = knowledgeAdapter
            itemAnimator = DefaultItemAnimator()
            recyclerViewItemDecoration?.let { addItemDecoration(it) }
        }

        knowledgeAdapter.run {
            setOnLoadMoreListener(onRequestLoadMoreListener,recyclerView)
            onItemClickListener = this@KnowledgeFragment.onItemClickListener
            onItemChildClickListener = this@KnowledgeFragment.onItemChildClickListener
            setEmptyView(R.layout.fragment_empty_layout)
        }
    }

    /**
     * ItemChildClickListener
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
                        knowledgeAdapter.setData(position,data)
                        if (collect)
                            mPresenter.cancelCollectArticle(data.id)
                        else
                            mPresenter.addCollectArticle(data.id)
                    }else{
                        Intent(activity,LoginActivity::class.java).run{
                            startActivity(this)
                        }
                        showToast(resources.getString(R.string.login_tint))
                    }
                }
            }
        }
    }

    /**
     * ItemClickListener
     */
    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener{_,_,positon ->
        if (datas.size != 0){
            val data = datas[positon]
            Intent(activity,ContentActivity::class.java).run {
                putExtra(Constant.CONTENT_URL_KEY,data.link)
                putExtra(Constant.CONTENT_TITLE_KEY,data.title)
                putExtra(Constant.CONTENT_ID_KEY,data.id)
                startActivity(this)
            }
        }
    }

    /**
     * LoadMoreListener
     */
    private val onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        isRefresh = false
        swipeRefreshLayout.isRefreshing = false
        val page = knowledgeAdapter.data.size / 20
        mPresenter.requestKonwledgeList(page,cid)
    }

    /**
     * RefreshListener
     */
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        isRefresh = true
        knowledgeAdapter.setEnableLoadMore(false)
        mPresenter.requestKonwledgeList(0,cid)
    }

    override fun lazyLoad() {
        mPresenter.requestKonwledgeList(0,cid)
    }
}
