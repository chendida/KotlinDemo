package helloworld.zq.com.kotlindemo.ui.fragment

import android.content.Intent
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.adapter.KnowledgeTreeAdapter
import helloworld.zq.com.kotlindemo.base.BaseFragment
import helloworld.zq.com.kotlindemo.constant.Constant
import helloworld.zq.com.kotlindemo.mvp.contract.KnowledgeTreeContract
import helloworld.zq.com.kotlindemo.mvp.model.bean.KnowledgeTreeBody
import helloworld.zq.com.kotlindemo.mvp.presenter.KnowledgeTreePresenter
import helloworld.zq.com.kotlindemo.ui.activity.KnowledgeActivity
import helloworld.zq.com.kotlindemo.utils.showToast
import helloworld.zq.com.kotlindemo.widget.RecyclerViewItemDecoration
import kotlinx.android.synthetic.main.fragment_knowledge.*

/**
 * 知识体系
 */
class KnowledgeTreeFragment : BaseFragment(),KnowledgeTreeContract.View {

    /**
     * mPresenter
     */
    private val mPresenter by lazy {
        KnowledgeTreePresenter()
    }

    /**
     * datas
     */
    private val datas = mutableListOf<KnowledgeTreeBody>()

    /**
     * adapter
     */
    private val knowledgeTreeAdapter : KnowledgeTreeAdapter by lazy {
        KnowledgeTreeAdapter(activity,datas)
    }

    /**
     * RecyclerView Divider
     */
    private val recylerViewItemDecoration by lazy {
        activity?.let {
            RecyclerViewItemDecoration(it,LinearLayoutManager.VERTICAL)
        }
    }

    /**
     * linearLayoutManager
     */
    private val linearLayoutManager : LinearLayoutManager by lazy {
        LinearLayoutManager(activity)
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

    override fun setKnowledgeTree(list: List<KnowledgeTreeBody>) {
        list.let {
            knowledgeTreeAdapter.run {
                replaceData(list)
            }
        }
    }

    override fun showLoading() {
        swipeRefreshLayout.isRefreshing = true
    }

    override fun hideLoading() {
        swipeRefreshLayout.isRefreshing = false
        knowledgeTreeAdapter.run {
            loadMoreComplete()
        }
    }

    override fun showError(errMsg: String) {
        knowledgeTreeAdapter.run {
            loadMoreFail()
        }
        showToast(errMsg)
    }

    companion object {
        fun getInstance() : KnowledgeTreeFragment = KnowledgeTreeFragment()
    }
    override fun attachLayoutRes(): Int = R.layout.fragment_knowledge

    override fun initView() {
        mPresenter.attachView(this)
        swipeRefreshLayout.run{
            isRefreshing = true
            setOnRefreshListener(onRefreshListener)
        }

        recyclerView.run {
            layoutManager = linearLayoutManager
            adapter = knowledgeTreeAdapter
            itemAnimator = DefaultItemAnimator()
            recylerViewItemDecoration?.let { addItemDecoration(it) }
        }

        knowledgeTreeAdapter.run {
            bindToRecyclerView(recyclerView)
            setEnableLoadMore(false)
            onItemClickListener = this@KnowledgeTreeFragment.onItemClickListener
            setEmptyView(R.layout.fragment_empty_layout)
        }
    }

    /**
     * onItemClickListener
     */
    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener{_,_,position ->
        if (datas.size > 0){
            val data = datas[position]
            Intent(activity,KnowledgeActivity::class.java).run {
                putExtra(Constant.CONTENT_TITLE_KEY, data.name)
                putExtra(Constant.CONTENT_DATA_KEY, data)
                startActivity(this)
            }
        }
    }

    /**
     * onRefreshListener
     */
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        mPresenter.requestKnowledgeTree()
    }

    override fun lazyLoad() {
        mPresenter.requestKnowledgeTree()
    }
}