package com.example.simplebanner

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //v1.0 需要自己在xml中写好 ViewPager2 和 ProgressDotView 然后into添加上去 未解决VP2中banner的滑动冲突问题
//        val vpBanner = findViewById<ViewPager2>(R.id.vp_banner)
//        val pdBanner = findViewById<ProgressDotView>(R.id.pd_banner)
//        SimpleBannerHelper
//            .load(
//                arrayListOf(
//                    "https://huyaimg.msstatic.com/cdnimage/gamebanner/phpzivjlx1629479013.jpg",
//                    "https://huyaimg.msstatic.com/cdnimage/gamebanner/phphZFnfk1628077525.jpg",
//                    "https://huyaimg.msstatic.com/cdnimage/gamebanner/phpbe9dVm1626696077.jpg"
//                )
//            )
//            .onClick {
//                Log.d("zzzz", "(MainActivity.kt:20)-->> $it")
//            }
//            .setScaleType(ImageView.ScaleType.CENTER_CROP)
//            .into(vpBanner, pdBanner)


        //v2.0 自定义了ViewGroup 里面整合好了需要的子View 创建banner只需在xml添加自定义view即可 解决了VP2中banner的滑动冲突
//        val banner = findViewById<SimpleBanner>(R.id.banner)
//        banner
//            .load(
//                arrayListOf(
//                    "https://huyaimg.msstatic.com/cdnimage/gamebanner/phpzivjlx1629479013.jpg",
//                    "https://huyaimg.msstatic.com/cdnimage/gamebanner/phphZFnfk1628077525.jpg",
//                    "https://huyaimg.msstatic.com/cdnimage/gamebanner/phpbe9dVm1626696077.jpg"
//                )
//            )
//            .onClick {
//                Log.d("zzzz", "(MainActivity.kt:20)-->> $it")
//            }
//            .setScaleType(ImageView.ScaleType.CENTER_CROP)


        val vp = findViewById<ViewPager2>(R.id.vp_test)
        vp.adapter = FragmentStateAdapter(this, arrayListOf(BlankFragment(), BlankFragment()))
    }
}