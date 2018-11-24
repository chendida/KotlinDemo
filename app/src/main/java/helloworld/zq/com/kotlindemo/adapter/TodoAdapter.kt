package helloworld.zq.com.kotlindemo.adapter

import android.view.View
import android.widget.Button
import android.widget.TextView
import com.chad.library.adapter.base.BaseSectionQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.mvp.model.bean.TodoBean
import helloworld.zq.com.kotlindemo.mvp.model.bean.TodoDataBean

class TodoAdapter : BaseSectionQuickAdapter<TodoDataBean,BaseViewHolder> {

    constructor(layoutResId : Int,sectionHeaderResId : Int,data : MutableList<TodoDataBean>) : super(layoutResId,sectionHeaderResId,data)

    override fun convertHead(helper: BaseViewHolder?, item: TodoDataBean?) {
        helper ?: return
        item ?: return
        helper.setText(R.id.tv_header,item.header)
    }

    override fun convert(helper: BaseViewHolder?, item: TodoDataBean?) {
        helper ?: return
        item ?: return
        val itemData = item.t as TodoBean
        helper.setText(R.id.tv_todo_title,itemData.title)
                .addOnClickListener(R.id.item_todo_content)
                .addOnClickListener(R.id.btn_delete)
                .addOnClickListener(R.id.btn_done)
        val tv_todo_desc = helper.getView<TextView>(R.id.tv_todo_desc)
        tv_todo_desc.text = ""
        tv_todo_desc.visibility = View.INVISIBLE
        if (itemData.content.isNotEmpty()){
            tv_todo_desc.visibility = View.VISIBLE
            tv_todo_desc.text = itemData.content
        }
        val btn_down = helper.getView<Button>(R.id.btn_done)
        if (itemData.status == 0){//未完成状态
            btn_down.text = mContext.getString(R.string.mark_done)
        }else if (itemData.status == 1){
            btn_down.text = mContext.getString(R.string.restore)
        }
    }
}