package helloworld.zq.com.kotlindemo.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.text.Html
import helloworld.zq.com.kotlindemo.mvp.model.bean.WXChapterBean
import helloworld.zq.com.kotlindemo.ui.fragment.KnowledgeFragment

class WeChatPagerAdapter(private val list: MutableList<WXChapterBean>,fm : FragmentManager?) :
    FragmentStatePagerAdapter(fm){

    private val fragments = mutableListOf<Fragment>()

    init {
        fragments.clear()
        list.forEach {
            fragments.add(KnowledgeFragment.getInstance(it.id))
        }
    }

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = list.size

    override fun getPageTitle(position: Int): CharSequence? = Html.fromHtml(list[position].name)

    override fun getItemPosition(`object`: Any): Int = PagerAdapter.POSITION_NONE
}