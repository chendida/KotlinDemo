package helloworld.zq.com.kotlindemo.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v4.view.ViewCompat
import android.support.v7.widget.*
import android.view.View

/**
 * 简单的RecyclerView分割线
 */
class SpaceItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private var mDivier : Drawable? = null
    private var mSectionOffsetV : Int = 0
    private var mSectionOffsetH : Int = 0
    private var mDrawOver = true
    private var attrs : IntArray = intArrayOf(android.R.attr.listDivider)

    init {
        var a = context.obtainStyledAttributes(attrs)
        mDivier = a.getDrawable(0)
        a.recycle()
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        if (mDivier != null && mDrawOver){
            draw(c,parent)
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if (mDivier != null && mDrawOver){
            draw(c,parent)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        if (getOrientation(parent.layoutManager!!) == RecyclerView.VERTICAL){
            outRect.set(mSectionOffsetH,0,mSectionOffsetH,mSectionOffsetV)
        }else{
            outRect.set(0,0,mSectionOffsetV,0)
        }
    }

    private fun getOrientation(layoutManager: RecyclerView.LayoutManager) : Int{
        if (layoutManager is LinearLayoutManager){
            return layoutManager.orientation
        }else if (layoutManager is StaggeredGridLayoutManager){
            return layoutManager.orientation
        }
        return OrientationHelper.HORIZONTAL
    }

    private fun draw(c: Canvas,parent: RecyclerView){
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        val childCount = parent.childCount
        for (i in 0 until childCount){
            val child = parent.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.bottom + params.bottomMargin + Math.round(ViewCompat.getTranslationY(child))
            val bottom = top + (if (mDivier!!.intrinsicHeight <= 0) 1 else mDivier!!.intrinsicHeight)

            mDivier?.let {
                it.setBounds(left,top,right,bottom)
                it.draw(c)
            }
        }
    }
}