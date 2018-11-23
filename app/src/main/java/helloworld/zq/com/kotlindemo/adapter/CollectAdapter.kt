package helloworld.zq.com.kotlindemo.adapter

import android.content.Context
import android.text.Html
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.mvp.model.bean.CollectionArticle
import helloworld.zq.com.kotlindemo.utils.ImageLoader

class CollectAdapter(private val context: Context?,datas : MutableList<CollectionArticle>) :
    BaseQuickAdapter<CollectionArticle,BaseViewHolder>(R.layout.item_collect_list,datas){

    override fun convert(helper: BaseViewHolder?, item: CollectionArticle?) {
        item ?: return
        helper ?: return

        helper.setText(R.id.tv_article_author,item.author)
                .setText(R.id.tv_article_date,item.niceDate)
                .setText(R.id.tv_article_title,Html.fromHtml(item.title))
                .setText(R.id.tv_article_chapterName,item.chapterName)
                .setImageResource(R.id.iv_like,R.drawable.ic_like)
                .addOnClickListener(R.id.iv_like)
        if (item.envelopePic.isNotEmpty()){
            helper.getView<ImageView>(R.id.iv_article_thumbnail)
                    .visibility = View.VISIBLE
            context?.let {
                ImageLoader.load(it,item.envelopePic,helper.getView(R.id.iv_article_thumbnail))
            }
        }else{
            helper.getView<ImageView>(R.id.iv_article_thumbnail)
                    .visibility = View.GONE
        }
    }
}