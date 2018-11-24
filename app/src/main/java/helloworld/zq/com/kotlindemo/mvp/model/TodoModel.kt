package helloworld.zq.com.kotlindemo.mvp.model

import helloworld.zq.com.kotlindemo.base.BaseModel
import helloworld.zq.com.kotlindemo.http.RetrofitHelper
import helloworld.zq.com.kotlindemo.mvp.model.bean.AllTodoResponseBody
import helloworld.zq.com.kotlindemo.mvp.model.bean.HttpResult
import helloworld.zq.com.kotlindemo.mvp.model.bean.TodoResponseBody
import helloworld.zq.com.kotlindemo.rx.SchedulerUtils
import io.reactivex.Observable

class TodoModel : BaseModel() {
    fun getAllTodoList(type : Int) : Observable<HttpResult<AllTodoResponseBody>>{
        return RetrofitHelper.service.getTodoList(type)
                .compose(SchedulerUtils.ioToMain())
    }

    fun getNoTodoList(page : Int,type: Int) : Observable<HttpResult<TodoResponseBody>>{
        return RetrofitHelper.service.getNoTodoList(page,type)
                .compose(SchedulerUtils.ioToMain())
    }

    fun getDownTodoList(page: Int,type: Int) : Observable<HttpResult<TodoResponseBody>>{
        return RetrofitHelper.service.getDoneList(page,type)
                .compose(SchedulerUtils.ioToMain())
    }

    fun deleteTodoById(id : Int) : Observable<HttpResult<Any>>{
        return RetrofitHelper.service.deleteTodoById(id)
                .compose(SchedulerUtils.ioToMain())
    }

    fun updateTodoStatus(id: Int,status : Int) : Observable<HttpResult<Any>>{
        return RetrofitHelper.service.updateTodoById(id,status)
                .compose(SchedulerUtils.ioToMain())
    }
}