package helloworld.zq.com.kotlindemo.ui.activity

import android.content.Intent
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.adapter.SearchHistoryAdapter
import helloworld.zq.com.kotlindemo.base.BaseSwipeBackActivity
import helloworld.zq.com.kotlindemo.constant.Constant
import helloworld.zq.com.kotlindemo.mvp.contract.SearchContract
import helloworld.zq.com.kotlindemo.mvp.model.bean.HotSearchBean
import helloworld.zq.com.kotlindemo.mvp.model.bean.SearchHistoryBean
import helloworld.zq.com.kotlindemo.mvp.presenter.SearchPresenter
import helloworld.zq.com.kotlindemo.utils.CommonUtil
import helloworld.zq.com.kotlindemo.utils.DisplayManager
import helloworld.zq.com.kotlindemo.utils.showToast
import helloworld.zq.com.kotlindemo.widget.RecyclerViewItemDecoration
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.toolbar_search.*

class SearchActivity : BaseSwipeBackActivity(),SearchContract.View {

    private val mPresenter : SearchPresenter by lazy {
        SearchPresenter()
    }

    private var mHotSearchDatas = mutableListOf<HotSearchBean>()

    private val datas = mutableListOf<SearchHistoryBean>()

    private val searchHistoryAdapter by lazy {
        SearchHistoryAdapter(this,datas)
    }

    private val linearLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    private val recyclerViewItemDecoration by lazy {
        RecyclerViewItemDecoration(this)
    }

    override fun showHistoryData(historyBeans: MutableList<SearchHistoryBean>) {
        searchHistoryAdapter.replaceData(historyBeans)
    }

    override fun showHotSearchData(hotSearchDatas: MutableList<HotSearchBean>) {
        this.mHotSearchDatas.addAll(hotSearchDatas)
        hot_search_flow_layout.adapter = object : TagAdapter<HotSearchBean>(hotSearchDatas){
            override fun getView(parent: FlowLayout?, position: Int, hotSearchBean : HotSearchBean?): View {
                val tv : TextView = LayoutInflater.from(parent?.context).inflate(R.layout.flow_layout_tv,
                        hot_search_flow_layout,false) as TextView
                val padding : Int = DisplayManager.dip2px(10F)!!
                tv.setPadding(padding,padding,padding,padding)
                tv.text = hotSearchBean?.name
                tv.setTextColor(CommonUtil.randomColor())
                return tv
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.onActionViewExpanded()
        searchView.queryHint = getString(R.string.search_tint)
        searchView.setOnQueryTextListener(queryTextListener)
        searchView.isSubmitButtonEnabled = true
        try {
            val field = searchView.javaClass.getDeclaredField("mGoButton")
            field.isAccessible = true
            val mGoButton = field.get(searchView) as ImageView
            mGoButton.setImageResource(R.drawable.ic_search_white_24dp)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return super.onCreateOptionsMenu(menu)
    }

    private val queryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            gotoSearchList(query.toString())
            return false
        }

        override fun onQueryTextChange(p0: String?): Boolean {
            return false
        }
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showError(errMsg: String) {
        showToast(errMsg)
    }


    override fun attachLayoutRes(): Int = R.layout.activity_search

    override fun initData() {
    }

    override fun initView() {
        mPresenter.attachView(this)

        toolbar.run {
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        hot_search_flow_layout.run {
            setOnTagClickListener{view, position, parent ->
                if (mHotSearchDatas.size > 0){
                    val hotSearchBean = mHotSearchDatas[position]
                    gotoSearchList(hotSearchBean.name)
                    true
                }
                false
            }
        }

        rv_history_search.run {
            layoutManager = linearLayoutManager
            adapter = searchHistoryAdapter
            itemAnimator = DefaultItemAnimator()
        }

        searchHistoryAdapter.run {
            bindToRecyclerView(rv_history_search)
            onItemClickListener = this@SearchActivity.onItemClickListener
            onItemChildClickListener = this@SearchActivity.onItemChildClickListener
            setEmptyView(R.layout.fragment_empty_layout)
        }

        search_history_clear_all_tv.setOnClickListener {
            datas.clear()
            searchHistoryAdapter.replaceData(datas)
            mPresenter.clearAllHistory()
        }
        mPresenter.getHotSearchData()
    }

    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener{_,_,position ->
        if (searchHistoryAdapter.data.size != 0){
            val item = searchHistoryAdapter.data[position]
            gotoSearchList(item.key)
        }
    }

    private val onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener{_,view,positon ->
        if(searchHistoryAdapter.data.size != 0){
            val item = searchHistoryAdapter.data[positon]
            when(view.id){
                R.id.iv_clear ->{
                    mPresenter.deleteById(item.id)
                    searchHistoryAdapter.remove(positon)
                }
            }
        }
    }

    private fun gotoSearchList(name: String) {
        mPresenter.saveSearchKey(name)
        Intent(this, CommonActivity::class.java).run {
            putExtra(Constant.TYPE_KEY, Constant.Type.SEARCH_TYPE_KEY)
            putExtra(Constant.SEARCH_KEY, name)
            startActivity(this)
        }
    }

    override fun start() {
    }

    override fun onResume() {
        super.onResume()
        mPresenter.quearHistory()
    }
}
