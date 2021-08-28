package com.zz.simplebanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

/**
 *    author : zz
 *    e-mail : 1140143252@qq.com
 *    date   : 2021/8/28 16:00
 */
class BannerVPAdapter(private val mImageUrlList: List<String>, private val mOnClick: (position:Int) -> Unit, private val mImageScaleType:ImageView.ScaleType) :
    RecyclerView.Adapter<BannerVPAdapter.ImageHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        return ImageHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {

        //设置scaleType
        holder.bannerImage.scaleType=mImageScaleType
        //加载图片
        Glide.with(holder.bannerImage.context).load(mImageUrlList[position]).into(holder.bannerImage)
        //设置点击事件
        holder.bannerImage.setOnClickListener {
            mOnClick(position)
        }
    }

    override fun getItemCount(): Int {
        return mImageUrlList.size
    }

    class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var bannerImage: ImageView = itemView.findViewById(R.id.iv_banner_image)
    }
}