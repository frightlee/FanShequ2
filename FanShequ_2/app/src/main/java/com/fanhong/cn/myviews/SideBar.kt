package com.fanhong.cn.myviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.view.MotionEvent
import android.view.View

class SideBar(context: Context) : View(context) {
    var mContext: Context? = null
    private val b = arrayOf("A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#")
    var paint = Paint()
    var choose = -1 //选中状态
    var onLetterListener: OnLetterChangedListener? = null

    constructor(context: Context, f: Int) : this(context) {
        mContext = context
    }

    override fun onDraw(canvas: Canvas) {
        //获取焦点时改变背景颜色
        var singleHeight = height / b.size

        for (i in 0 until b.size) {
            paint.color = Color.GRAY
            paint.typeface = Typeface.DEFAULT_BOLD
            paint.isAntiAlias = true
            paint.textSize = dip2px(mContext!!, 10f)
            if (i == choose) {
                paint.color = Color.parseColor("#3399ff")
                paint.isFakeBoldText = true
            }
            // x坐标 = 中间 - 字符串宽度的一半
            var xPos = width/2 - paint.measureText(b[i])/2
            var yPos = singleHeight*i + singleHeight
            canvas.drawText(b[i],xPos, yPos.toFloat(),paint)
            paint.reset()
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        val oldChoose = choose
        val listener:OnLetterChangedListener = onLetterListener!!
        val c =  (event!!.y / height * b.size)  // 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.
        when(event.action){
            MotionEvent.ACTION_UP -> setBackgroundDrawable(null)
            else -> null
        }
        return true
    }

    private fun dip2px(c: Context, dpValue: Float): Float {
        val scale = c.resources.displayMetrics.density
        return dpValue * scale + 0.5f
    }

    interface OnLetterChangedListener {
        fun onLetterChanged(s: String)
    }

    fun setOnLetterChangedListener(onLetterChangedListener: OnLetterChangedListener) {
        onLetterListener = onLetterChangedListener
    }
}

