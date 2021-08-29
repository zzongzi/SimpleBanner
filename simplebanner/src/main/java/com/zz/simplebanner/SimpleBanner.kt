package com.zz.simplebanner

import android.content.Context
import android.gesture.GestureOverlayView.ORIENTATION_HORIZONTAL
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.viewpager2.widget.ViewPager2
import com.zz.simplebanner.utils.widget.ProgressDotView
import kotlin.math.absoluteValue
import kotlin.math.sign

/**
 *    author : zz
 *    e-mail : 1140143252@qq.com
 *    date   : 2021/8/28 23:55
 *
 *    v2.0:自定义了ViewGroup 里面整合好了需要的子View 创建banner只需在xml添加自定义view即可 解决了VP2中banner的滑动冲突
 *
 *    使用示例
 *    其中 load onClick setScaleType 的调用顺序不影响
 *    val banner = findViewById<SimpleBanner>(R.id.banner)
 *    banner
 *        .load(
 *            arrayListOf(
 *                "https://huyaimg.msstatic.com/cdnimage/gamebanner/phpzivjlx1629479013.jpg",
 *                "https://huyaimg.msstatic.com/cdnimage/gamebanner/phphZFnfk1628077525.jpg",
 *                "https://huyaimg.msstatic.com/cdnimage/gamebanner/phpbe9dVm1626696077.jpg"
 *            )
 *        )
 *        .onClick {
 *            Log.d("zzzz", "(MainActivity.kt:20)-->> $it")
 *        }
 *        .setScaleType(ImageView.ScaleType.CENTER_CROP)
 */
class SimpleBanner : FrameLayout {

    private var mRadius = 3f //圆点半径 dp
    private var mImageUrlList = ArrayList<String>() //banner个数
    private var mPosition = 0 //当前VP显示的item的位置
    private var mOnClick: ((position: Int) -> Unit)? = null //banner的点击事件
    private var mImageScaleType = ImageView.ScaleType.CENTER_INSIDE //图片的ScaleType
    private var mProgressDotMarginBottom = 10f //进度圆点离下方的间距 dp

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        //获取属性
        initAttrs(context, attrs)
        //创建子view
        addChild(attrs)
    }

    private fun addChild(attrs: AttributeSet?) {
        val viewPager2 = ViewPager2(context)
        val progressDotView = ProgressDotView(context, attrs)
        addView(viewPager2)
        addView(progressDotView)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.SimpleBanner)
        mRadius = dp2px(a.getFloat(R.styleable.SimpleBanner_dotRadius, mRadius))
        mProgressDotMarginBottom = dp2px(
            a.getFloat(
                R.styleable.SimpleBanner_progressDotMarginBottom,
                mProgressDotMarginBottom
            )
        )
        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //获取父控件的宽高
        val parentWidthSize = MeasureSpec.getSize(widthMeasureSpec)
        val parentHeightSize = MeasureSpec.getSize(heightMeasureSpec)
        //配置子View宽高模式
        val viewPager2 = getChildAt(0) as ViewPager2
        val progressDotView = getChildAt(1) as ProgressDotView

        val viewPageWidthSpace = MeasureSpec.makeMeasureSpec(parentWidthSize, MeasureSpec.EXACTLY)
        val viewPageHeightSpace = MeasureSpec.makeMeasureSpec(parentHeightSize, MeasureSpec.EXACTLY)
        val progressDotWidthSpace = MeasureSpec.makeMeasureSpec(
            (2 * mRadius * (2 * (mImageUrlList.size - 2) - 1)).toInt(),//这里-2是因为为了实现循环 mImageUrlList 的头和尾各多加了1
            MeasureSpec.EXACTLY
        ) //宽为(2n-1)*直径大小 即圆点间距也为直径大小
        val progressDotHeightSpace =
            MeasureSpec.makeMeasureSpec((2 * mRadius).toInt(), MeasureSpec.EXACTLY) //高为直径大小

        //测量VP2的大小与父布局相同
        measureChild(viewPager2, viewPageWidthSpace, viewPageHeightSpace)
        //测量ProgressDotView大小
        measureChild(progressDotView, progressDotWidthSpace, progressDotHeightSpace)
        //测量自己
        setMeasuredDimension(parentWidthSize, parentHeightSize)

    }

    //设置图片链接
    fun load(imageUrlList: ArrayList<String>): SimpleBanner {
        mImageUrlList = imageUrlList
        //利用如 2 0 1 2 0 的方式在头和尾添加一个图片 实现循环
        mImageUrlList.add(0, imageUrlList[imageUrlList.size - 1])
        mImageUrlList.add(imageUrlList[1])
        into(getChildAt(0) as ViewPager2, getChildAt(1) as ProgressDotView)
        return this
    }

    //设置banner的点击事件
    fun onClick(onClick: (position: Int) -> Unit): SimpleBanner {
        mOnClick = onClick
        into(getChildAt(0) as ViewPager2, getChildAt(1) as ProgressDotView)

        return this
    }

    //设置图片的ScaleType
    fun setScaleType(imageScaleType: ImageView.ScaleType): SimpleBanner {
        mImageScaleType = imageScaleType
        into(getChildAt(0) as ViewPager2, getChildAt(1) as ProgressDotView)
        return this
    }

    //设置VP和进度圆点
    private fun into(viewPager2: ViewPager2, progressDotView: ProgressDotView) {
        //配置VP和进度圆点 使其达到循环
        initView(viewPager2, progressDotView)
    }

    private fun initView(viewPager2: ViewPager2, progressDotView: ProgressDotView) {
        //设置VP的adapter
        viewPager2.adapter = BannerVPAdapter(
            mImageUrlList,
            mOnClick,
            mImageScaleType
        )
        //设置起始页 通过 page：2 0 1 2 0 来实现 0 1 2 界面的循环滑动
        viewPager2.setCurrentItem(1, false)
        //添加VP的页面选中监听 来控制圆点重绘
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                //更新圆点
                progressDotView.updatePosition(position - 1, positionOffset)
                //进行界面跳转 实现循环
                if (position == mImageUrlList.size - 1) {
                    viewPager2.setCurrentItem(1, false)
                }
                if (position == 0) {
                    viewPager2.setCurrentItem(mImageUrlList.size - 1, false)
                }
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                mPosition = position - 1
            }
        })
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val viewPager2 = getChildAt(0) as ViewPager2
        val progressDotView = getChildAt(1) as ProgressDotView
        //摆放子View
        viewPager2.layout(0, 0, measuredWidth, measuredHeight)
        progressDotView.layout(
            (measuredWidth - progressDotView.measuredWidth) / 2,
            (measuredHeight - mProgressDotMarginBottom - progressDotView.measuredHeight).toInt(),
            (measuredWidth - progressDotView.measuredWidth) / 2 + progressDotView.measuredWidth,
            (measuredHeight - mProgressDotMarginBottom).toInt()
        )
    }

    private var touchSlop = 0
    private var initialX = 0f
    private var initialY = 0f
    private val parentViewPager: ViewPager2?
        get() {
            var v: View? = parent as? View
            while (v != null && v !is ViewPager2) {
                v = v.parent as? View
            }
            return v as? ViewPager2
        }

    private val child: View? get() = if (childCount > 0) getChildAt(0) else null

    init {
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    private fun canChildScroll(orientation: Int, delta: Float): Boolean {
        val direction = -delta.sign.toInt()
        return when (orientation) {
            0 -> child?.canScrollHorizontally(direction) ?: false
            1 -> child?.canScrollVertically(direction) ?: false
            else -> throw IllegalArgumentException()
        }
    }

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        handleInterceptTouchEvent(e)
        return super.onInterceptTouchEvent(e)
    }

    private fun handleInterceptTouchEvent(e: MotionEvent) {
        val orientation = parentViewPager?.orientation ?: return

        // Early return if child can't scroll in same direction as parent
        if (!canChildScroll(orientation, -1f) && !canChildScroll(orientation, 1f)) {
            return
        }

        if (e.action == MotionEvent.ACTION_DOWN) {
            initialX = e.x
            initialY = e.y
            parent.requestDisallowInterceptTouchEvent(true)
        } else if (e.action == MotionEvent.ACTION_MOVE) {
            val dx = e.x - initialX
            val dy = e.y - initialY
            val isVpHorizontal = orientation == ORIENTATION_HORIZONTAL

            // assuming ViewPager2 touch-slop is 2x touch-slop of child
            val scaledDx = dx.absoluteValue * if (isVpHorizontal) .5f else 1f
            val scaledDy = dy.absoluteValue * if (isVpHorizontal) 1f else .5f

            if (scaledDx > touchSlop || scaledDy > touchSlop) {
                if (isVpHorizontal == (scaledDy > scaledDx)) {
                    // Gesture is perpendicular, allow all parents to intercept
                    parent.requestDisallowInterceptTouchEvent(false)
                } else {
                    // Gesture is parallel, query child if movement in that direction is possible
                    if (canChildScroll(orientation, if (isVpHorizontal) dx else dy)) {
                        // Child can scroll, disallow all parents to intercept
                        parent.requestDisallowInterceptTouchEvent(true)
                    } else {
                        // Child cannot scroll, allow all parents to intercept
                        parent.requestDisallowInterceptTouchEvent(false)
                    }
                }
            }
        }
    }

    //dp2px
    private fun dp2px(dpValue: Float) = (dpValue * resources.displayMetrics.density + 0.5f)
}