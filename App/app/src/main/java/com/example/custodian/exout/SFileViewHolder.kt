package com.example.custodian.exout

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.custodian.R

class SFileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener,
    View.OnLongClickListener {
    var container: LinearLayout
    var imageType: ImageView
    var name: TextView
    var size: TextView
    var date: TextView

    init {
        container = itemView.findViewById(R.id.container)
        name = itemView.findViewById(R.id.name)
        imageType = itemView.findViewById(R.id.image_type)
        size = itemView.findViewById(R.id.size)
        date = itemView.findViewById(R.id.date)

        itemView.setOnClickListener(this)
        itemView.setOnLongClickListener(this)
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }

    override fun onLongClick(p0: View?): Boolean {
        TODO("Not yet implemented")
    }
}