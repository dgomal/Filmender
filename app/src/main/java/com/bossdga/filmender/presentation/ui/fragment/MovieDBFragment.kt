package com.bossdga.filmender.presentation.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bossdga.filmender.OnItemClickListener
import com.bossdga.filmender.R
import com.bossdga.filmender.model.content.BaseContent
import com.bossdga.filmender.model.content.Movie
import com.bossdga.filmender.presentation.adapter.MovieDBAdapter
import com.bossdga.filmender.presentation.ui.activity.MovieDetailActivity
import com.bossdga.filmender.presentation.viewmodel.MainViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers


/**
 * Fragment that will show a list of movies
 */
class MovieDBFragment : BaseFragment() {
    private lateinit var adapter: MovieDBAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var mainViewModel: MainViewModel
    private lateinit var moviesHeader: TextView
    private var isEmpty: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_db_movie, container, false)

        moviesHeader = rootView.findViewById(R.id.MoviesHeader)
        mRecyclerView = rootView.findViewById(R.id.recyclerView)
        linearLayoutManager = LinearLayoutManager(activity)
        mRecyclerView.layoutManager = linearLayoutManager
        adapter = MovieDBAdapter(activity as Context, object : OnItemClickListener {
            override fun onItemClick(content: BaseContent) {
                val intent = Intent(activity, MovieDetailActivity::class.java)
                intent.putExtra("id", content.id)
                requireActivity().startActivity(intent)
            }
        })
        mRecyclerView.setAdapter(adapter)

        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()

        disposable.clear()
    }

    override fun onDestroy() {
        super.onDestroy()

        disposable.dispose()
    }

    /**
     * Method that adds a Disposable to the CompositeDisposable
     * @param moviesObservable
     */
    private fun subscribeMoviesFromDB(moviesObservable: Observable<List<Movie>>) {
        disposable.add(moviesObservable
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<List<Movie>>() {
                override fun onComplete() {

                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

                override fun onNext(movies: List<Movie>) {
                    adapter.setItems(movies)
                    if(movies.isEmpty()) {
                        moviesHeader.visibility = View.GONE
                        isEmpty = true
                    } else {
                        moviesHeader.visibility = View.VISIBLE
                        isEmpty = false
                    }
                    mainViewModel.loadedDB.postValue("true")
                }
            }))
    }

    fun refreshContent() {
        subscribeMoviesFromDB(mainViewModel.loadMoviesFromDB())
    }

    fun isEmpty(): Boolean {
        return isEmpty
    }
}