package helloworld.zq.com.kotlindemo.base

interface IView {
    fun showLoading()

    fun hideLoading()

    fun showError(errMsg : String)
}