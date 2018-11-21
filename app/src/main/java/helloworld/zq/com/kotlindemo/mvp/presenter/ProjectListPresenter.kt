package helloworld.zq.com.kotlindemo.mvp.presenter

import com.cxz.wanandroid.http.exception.ExceptionHandle
import com.cxz.wanandroid.http.function.RetryWithDelay
import helloworld.zq.com.kotlindemo.mvp.contract.ProjectListContract
import helloworld.zq.com.kotlindemo.mvp.model.ProjectListModel

class ProjectListPresenter : CommonPresenter<ProjectListContract.View>(),ProjectListContract.Presenter {

    private val projectListModel : ProjectListModel by lazy {
        ProjectListModel()
    }

    override fun requestProjectList(page: Int, cid: Int) {
        if (page == 1)
            mView?.showLoading()
        val disposable = projectListModel.requestProjectList(page,cid)
                .retryWhen(RetryWithDelay())
                .subscribe({results ->
                    mView?.apply {
                        if (results.errorCode != 0){
                            showError(results.errorMsg)
                        }else{
                            setProjectList(results.data)
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