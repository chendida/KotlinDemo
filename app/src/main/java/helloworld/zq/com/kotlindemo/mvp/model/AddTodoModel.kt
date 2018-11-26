package helloworld.zq.com.kotlindemo.mvp.model

import helloworld.zq.com.kotlindemo.http.RetrofitHelper
import helloworld.zq.com.kotlindemo.mvp.model.bean.HttpResult
import helloworld.zq.com.kotlindemo.rx.SchedulerUtils
import io.reactivex.Observable

class AddTodoModel {

    fun addTodo(map : MutableMap<String,Any>) : Observable<HttpResult<Any>>{
        return RetrofitHelper.service.addTodo(map)
                .compose(SchedulerUtils.ioToMain())
    }

    fun updateTodo(id : Int,map: MutableMap<String, Any>) : Observable<HttpResult<Any>>{
        return RetrofitHelper.service.updateTodo(id,map)
                .compose(SchedulerUtils.ioToMain())
    }
}