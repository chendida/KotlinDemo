package helloworld.zq.com.kotlindemo.mvp.presenter

import com.cxz.wanandroid.http.exception.ExceptionHandle
import com.cxz.wanandroid.http.function.RetryWithDelay
import helloworld.zq.com.kotlindemo.mvp.contract.HomeContract
import helloworld.zq.com.kotlindemo.mvp.model.HomeModel
import helloworld.zq.com.kotlindemo.mvp.model.bean.Article
import helloworld.zq.com.kotlindemo.mvp.model.bean.ArticleResponseBody
import helloworld.zq.com.kotlindemo.mvp.model.bean.HttpResult
import helloworld.zq.com.kotlindemo.utils.SettingUtil
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

class HomePresenter : CommonPresenter<HomeContract.View>(),HomeContract.Presenter {
    private val mHomeModel : HomeModel by lazy {
        HomeModel()
    }
    override fun requestHomeData() {
        mView?.showLoading()

        requestBanner()

        val observable = if (SettingUtil.getIsShowTopArticle()){
            mHomeModel.requestArticles(0)
        }else{
            Observable.zip(mHomeModel.requestTopArticles(),mHomeModel.requestArticles(0),
                    BiFunction<HttpResult<MutableList<Article>>,HttpResult<ArticleResponseBody>,
                            HttpResult<ArticleResponseBody>>{t1, t2 ->
                        t1.data.forEach {
                            // 置顶数据中没有标识，手动添加一个标识
                            it.top = "1"
                        }
                        t2.data.datas.addAll(0,t1.data)
                        t2
                    })
        }

        val disposable = observable.retryWhen(RetryWithDelay())
                .subscribe({results ->
                    mView?.apply {
                        if (results.errorCode != 0){
                            showError(results.errorMsg)
                        }else{
                            setArticles(results.data)
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

    override fun requestBanner() {
        val disposable = mHomeModel.requestBanner()
                .retryWhen(RetryWithDelay())
                .subscribe({results ->
                    mView?.apply {
                        if (results.errorCode != 0){
                            showError(results.errorMsg)
                        }else{
                            setBanner(results.data)
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

    override fun requestArticles(num: Int) {
        if (num == 0)
            mView?.showLoading()
        val disposable = mHomeModel.requestArticles(num)
                .retryWhen(RetryWithDelay())
                .subscribe({results ->
                    mView?.apply {
                        if (results.errorCode != 0){
                            showError(results.errorMsg)
                        }else{
                            setArticles(results.data)
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