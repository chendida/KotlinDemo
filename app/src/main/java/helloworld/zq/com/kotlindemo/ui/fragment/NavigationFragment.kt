package helloworld.zq.com.kotlindemo.ui.fragment

import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.adapter.NavigationAdapter
import helloworld.zq.com.kotlindemo.adapter.NavigationTabAdapter
import helloworld.zq.com.kotlindemo.base.BaseFragment
import helloworld.zq.com.kotlindemo.mvp.contract.NavigationContract
import helloworld.zq.com.kotlindemo.mvp.model.bean.NavigationBean
import helloworld.zq.com.kotlindemo.mvp.presenter.NavigationPresenter
import helloworld.zq.com.kotlindemo.utils.showToast
import kotlinx.android.synthetic.main.fragment_navigation.*
import q.rorbin.verticaltablayout.VerticalTabLayout
import q.rorbin.verticaltablayout.widget.TabView

/**
 * 导航
 */
class NavigationFragment : BaseFragment(),NavigationContract.View {
    /**
     * Presenter
     */
    private val mPresenter : NavigationPresenter by lazy {
        NavigationPresenter()
    }

    /**
     * datas
     */
    private val datas = mutableListOf<NavigationBean>()

    /**
     * linearLayoutManager
     */
    private val linearLayoutManager : LinearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }

    /**
     * adapter
     */
    private val navigationAdapter : NavigationAdapter by lazy {
        NavigationAdapter(activity,datas)
    }

    private var bScroll : Boolean = false
    private var currentIndex : Int = 0
    private var bClickTab : Boolean = false

    override fun setNavigationData(list: List<NavigationBean>) {
        list.let {
            navigation_tab_layout.run {
                setTabAdapter(NavigationTabAdapter(activity!!.applicationContext,list))
            }

            navigationAdapter.run {
                replaceData(it)

                loadMoreComplete()
                loadMoreEnd()
                setEnableLoadMore(false)
            }
        }
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showError(errMsg: String) {
        showToast(errMsg)
    }

    companion object {
        fun getInstance() : NavigationFragment = NavigationFragment()
    }
    override fun attachLayoutRes(): Int = R.layout.fragment_navigation

    override fun initView() {
        mPresenter.attachView(this)

        recyclerView.run {
            layoutManager = linearLayoutManager
            adapter = navigationAdapter
            itemAnimator = DefaultItemAnimator()
            setHasFixedSize(true)
        }

        navigationAdapter.run {
            bindToRecyclerView(recyclerView)
        }

        leftRightLink()
    }

    /**
     * Left TabLayout and Right RecyclerView Link
     */
    private fun leftRightLink() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (bScroll && newState == RecyclerView.SCROLL_STATE_IDLE){
                    scrollRecyclerView()
                }
                rightLinkLeft(newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (bScroll){
                    scrollRecyclerView()
                }
            }
        })

        navigation_tab_layout.addOnTabSelectedListener(object : VerticalTabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabView?, position: Int) {
                bClickTab = true
                selectTab(position)
            }

            override fun onTabReselected(tab: TabView?, position: Int) {
            }
        })
    }

    private fun selectTab(position: Int) {
        currentIndex = position
        recyclerView.stopScroll()
        smoothScrollToPosition(position)
    }

    private fun smoothScrollToPosition(position: Int) {
        val firstPosition : Int = linearLayoutManager.findFirstVisibleItemPosition()
        val lastPosition : Int = linearLayoutManager.findLastVisibleItemPosition()
        when{
            position <= firstPosition ->{
                recyclerView.smoothScrollToPosition(position)
            }
            position <= lastPosition ->{
                val top : Int = recyclerView.getChildAt(position - firstPosition).top
                recyclerView.smoothScrollBy(0,top)
            }
            else ->{
                recyclerView.smoothScrollToPosition(position)
                bScroll = true
            }
        }
    }

    /**
     * Right RecyclerView Link Left TabLayout
     */
    private fun rightLinkLeft(newState : Int) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE){
            if (bClickTab){
                bClickTab = false
                return
            }
            val firstPosition : Int = linearLayoutManager.findFirstVisibleItemPosition()
            if (firstPosition != currentIndex){
                currentIndex = firstPosition
                setChecked(currentIndex)
            }
        }
    }

    /**
     * Smooth Right RecyclerView to Select Left TabLayout
     *
     * @param position checked position
     */
    private fun setChecked(position: Int) {
        if (bClickTab){
            bClickTab = false
        }else{
            navigation_tab_layout.setTabSelected(currentIndex)
        }
        currentIndex = position
    }

    private fun scrollRecyclerView() {
        bScroll = false
        val indexDistance : Int = currentIndex - linearLayoutManager.findFirstVisibleItemPosition()
        if (indexDistance > 0 && indexDistance < recyclerView!!.childCount){
            val top : Int = recyclerView.getChildAt(indexDistance).top
            recyclerView.smoothScrollBy(0,top)
        }
    }

    override fun lazyLoad() {
        mPresenter.requestNavigationList()
    }

    fun scrollToTop() {
        navigation_tab_layout.setTabSelected(0)
    }
}