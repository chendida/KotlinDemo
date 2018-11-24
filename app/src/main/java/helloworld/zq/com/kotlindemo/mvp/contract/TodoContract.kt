package helloworld.zq.com.kotlindemo.mvp.contract

import helloworld.zq.com.kotlindemo.base.IPresenter
import helloworld.zq.com.kotlindemo.base.IView
import helloworld.zq.com.kotlindemo.mvp.model.bean.TodoResponseBody

interface TodoContract {
    interface View : IView{
        //显示待办列表
        fun showNoTodoList(todoResponseBody : TodoResponseBody)
        //显示删除是否成功
        fun showDeleteSuccess(success : Boolean)
        //显示状态更新是否成功
        fun showUpdateSuccess(success: Boolean)
    }

    interface Presenter : IPresenter<View>{
        fun getAllTodoList(type : Int)

        fun getNoTodoList(page : Int,type: Int)

        fun getDownTodoList(page: Int,type: Int)

        fun deleteTodoById(id : Int)

        fun updateTodoStatus(id : Int,status : Int)
    }
}