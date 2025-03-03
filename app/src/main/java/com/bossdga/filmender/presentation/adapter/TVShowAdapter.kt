package com.bossdga.filmender.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bossdga.filmender.OnItemClickListener
import com.bossdga.filmender.R
import com.bossdga.filmender.model.content.ImageType
import com.bossdga.filmender.model.content.TVShow
import com.bossdga.filmender.util.ImageUtils.setImage
import java.util.*

/**
 * Provide views to RecyclerView with data from apps.
 */
class TVShowAdapter(private var context: Context, private val listener: OnItemClickListener) : RecyclerView.Adapter<TVShowAdapter.ViewHolder>() {
    private var tvShowList: List<TVShow> = ArrayList()

    /**
     * Provide a reference to the type of views used (custom ViewHolder)
     */
    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val image: ImageView = v.findViewById(R.id.Image)
        private val title: TextView = v.findViewById(R.id.Title)

        fun bind(tvShow: TVShow, listener: OnItemClickListener) {
            if(tvShow.posterPath == null) {
                title.text = tvShow.title
                title.visibility = View.VISIBLE
            } else {
                title.visibility = View.GONE
            }
            setImage(image, tvShow.posterPath, ImageType.POSTER)

            itemView.setOnClickListener { listener.onItemClick(tvShow) }
        }
    }

    /**
     * Sets the data set to the recyclerview
     * @param tvShowList
     */
    fun setItems(tvShowList: List<TVShow>) {
        this.tvShowList = tvShowList
        notifyDataSetChanged()
    }

    /**
     * Create new views (invoked by the layout manager)
     * @param viewGroup
     * @param viewType
     * @return
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.row_tv_show_data, viewGroup, false)
        return ViewHolder(v)
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     * @param viewHolder
     * @param position
     */
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(tvShowList[position], listener)
    }

    /**
     * Return the size of the data set (invoked by the layout manager)
     * @return
     */
    override fun getItemCount() = tvShowList.size
}