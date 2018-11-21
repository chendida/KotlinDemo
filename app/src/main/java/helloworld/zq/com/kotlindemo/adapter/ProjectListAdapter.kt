package helloworld.zq.com.kotlindemo.adapter

import android.content.Context
import android.text.Html
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.mvp.model.bean.Article
import helloworld.zq.com.kotlindemo.utils.ImageLoader

class ProjectListAdapter(private val context: Context?,datas : MutableList<Article>) :
    BaseQuickAdapter<Article,BaseViewHolder>(R.layout.item_project_list,datas){

    override fun convert(helper: BaseViewHolder?, item: Article?) {
        item ?: return
        helper ?: return
        helper.setText(R.id.item_project_list_title_tv, Html.fromHtml(item.title))
                .setText(R.id.item_project_list_content_tv,Html.fromHtml(item.desc))
                .setText(R.id.item_project_list_time_tv,item.niceDate)
                .setText(R.id.item_project_list_author_tv,item.author)
                .setImageResource(R.id.item_project_list_like_iv,
                        if (item.collect) R.drawable.ic_like else R.drawable.ic_like_not
                )
                .addOnClickListener(R.id.item_project_list_like_iv)
        context.let {
            ImageLoader.load(it,item.envelopePic,helper.getView(R.id.item_project_list_iv))
        }
    }
}