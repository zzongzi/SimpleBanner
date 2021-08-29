package com.example.simplebanner

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.zz.simplebanner.SimpleBanner

class BlankFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val banner = view.findViewById<SimpleBanner>(R.id.banner)
        banner
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
        super.onViewCreated(view, savedInstanceState)
    }
}