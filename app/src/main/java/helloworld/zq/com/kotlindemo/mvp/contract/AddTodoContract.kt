package helloworld.zq.com.kotlindemo.mvp.contract

import helloworld.zq.com.kotlindemo.base.IPresenter
import helloworld.zq.com.kotlindemo.base.IView

interface AddTodoContract {

    interface View : IView{
        fun showAddTodo(success : Boolean)

        fun updateTodo(sussess : Boolean)

        fun getType() : Int
        fun getCurrentDate() : String
        fun getTitle() : String
        fun getContent() : String
        fun getStatus() : Int
        fun getItemId() : Int
    }

    interface Presenter : IPresenter<View>{
        fun addTodo()

        fun updateTodo(id : Int)
    }
}