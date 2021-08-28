package com.example.simplebanner

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.zz.simplebanner.SimpleBanner
import com.zz.simplebanner.utils.widget.ProgressDotView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val vpBanner = findViewById<ViewPager2>(R.id.vp_banner)
        val pdBanner = findViewById<ProgressDotView>(R.id.pd_banner)
        SimpleBanner
            .load(
                arrayListOf(
                    "https://huyaimg.msstatic.com/cdnimage/gamebanner/phpzivjlx1629479013.jpg",
                    "https://huyaimg.msstatic.com/cdnimage/gamebanner/phphZFnfk1628077525.jpg",
                    "https://huyaimg.msstatic.com/cdnimage/gamebanner/phpbe9dVm1626696077.jpg"
                )
            )
            .onClick {
                Log.d("zzzz", "(MainActivity.kt:20)-->> $it")
            }
            .setScaleType(ImageView.ScaleType.CENTER_CROP)
            .into(vpBanner, pdBanner)
    }
}