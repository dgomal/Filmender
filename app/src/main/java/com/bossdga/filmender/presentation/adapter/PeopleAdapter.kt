package com.bossdga.filmender.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bossdga.filmender.OnImageClickListener
import com.bossdga.filmender.OnItemClickListener
import com.bossdga.filmender.R
import com.bossdga.filmender.model.content.ImageType
import com.bossdga.filmender.model.content.People
import com.bossdga.filmender.util.ImageUtils
import java.util.*

/**
 * Provide views to RecyclerView with data from apps.
 */
class PeopleAdapter(private var context: Context, private val listener: OnImageClickListener) : RecyclerView.Adapter<PeopleAdapter.ViewHolder>() {
    private var peopleList: List<People> = ArrayList()

    /**
     * Provide a reference to the type of views used (custom ViewHolder)
     */
    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val image: ImageView = v.findViewById(R.id.Image)
        private val name: TextView = v.findViewById(R.id.Name)

        fun bind(people: People, listener: OnImageClickListener) {
            name.text = people.name
            ImageUtils.setImage(context, image, people.profilePath, ImageType.PROFILE)

            itemView.setOnClickListener { v: View? -> listener.onImageClick(people) }
        }
    }

    /**
     * Sets the data set to the recyclerview
     * @param movieList
     */
    fun setItems(peopleList: List<People>) {
        this.peopleList = peopleList
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
                .inflate(R.layout.row_people_data, viewGroup, false)
        return ViewHolder(v)
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     * @param viewHolder
     * @param position
     */
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(peopleList[position], listener)
    }

    /**
     * Return the size of the data set (invoked by the layout manager)
     * @return
     */
    override fun getItemCount() = peopleList.size
}