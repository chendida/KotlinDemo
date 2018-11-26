package helloworld.zq.com.kotlindemo.mvp.presenter

import com.cxz.wanandroid.http.exception.ExceptionHandle
import com.cxz.wanandroid.http.function.RetryWithDelay
import helloworld.zq.com.kotlindemo.base.BasePresenter
import helloworld.zq.com.kotlindemo.mvp.contract.AddTodoContract
import helloworld.zq.com.kotlindemo.mvp.model.AddTodoModel

class AddTodoPresenter : BasePresenter<AddTodoContract.View>(),AddTodoContract.Presenter {


    private val addTodoModel : AddTodoModel by lazy {
        AddTodoModel()
    }

    override fun addTodo() {
        val type = mView?.getType() ?: 0
        val title = mView?.getTitle().toString()
        val content = mView?.getContent().toString()
        val date = mView?.getCurrentDate().toString()
        val map = mutableMapOf<String,Any>()
        map["type"] = type
        map["title"] = title
        map["content"] = content
        map["date"] = date

        mView?.showLoading()
        val disposable = addTodoModel.addTodo(map)
                .retryWhen(RetryWithDelay())
                .subscribe({results ->
                    mView?.apply {
                        if (results.errorCode != 0){
                            showError(errMsg = results.errorMsg)
                        }else{
                            showAddTodo(true)
                        }
                        hideLoading()
                    }
                },{t ->
                    mView?.apply {
                        hideLoading()
                        showError(ExceptionHandle.handleException(t))
                    }
                })
        addSubscription(disposable)
    }

    override fun updateTodo(id: Int) {
        val type = mView?.getType() ?: 0
        val content = mView?.getContent().toString()
        val title = mView?.getTitle().toString()
        val date = mView?.getCurrentDate().toString()
        val status = mView?.getStatus() ?: 0
        val map = mutableMapOf<String,Any>()
        map["type"] = type
        map["content"] = content
        map["title"] = title
        map["date"] = date
        map["status"] = status

        mView?.showLoading()
        val disposable = addTodoModel.updateTodo(id,map)
                .retryWhen(RetryWithDelay())
                .subscribe({results ->
                    mView?.apply {
                        if (results.errorCode != 0){
                            showError(results.errorMsg)
                        }else{
                            updateTodo(true)
                        }
                        hideLoading()
                    }
                },{t ->
                    mView?.apply {
                        hideLoading()
                        showError(ExceptionHandle.handleException(t))
                    }
                })
        addSubscription(disposable)
    }
}