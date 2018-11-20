package helloworld.zq.com.kotlindemo.ui.fragment

import android.support.design.widget.TabLayout
import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.adapter.WeChatPagerAdapter
import helloworld.zq.com.kotlindemo.base.BaseFragment
import helloworld.zq.com.kotlindemo.event.ColorEvent
import helloworld.zq.com.kotlindemo.mvp.contract.WeChatContract
import helloworld.zq.com.kotlindemo.mvp.model.bean.WXChapterBean
import helloworld.zq.com.kotlindemo.mvp.presenter.WeChatPresenter
import helloworld.zq.com.kotlindemo.utils.SettingUtil
import kotlinx.android.synthetic.main.fragment_wechat.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 公众号
 */
class WeChatFragment : BaseFragment(),WeChatContract.View {

    /**
     * presenter
     */
    private val mPresenter : WeChatPresenter by lazy {
        WeChatPresenter()
    }

    /**
     * datas
     */
    private val datas = mutableListOf<WXChapterBean>()

    /**
     * viewPagerAdapter
     */
    private val viewPagerAdapter : WeChatPagerAdapter by lazy {
        WeChatPagerAdapter(datas,childFragmentManager)
    }



    override fun scrollToTop() {
        if (viewPagerAdapter.count == 0){
            return
        }
        val fragment : KnowledgeFragment = viewPagerAdapter.getItem(viewPager.currentItem) as KnowledgeFragment
        fragment.scrollToTop()
    }

    override fun showWXChapters(chapters: MutableList<WXChapterBean>) {
        chapters.let {
            datas.addAll(it)
            viewPager.run {
                adapter = viewPagerAdapter
                offscreenPageLimit = datas.size
            }
        }
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showError(errMsg: String) {
    }

    companion object {
        fun getInstance() : WeChatFragment = WeChatFragment()
    }
    override fun attachLayoutRes(): Int = R.layout.fragment_wechat

    override fun initView() {
        mPresenter.attachView(this)

        viewPager.run {
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        }

        tabLayout.run {
            setupWithViewPager(viewPager)
            addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))
            addOnTabSelectedListener(onTabSelectedListener)
        }

        refreshColor(ColorEvent(true))
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshColor(event: ColorEvent){
        if (event.isRefresh){
            if (!SettingUtil.getIsNightMode()){
                tabLayout.setBackgroundColor(SettingUtil.getColor())
            }
        }
    }



    /**
     * onTabSelectedListener
     */
    private val onTabSelectedListener = object : TabLayout.OnTabSelectedListener{
        override fun onTabReselected(tab: TabLayout.Tab?) {
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            // 默认切换的时候，会有一个过渡动画，设为false后，取消动画，直接显示
            tab?.let {
                viewPager.setCurrentItem(it.position,false)
            }
        }
    }


    override fun lazyLoad() {
        mPresenter.getWXChapters()
    }

    override fun doReConnected() {
        if (datas.size == 0) {
            super.doReConnected()
        }
    }
}