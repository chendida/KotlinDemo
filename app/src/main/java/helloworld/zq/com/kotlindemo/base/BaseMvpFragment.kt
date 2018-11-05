package helloworld.zq.com.kotlindemo.base

import android.os.Bundle
import android.view.View

@Suppress("UNCHECKED_CAST")
abstract class BaseMvpFragment<in V : IView,P : IPresenter<V>> : BaseFragment(),IView{
    /**
     * presenter
     */
    protected var mPresenter : P? = null

    protected abstract fun createPresenter() : P

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter = createPresenter()
        if (mPresenter != null){
            mPresenter?.attachView(this as V)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (mPresenter != null){
            mPresenter?.detachView()
        }
        mPresenter = null
    }
}