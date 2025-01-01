package com.BCU.testingapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class GridAdapter(private val context: Context, private val items: List<GridItem>) : BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.myworks_card, parent, false)

        val headerImageView: ImageView = view.findViewById(R.id.headerImage)
        val titleTextView: TextView = view.findViewById(R.id.headerTitle)
        val titleSubTextView: TextView = view.findViewById(R.id.subTitle)

        val item = items[position]
        headerImageView.setImageResource(item.imageResId)
        titleTextView.text = item.title
        titleSubTextView.text = item.subtitle

        return view
    }
}

data class GridItem(val imageResId: Int, val title: String,val subtitle:String)