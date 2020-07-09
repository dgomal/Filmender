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
class TVShowAdapter(private var context: Context, private var viewHolderType: ViewHolderType, private val listener: OnItemClickListener) : RecyclerView.Adapter<BaseViewHolder<*>>() {
    private var tvShowList: List<TVShow> = ArrayList()

    companion object {
        private const val TYPE_SIMPLE = 0
        private const val TYPE_COMPLEX = 1
    }

    /**
     * Provide a reference to the type of views used (custom ViewHolder)
     */
    inner class SimpleViewHolder(v: View) : BaseViewHolder<TVShow>(v) {
        private val image: ImageView = v.findViewById(R.id.Image)
        private val title: TextView = v.findViewById(R.id.Title)

        override fun bind(item: TVShow, listener: OnItemClickListener) {
            if(item.posterPath == null) {
                title.text = item.title
                title.visibility = View.VISIBLE
            } else {
                title.visibility = View.GONE
            }
            setImage(image, item.posterPath, ImageType.POSTER)

            itemView.setOnClickListener { listener.onItemClick(item) }
        }
    }

    /**
     * Provide a reference to the type of views used (custom ViewHolder)
     */
    inner class ComplexViewHolder(v: View) : BaseViewHolder<TVShow>(v) {
        private val image: ImageView = v.findViewById(R.id.Image)
        private val title: TextView = v.findViewById(R.id.Title)
        private val voteAverage: TextView = v.findViewById(R.id.voteAverage)
        private val numberOfSeasons: TextView = v.findViewById(R.id.numberOfSeasons)
        private val date: TextView = v.findViewById(R.id.date)

        override fun bind(item: TVShow, listener: OnItemClickListener) {
            title.text = item.title
            setImage(image, item.posterPath, ImageType.POSTER)
            voteAverage.text = item.voteAverage
            numberOfSeasons.text = item.numberOfSeasons.toString()
            date.text = item.releaseDate.substringBefore("-")

            itemView.setOnClickListener { listener.onItemClick(item) }
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
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            TYPE_SIMPLE -> {
                val view = LayoutInflater.from(context).inflate(R.layout.row_tv_show_simple, viewGroup, false)
                SimpleViewHolder(view)
            }
            TYPE_COMPLEX -> {
                val view = LayoutInflater.from(context).inflate(R.layout.row_tv_show_complex, viewGroup, false)
                ComplexViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     * @param viewHolder
     * @param position
     */
    override fun onBindViewHolder(viewHolder: BaseViewHolder<*>, position: Int) {
        val element = tvShowList[position]
        when (viewHolder) {
            is SimpleViewHolder -> viewHolder.bind(element, listener)
            is ComplexViewHolder -> viewHolder.bind(element, listener)
            else -> throw IllegalArgumentException()
        }
    }

    /**
     *
     */
    override fun getItemViewType(position: Int): Int {
        return when (viewHolderType) {
            ViewHolderType.SIMPLE -> TYPE_SIMPLE
            ViewHolderType.COMPLEX -> TYPE_COMPLEX
        }
    }

    /**
     * Return the size of the data set (invoked by the layout manager)
     * @return
     */
    override fun getItemCount() = tvShowList.size
}