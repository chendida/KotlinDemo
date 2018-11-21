package helloworld.zq.com.kotlindemo.ui.fragment

import android.support.design.widget.TabLayout
import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.adapter.ProjectPagerAdapter
import helloworld.zq.com.kotlindemo.base.BaseFragment
import helloworld.zq.com.kotlindemo.event.ColorEvent
import helloworld.zq.com.kotlindemo.mvp.contract.ProjectContract
import helloworld.zq.com.kotlindemo.mvp.model.bean.ProjectTreeBean
import helloworld.zq.com.kotlindemo.mvp.presenter.ProjectPresenter
import helloworld.zq.com.kotlindemo.utils.SettingUtil
import kotlinx.android.synthetic.main.fragment_project.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 项目
 */
class ProjectFragment : BaseFragment(),ProjectContract.View {

    override fun setProjectTree(list: List<ProjectTreeBean>) {
        list.let {
            projectTree.addAll(it)
            viewPager.run {
                adapter = viewPagerAdapter
                offscreenPageLimit = projectTree.size
            }
        }
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showError(errMsg: String) {
    }

    override fun scrollToTop() {
        if (viewPagerAdapter.count == 0){
            return
        }
        val fragment : ProjectListFragment = viewPagerAdapter.getItem(viewPager.currentItem) as ProjectListFragment
        fragment.scrollToTop()
    }

    /**
     * Presenter
     */
    private val mPresenter : ProjectPresenter by lazy {
        ProjectPresenter()
    }

    /**
     * ProjectTreeBean
     */
    private var projectTree = mutableListOf<ProjectTreeBean>()

    /**
     * ViewPagerAdapter
     */
    private val viewPagerAdapter : ProjectPagerAdapter by lazy {
        ProjectPagerAdapter(projectTree,childFragmentManager)
    }

    override fun useEventBus(): Boolean = true

    companion object {
        fun getInstance() : ProjectFragment = ProjectFragment()
    }
    override fun attachLayoutRes(): Int = R.layout.fragment_project

    override fun initView() {
        mPresenter.attachView(this)

        viewPager.run {
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        }

        tabLayout.run {
            setupWithViewPager(viewPager)
            addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))
            addOnTabSelectedListener(onTabSelectListener)
        }

        refreshColor(ColorEvent(true))
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshColor(event: ColorEvent) {
        if (event.isRefresh){
            if (!SettingUtil.getIsNightMode()){
                tabLayout.setBackgroundColor(SettingUtil.getColor())
            }
        }
    }

    private val onTabSelectListener = object : TabLayout.OnTabSelectedListener{
        override fun onTabReselected(p0: TabLayout.Tab?) {
        }

        override fun onTabUnselected(p0: TabLayout.Tab?) {
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            // 默认切换的时候，会有一个过渡动画，设为false后，取消动画，直接显示
            tab?.let {
                viewPager.setCurrentItem(it.position,false)
            }
        }

    }

    override fun lazyLoad() {
        mPresenter.requestProjectTree()
    }

    override fun doReConnected() {
        if (projectTree.size == 0) {
            super.doReConnected()
        }
    }
}