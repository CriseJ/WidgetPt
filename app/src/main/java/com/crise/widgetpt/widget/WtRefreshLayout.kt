package com.crise.widgetpt.widget

import android.content.Context
import android.os.Build
import android.support.v4.view.MotionEventCompat
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.AbsListView

/**
 * 注释：
 * ===========================
 * Author by Jack
 * on 2018/7/20 16:27
 */
class WtRefreshLayout: ViewGroup {

    private var mTargetView: View? = null//刷新的View中填充的View
    private var mIsBeingDragged: Boolean = false//是否可以拖动布局
    private var mActivePointerId: Int = INVALID_POINTER//
    //Kotlin中的静态方法和常量需要用下面的方式包裹起来：companion object{}
    companion object {
        private val INVALID_POINTER = -1
    }

    private var mInitialDownY: Float = 0.toFloat()//触摸点最开始的Y坐标
    private var mTouchSlop: Int = 0//系统允许最小的滑动判断值
    private var mInitialMotionY: Float = 0.toFloat()//总共滑动的距离Y
    private var mLastMotionY: Float = 0.toFloat()//滑动到最后的触摸点的Y坐标

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (childCount == 0) {
            return
        }
        if (mTargetView == null) {
            ensureTarget()
        }
        if (mTargetView == null) {
            return
        }
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        initView(context)
    }

    fun initView(context: Context?){
        mTouchSlop = ViewConfiguration.get(getContext()).scaledTouchSlop
    }

    /**
     * 给WtRefreshLayout设置内部的View，只能有一个View
     * */
    fun setTargetView(view: View) {
        if (mTargetView != null) {
            this.removeView(mTargetView)
        }
        mTargetView = view
        this.addView(view)
    }

    fun getTargetView(): View? {
        return this.mTargetView
    }



    private fun ensureTarget() {
        if (mTargetView == null) {
            mTargetView = getChildAt(childCount - 1)
        }
    }

    override fun setOnTouchListener(l: OnTouchListener?) {
        super.setOnTouchListener(l)

    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        ensureTarget()
        val action = MotionEventCompat.getActionMasked(ev)//动作掩码
        //判断拦截
        when(action){
            MotionEvent.ACTION_DOWN -> {
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0)
                mIsBeingDragged = false
                val initialDownY = getMotionEventY(ev!!, mActivePointerId)
                if (initialDownY == -1f) {
                    return false
                }
                mInitialDownY = initialDownY//将用户手指DOWN在手机屏幕上时，一开始的触摸点的Y坐标记录下来
            }
            MotionEvent.ACTION_MOVE ->{
                if (mActivePointerId == INVALID_POINTER) {
                    // 如果这里的触摸点的Id还是初始值，说明没有手指触摸到屏幕
                    return false
                }
                val y = getMotionEventY(ev!!, mActivePointerId)
                if (y == -1f) {
                    return false
                }
                if(y > mInitialDownY) {
                    //判断是否是上拉操作
                    //判断是否是上拉操作
                    val yDiff = y - mInitialDownY
                    if (yDiff > mTouchSlop && !mIsBeingDragged && !canChildScrollUp()) {
                        mInitialMotionY = mInitialDownY + mTouchSlop
                        mLastMotionY = mInitialMotionY
                        mIsBeingDragged = true
                    }
                }else if(y < mInitialDownY){
                    //判断是否是下拉操作
                    val yDiff = mInitialDownY - y
                    if (yDiff > mTouchSlop && !mIsBeingDragged && !canChildScrollDown()) {
                        mInitialMotionY = mInitialDownY + mTouchSlop//初始的Y坐标 + 安卓允许View滑动的最小的距离
                        mLastMotionY = mInitialMotionY
                        mIsBeingDragged = true
                    }
                }
            }
            MotionEventCompat.ACTION_POINTER_UP ->{ }
            MotionEvent.ACTION_UP ->{
                //恢复到初始状态的值
                mIsBeingDragged = false
                mActivePointerId = INVALID_POINTER
            }
            MotionEvent.ACTION_CANCEL ->{
                //恢复到初始状态的值
                mIsBeingDragged = false
                mActivePointerId = INVALID_POINTER
            }
        }
        return mIsBeingDragged
    }

    /**
    * 通过id来获取触摸点的Y坐标
    * */
    private fun getMotionEventY(ev: MotionEvent, activePointerId: Int): Float {
        val index = MotionEventCompat.findPointerIndex(ev, activePointerId)
        return if (index < 0) {
            -1f
        } else MotionEventCompat.getY(ev, index)
    }

    /**
     * 判断View是否可以下拉
     * @return canChildScrollDown
     */
    fun canChildScrollDown(): Boolean {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (mTargetView is AbsListView) {
                val absListView = mTargetView as AbsListView
                return (absListView.childCount > 0 && absListView.adapter != null
                        && (absListView.lastVisiblePosition < absListView.adapter.count - 1 || absListView.getChildAt(absListView.childCount - 1)
                        .bottom < absListView.paddingBottom))
            } else {
                return ViewCompat.canScrollVertically(mTargetView, 1) || mTargetView.getScrollY() > 0
            }
        } else {
            return ViewCompat.canScrollVertically(mTargetView, 1)
        }
    }

    /**
     * 判断View是否可以上拉
     * @return canChildScrollUp
     */
    fun canChildScrollUp(): Boolean {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (mTargetView is AbsListView) {
                val absListView = mTargetView as AbsListView
                return absListView.childCount > 0 && (absListView.firstVisiblePosition > 0 || absListView.getChildAt(0)
                        .top < absListView.paddingTop)
            } else {
                return ViewCompat.canScrollVertically(mTargetView, -1) || mTargetView.getScrollY() > 0
            }
        } else {
            return ViewCompat.canScrollVertically(mTargetView, -1)
        }
    }




















}