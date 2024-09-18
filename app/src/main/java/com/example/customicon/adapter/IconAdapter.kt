package com.example.customicon.adapter

import IconInfo
import android.content.Context
import android.graphics.drawable.Drawable
import android.transition.Transition
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.customicon.R

class IconAdapter(private val context: Context, private val icons: List<IconInfo>) : BaseAdapter() {

    override fun getCount(): Int = icons.size

    override fun getItem(position: Int): Any = icons[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(context)
        val view = convertView ?: inflater.inflate(R.layout.list_item_icon, parent, false)

        val imageView: ImageView = view.findViewById(R.id.icon_image_view)

        Glide.with(context)
            .load(icons[position].img_url)
            .error(R.drawable.img) // Hiển thị ảnh thay thế khi có lỗi
            .fallback(R.drawable.ic_launcher_foreground) // Hiển thị ảnh thay thế nếu URL là null
            .into(imageView)

        return view
    }

}