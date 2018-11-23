package helloworld.zq.com.kotlindemo.ui.activity

import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.base.BaseSwipeBackActivity
import helloworld.zq.com.kotlindemo.constant.Constant
import helloworld.zq.com.kotlindemo.ui.fragment.*
import kotlinx.android.synthetic.main.toolbar.*

class CommonActivity : BaseSwipeBackActivity() {

    override fun attachLayoutRes(): Int = R.layout.activity_common

    override fun initData() {
    }

    override fun initView() {
        val type = intent.extras?.getString(Constant.TYPE_KEY,"")
        toolbar.run {
            title = getString(R.string.app_name)
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        val fragment = when(type){
            Constant.Type.COLLECT_TYPE_KEY ->{//收藏
                toolbar.title = getString(R.string.collect)
                CollectFragment.getInstance(intent.extras)
            }
            Constant.Type.ABOUT_US_TYPE_KEY ->{
                toolbar.title = getString(R.string.nav_about_us)
                AboutFragment.getInstance(intent.extras)
            }
            Constant.Type.SETTING_TYPE_KEY ->{
                toolbar.title = getString(R.string.nav_setting)
                SettingFragment.getInstance(intent.extras)
            }
            Constant.Type.SEARCH_TYPE_KEY ->{
                toolbar.title = intent.extras.getString(Constant.SEARCH_KEY,"")
                SearchListFragment.getInstance(intent.extras)
            }
            Constant.Type.EDIT_TODO_TYPE_KEY ->{
                toolbar.title = getString(R.string.edit)
                AddTodoFragment.getInstance(intent.extras)
            }
            Constant.Type.ADD_TODO_TYPE_KEY ->{
                toolbar.title = getString(R.string.add)
                AddTodoFragment.getInstance(intent.extras)
            }
            Constant.Type.SEE_TODO_TYPE_KEY ->{
                toolbar.title = getString(R.string.see)
                AddTodoFragment.getInstance(intent.extras)
            }
            else ->{
                null
            }
        }
        fragment ?: return
        supportFragmentManager.beginTransaction()
                .replace(R.id.common_frame_layout,fragment,Constant.Type.COLLECT_TYPE_KEY)
                .commit()
    }

    override fun start() {
    }
}
