package helloworld.zq.com.kotlindemo.base

interface IPresenter<in V :IView> {
    fun attachView(mView : V)

    fun detachView()
}