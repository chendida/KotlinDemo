package helloworld.zq.com.kotlindemo.ui.activity

import android.support.design.widget.TabLayout
import android.view.View
import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.adapter.TodoPagerAdapter
import helloworld.zq.com.kotlindemo.base.BaseSwipeBackActivity
import helloworld.zq.com.kotlindemo.constant.Constant
import helloworld.zq.com.kotlindemo.event.ColorEvent
import helloworld.zq.com.kotlindemo.event.TodoEvent
import helloworld.zq.com.kotlindemo.mvp.model.bean.TodoTypeBean
import helloworld.zq.com.kotlindemo.utils.SettingUtil
import kotlinx.android.synthetic.main.activity_todo.*
import org.greenrobot.eventbus.EventBus

class TodoActivity : BaseSwipeBackActivity() {
    /**
     * ViewPager
     */
    private lateinit var viewPagerAdapter: TodoPagerAdapter

    private lateinit var datas : MutableList<TodoTypeBean>

    override fun initData() {
        datas = getData()
    }

    private fun getData() : MutableList<TodoTypeBean>{
        val list = mutableListOf<TodoTypeBean>()
        list.add(TodoTypeBean(0,"只用这一个"))
        list.add(TodoTypeBean(1,"工作"))
        list.add(TodoTypeBean(2,"学习"))
        list.add(TodoTypeBean(3,"生活"))
        return list
    }

    override fun initView() {
        toolbar.run {
            title = getString(R.string.nav_todo)
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        viewPagerAdapter = TodoPagerAdapter(datas,supportFragmentManager)
        viewPager.run {
            adapter = viewPagerAdapter
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
            offscreenPageLimit = datas.size
        }

        tabLayout.run {
            setupWithViewPager(viewPager)
            addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))
            addOnTabSelectedListener(onTabSelectedListener)
        }

        fab_menu.setClosedOnTouchOutside(true)
        fab_add.setOnClickListener(clickListener)
        fab_todo.setOnClickListener(clickListener)
        fab_down.setOnClickListener(clickListener)
    }

    private val clickListener = View.OnClickListener {
        val curIndex = viewPager.currentItem
        fab_menu.close(true)
        when(it.id){
            R.id.fab_add ->{
                EventBus.getDefault().post(TodoEvent(Constant.TODO_ADD,curIndex))
            }
            R.id.fab_todo ->{
                EventBus.getDefault().post(TodoEvent(Constant.TODO_NO,curIndex))
            }
            R.id.fab_down ->{
                EventBus.getDefault().post(TodoEvent(Constant.TODO_DONE,curIndex))
            }
        }
    }

    private val onTabSelectedListener = object : TabLayout.OnTabSelectedListener{
        override fun onTabReselected(p0: TabLayout.Tab?) {
        }

        override fun onTabUnselected(p0: TabLayout.Tab?) {
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            //默认切换的时候会有一个过渡动画，设为false，取消动画直接显示
            tab?.let{
                viewPager.setCurrentItem(it.position,false)
            }
        }

    }

    override fun start() {
    }

    override fun attachLayoutRes(): Int = R.layout.activity_todo

    override fun initColor() {
        super.initColor()
        refreshColor(ColorEvent(true))
    }

    private fun refreshColor(event: ColorEvent) {
        if (event.isRefresh){
            if (!SettingUtil.getIsNightMode()){
                val color = SettingUtil.getColor()
                tabLayout.setBackgroundColor(color)

                fab_menu.menuButtonColorNormal = color
                fab_menu.menuButtonColorPressed = color
                fab_menu.menuButtonColorRipple = color

                fab_add.colorNormal = color
                fab_add.colorPressed = color
                fab_add.colorRipple = color

                fab_todo.colorRipple = color
                fab_todo.colorPressed = color
                fab_todo.colorNormal = color

                fab_down.colorNormal = color
                fab_down.colorPressed = color
                fab_down.colorRipple = color
            }
        }
    }

}
