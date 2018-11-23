package helloworld.zq.com.kotlindemo.ui.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.view.Menu
import android.view.MenuItem
import android.view.View
import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.adapter.KnowledgePagerAdapter
import helloworld.zq.com.kotlindemo.base.BaseSwipeBackActivity
import helloworld.zq.com.kotlindemo.constant.Constant
import helloworld.zq.com.kotlindemo.event.ColorEvent
import helloworld.zq.com.kotlindemo.mvp.model.bean.Knowledge
import helloworld.zq.com.kotlindemo.mvp.model.bean.KnowledgeTreeBody
import helloworld.zq.com.kotlindemo.ui.fragment.KnowledgeFragment
import helloworld.zq.com.kotlindemo.utils.SettingUtil
import kotlinx.android.synthetic.main.activity_knowledge.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class KnowledgeActivity : BaseSwipeBackActivity() {

    private var knowledges = mutableListOf<Knowledge>()

    private lateinit var toolbarTitle : String

    private val viewPagerAdapter : KnowledgePagerAdapter by lazy {
        KnowledgePagerAdapter(knowledges,supportFragmentManager)
    }

    override fun attachLayoutRes(): Int = R.layout.activity_knowledge

    override fun useEventBus(): Boolean = true

    override fun initData() {
        intent.extras?.let {
            toolbarTitle = it.getString(Constant.CONTENT_TITLE_KEY,"")

            it.getSerializable(Constant.CONTENT_DATA_KEY)?.let {
                val data = it as KnowledgeTreeBody
                data.children.let {children ->
                    knowledges.addAll(children)
                }
            }
        }
    }

    override fun initView() {
        toolbar.run {
            title = toolbarTitle
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        viewPager.run {
            adapter = viewPagerAdapter
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
            offscreenPageLimit = knowledges.size
        }

        tabLayout.run {
            setupWithViewPager(viewPager)
            addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))
            addOnTabSelectedListener(onTabSelectedListener)
        }

        floating_action_btn.run {
            setOnClickListener(onFABClickListener)
        }
    }

    private val onFABClickListener = View.OnClickListener {
        if (viewPagerAdapter.count == 0){
            return@OnClickListener
        }
        val fragment : KnowledgeFragment = viewPagerAdapter.getItem(viewPager.currentItem) as KnowledgeFragment
        fragment.scrollToTop()
    }

    private val onTabSelectedListener = object : TabLayout.OnTabSelectedListener{
        override fun onTabReselected(p0: TabLayout.Tab?) {
        }

        override fun onTabUnselected(p0: TabLayout.Tab?) {
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            // 默认切换的时候，会有一个过渡动画，设为false后，取消动画，直接显示
            tab?.let {
                viewPager.setCurrentItem(it.position, false)
            }
        }
    }

    override fun start() {
    }

    override fun initColor() {
        super.initColor()
        refreshColor(ColorEvent(true))
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    private fun refreshColor(event: ColorEvent) {
        if (event.isRefresh){
            if (!SettingUtil.getIsNightMode()){
                tabLayout.setBackgroundColor(SettingUtil.getColor())
                floating_action_btn.backgroundTintList = ColorStateList.valueOf(mThemeColor)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_type_content,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_share ->{
                Intent().run {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT,
                            getString(R.string.share_article_url,
                                    getString(R.string.app_name),
                                    knowledges[tabLayout.selectedTabPosition].name,
                                    knowledges[tabLayout.selectedTabPosition].id.toString()
                                    ))
                    type = Constant.CONTENT_SHARE_TYPE
                    startActivity(Intent.createChooser(this,getString(R.string.action_share)))
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
