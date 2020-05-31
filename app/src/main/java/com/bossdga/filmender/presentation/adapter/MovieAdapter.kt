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
import com.bossdga.filmender.model.content.Movie
import com.bossdga.filmender.util.ImageUtils
import java.util.*

/**
 * Provide views to RecyclerView with data from apps.
 */
class MovieAdapter(private var context: Context, private val listener: OnItemClickListener) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {
    private var movieList: List<Movie> = ArrayList()

    /**
     * Provide a reference to the type of views used (custom ViewHolder)
     */
    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val image: ImageView = v.findViewById(R.id.Image)
        private val title: TextView = v.findViewById(R.id.Title)

        fun bind(movie: Movie, listener: OnItemClickListener) {
            if(movie.posterPath == null) {
                title.text = movie.title
                title.visibility = View.VISIBLE
            } else {
                title.visibility = View.GONE
            }
            ImageUtils.setImage(context, image, movie.posterPath, ImageType.POSTER)

            itemView.setOnClickListener { v: View? -> listener.onItemClick(movie) }
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
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.row_movie_data, viewGroup, false)
        return ViewHolder(v)
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     * @param viewHolder
     * @param position
     */
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(movieList[position], listener)
    }

    /**
     * Return the size of the data set (invoked by the layout manager)
     * @return
     */
    override fun getItemCount() = movieList.size
}