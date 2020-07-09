package com.bossdga.filmender.presentation.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bossdga.filmender.OnItemClickListener

abstract class BaseViewHolder<T>(v: View) : RecyclerView.ViewHolder(v) {
    abstract fun bind(item: T, listener: OnItemClickListener)
}