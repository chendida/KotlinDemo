package helloworld.zq.com.kotlindemo.ui.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import helloworld.zq.com.kotlindemo.R
import helloworld.zq.com.kotlindemo.base.BaseFragment
import helloworld.zq.com.kotlindemo.constant.Constant
import helloworld.zq.com.kotlindemo.event.RefreshTodoEvent
import helloworld.zq.com.kotlindemo.mvp.contract.AddTodoContract
import helloworld.zq.com.kotlindemo.mvp.model.bean.TodoBean
import helloworld.zq.com.kotlindemo.mvp.presenter.AddTodoPresenter
import helloworld.zq.com.kotlindemo.utils.DialogUtil
import helloworld.zq.com.kotlindemo.utils.KeyBordUtil
import helloworld.zq.com.kotlindemo.utils.formatCurrentDate
import helloworld.zq.com.kotlindemo.utils.showToast
import kotlinx.android.synthetic.main.fragment_add_todo.*
import org.greenrobot.eventbus.EventBus
import java.util.*

class AddTodoFragment : BaseFragment(),AddTodoContract.View {

    private val mPresenter by lazy {
        AddTodoPresenter()
    }

    private var mCurrentDate = formatCurrentDate()

    /**
     * 类型
     */
    private var mType : Int = 0
    private var mTodoBean : TodoBean? = null

    /**
     * 新增，编辑，查看三种状态
     */
    private var mTypeKey = ""

    private val mId : Int? = 0

    private val mDialog by lazy {
        DialogUtil.getWaitDialog(activity!!,resources.getString(R.string.save_ing))
    }


    override fun showAddTodo(success: Boolean) {
        if (success){
            showToast(getString(R.string.save_success))
            EventBus.getDefault().post(RefreshTodoEvent(true,mType))
            activity?.finish()
        }
    }

    override fun updateTodo(sussess: Boolean) {
        if (sussess){
            showToast(getString(R.string.save_success))
            EventBus.getDefault().post(RefreshTodoEvent(true,mType))
            activity?.finish()
        }
    }

    override fun getType(): Int = mType

    override fun getCurrentDate(): String = mCurrentDate

    override fun getTitle(): String = et_title.text.toString()

    override fun getContent(): String = et_content.text.toString()

    override fun getStatus(): Int = mTodoBean?.status ?: 0

    override fun getItemId(): Int = mTodoBean?.id ?:0

    override fun showLoading() {
        mDialog.show()
    }

    override fun hideLoading() {
        mDialog.dismiss()
    }

    override fun showError(errMsg: String) {
        showToast(errMsg)
    }

    companion object {
        fun getInstance(bundle: Bundle) : AddTodoFragment{
            val fragment = AddTodoFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun attachLayoutRes(): Int = R.layout.fragment_add_todo

    override fun initView() {
        mPresenter.attachView(this)

        tv_date.text = mCurrentDate
        mType = arguments?.getInt(Constant.TODO_TYPE) ?: 0
        mTypeKey = arguments?.getString(Constant.TYPE_KEY) ?: Constant.Type.ADD_TODO_TYPE_KEY

        when(mTypeKey){
            Constant.Type.ADD_TODO_TYPE_KEY ->{

            }
            Constant.Type.EDIT_TODO_TYPE_KEY ->{
                mTodoBean = arguments?.getSerializable(Constant.TODO_BEAN) as TodoBean ?: null
                et_title.setText(mTodoBean?.title)
                et_content.setText(mTodoBean?.content)
                tv_date.text = mTodoBean?.dateStr
            }
            Constant.Type.SEE_TODO_TYPE_KEY ->{
                mTodoBean = arguments?.getSerializable(Constant.TODO_BEAN) as TodoBean ?: null
                et_title.setText(mTodoBean?.title)
                et_content.setText(mTodoBean?.content)
                tv_date.text = mTodoBean?.dateStr

                et_title.isEnabled = false
                et_content.isEnabled = false
                layout_date.isEnabled = false
                btn_save.visibility = View.GONE
            }
        }

        layout_date.setOnClickListener{
            KeyBordUtil.closeKeyBord(et_content,activity!!)

            val now = Calendar.getInstance()
            val dpd = DatePickerDialog(activity,
                    DatePickerDialog.OnDateSetListener{view, year, month, dayOfMonth ->
                        val currentMonth = month + 1
                        mCurrentDate = "$year-$currentMonth-$dayOfMonth"
                        tv_date.text = mCurrentDate
                    },
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            )
            dpd.show()
        }

        btn_save.setOnClickListener {
            when(mTypeKey){
                Constant.Type.ADD_TODO_TYPE_KEY ->{
                    mPresenter.addTodo()
                }
                Constant.Type.EDIT_TODO_TYPE_KEY ->{
                    mPresenter.updateTodo(getItemId())
                }
            }
        }
    }

    override fun lazyLoad() {
    }
}