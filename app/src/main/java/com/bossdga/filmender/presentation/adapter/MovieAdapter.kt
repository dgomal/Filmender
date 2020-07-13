package com.bossdga.filmender.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bossdga.filmender.OnItemClickListener
import com.bossdga.filmender.R
import com.bossdga.filmender.model.content.ImageType
import com.bossdga.filmender.model.content.LayoutType
import com.bossdga.filmender.model.content.Movie
import com.bossdga.filmender.util.DateUtils
import com.bossdga.filmender.util.ImageUtils.setImage
import java.util.*

/**
 * Provide views to RecyclerView with data from apps.
 */
class MovieAdapter(private var context: Context, private var layoutType: LayoutType, private val listener: OnItemClickListener) : RecyclerView.Adapter<BaseViewHolder<*>>() {
    private var movieList: List<Movie> = ArrayList()

    companion object {
        private const val TYPE_SIMPLE = 0
        private const val TYPE_COMPLEX = 1
    }

    /**
     * Provide a reference to the type of views used (custom ViewHolder)
     */
    inner class SimpleViewHolder(v: View) : BaseViewHolder<Movie>(v) {
        private val image: ImageView = v.findViewById(R.id.Image)
        private val title: TextView = v.findViewById(R.id.Title)

        override fun bind(item: Movie, listener: OnItemClickListener) {
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
    inner class ComplexViewHolder(v: View) : BaseViewHolder<Movie>(v) {
        private val image: ImageView = v.findViewById(R.id.Image)
        private val title: TextView = v.findViewById(R.id.Title)
        private val voteAverage: TextView = v.findViewById(R.id.voteAverage)
        private val runtime: TextView = v.findViewById(R.id.runtime)
        private val date: TextView = v.findViewById(R.id.date)

        override fun bind(item: Movie, listener: OnItemClickListener) {
            title.text = item.title
            setImage(image, item.posterPath, ImageType.POSTER)
            voteAverage.text = item.voteAverage.toString()
            runtime.text = DateUtils.fromMinutesToHHmm(item.runtime)
            date.text = item.releaseDate.substringBefore("-")

            itemView.setOnClickListener { listener.onItemClick(item) }
        }
    }

    /**
     * Sets the data set to the recyclerview
     * @param movieList
     */
    fun setItems(movieList: List<Movie>) {
        this.movieList = movieList
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
                val view = LayoutInflater.from(context).inflate(R.layout.row_movie_simple, viewGroup, false)
                SimpleViewHolder(view)
            }
            TYPE_COMPLEX -> {
                val view = LayoutInflater.from(context).inflate(R.layout.row_movie_complex, viewGroup, false)
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
        val element = movieList[position]
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
        return when (layoutType) {
            LayoutType.COMPLEX -> TYPE_COMPLEX
            LayoutType.SIMPLE -> TYPE_SIMPLE
            else -> TYPE_SIMPLE
        }
    }

    /**
     * Return the size of the data set (invoked by the layout manager)
     * @return
     */
    override fun getItemCount() = movieList.size
}