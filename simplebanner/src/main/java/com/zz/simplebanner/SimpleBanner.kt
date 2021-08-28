package com.zz.simplebanner

import android.widget.ImageView
import androidx.viewpager2.widget.ViewPager2
import com.zz.simplebanner.utils.widget.ProgressDotView

/**
 *    author : zz
 *    e-mail : 1140143252@qq.com
 *    date   : 2021/8/28 15:51
 *
 *    注意: into必须是在最后面 传入 ViewPage2 以及 ProgressDotView
 *    使用示例
 *    val vpBanner = findViewById<ViewPager2>(R.id.vp_banner)
 *    val pdBanner = findViewById<ProgressDotView>(R.id.pd_banner)
 *    SimpleBanner
 *        .load(
 *            arrayListOf(
 *    "https://huyaimg.msstatic.com/cdnimage/gamebanner/phpzivjlx1629479013.jpg",
 *    "https://huyaimg.msstatic.com/cdnimage/gamebanner/phphZFnfk1628077525.jpg",
 *    "https://huyaimg.msstatic.com/cdnimage/gamebanner/phpbe9dVm1626696077.jpg"
 *            )
 *        )
 *        .onClick {
 *            Log.d("zzzz", "(MainActivity.kt:20)-->> $it")
 *        }
 *        .setScaleType(ImageView.ScaleType.CENTER_CROP)
 *        .into(vpBanner, pdBanner)
 */
object SimpleBanner {
    private lateinit var mUrlList: ArrayList<String> //图片Url
    private var mPosition = 0 //当前VP显示的item的位置
    private lateinit var mOnClick: (position: Int) -> Unit //banner的点击事件
    private var mImageScaleType = ImageView.ScaleType.CENTER_INSIDE //图片的ScaleType

    //设置图片链接
    fun load(imageUrlList: ArrayList<String>): SimpleBanner {
        mUrlList = imageUrlList
        //利用如 2 0 1 2 0 的方式在头和尾添加一个图片 实现循环
        mUrlList.add(0, imageUrlList[imageUrlList.size - 1])
        mUrlList.add(imageUrlList[1])
        return this
    }

    //设置banner的点击事件
    fun onClick(onClick: (position: Int) -> Unit): SimpleBanner {
        mOnClick = onClick
        return this
    }

    //设置图片的ScaleType
    fun setScaleType(imageScaleType: ImageView.ScaleType): SimpleBanner {
        mImageScaleType = imageScaleType
        return this
    }

    //设置VP和进度圆点
    fun into(viewPager2: ViewPager2, progressDotView: ProgressDotView) {
        //配置VP和进度圆点 使其达到循环
        initView(viewPager2, progressDotView)
    }

    private fun initView(viewPager2: ViewPager2, progressDotView: ProgressDotView) {
        //设置VP的adapter
        viewPager2.adapter = BannerVPAdapter(mUrlList, mOnClick, mImageScaleType)
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
                if (position == mUrlList.size - 1) {
                    viewPager2.setCurrentItem(1, false)
                }
                if (position == 0) {
                    viewPager2.setCurrentItem(mUrlList.size - 1, false)
                }
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                mPosition = position - 1
            }
        })
    }
}