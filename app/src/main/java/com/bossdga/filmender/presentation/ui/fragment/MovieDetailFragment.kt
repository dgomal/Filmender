package com.bossdga.filmender.presentation.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.bossdga.filmender.OnLoadingListener
import com.bossdga.filmender.R
import com.bossdga.filmender.model.content.ImageType
import com.bossdga.filmender.model.content.Movie
import com.bossdga.filmender.presentation.viewmodel.MovieDetailViewModel
import com.bossdga.filmender.util.DateUtils
import com.bossdga.filmender.util.ImageUtils.setImage
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers


/**
 * A simple Fragment that will show Event details
 */
class MovieDetailFragment : BaseFragment() {
    private lateinit var movieDetailViewModel: MovieDetailViewModel
    private var id: Int? = 0

    private lateinit var image: ImageView
    private lateinit var voteAverage: TextView
    private lateinit var date: TextView
    private lateinit var overview: TextView
    private lateinit var genre: TextView
    private lateinit var cast: TextView
    private lateinit var runtime: TextView
    private lateinit var onLoadingListener: OnLoadingListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showProgressDialog()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false)

        image = requireActivity().findViewById(R.id.image)
        voteAverage = rootView.findViewById(R.id.voteAverage)
        date = rootView.findViewById(R.id.date)
        overview = rootView.findViewById(R.id.overview)
        genre = rootView.findViewById(R.id.genre)
        cast = rootView.findViewById(R.id.cast)
        runtime = rootView.findViewById(R.id.runtime)

        movieDetailViewModel = ViewModelProvider(requireActivity()).get(MovieDetailViewModel::class.java)
        id = extras?.getIntExtra("id", 0)
        subscribeMovie(movieDetailViewModel.loadMovie(id,
            "videos,images,credits"))

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

    override fun onAttach(context: Context) {
        super.onAttach(context)

        this.onLoadingListener = (context as OnLoadingListener)
    }

    /**
     * Method that adds a Disposable to the CompositeDisposable
     * @param moviesObservable
     */
    private fun subscribeMovie(movieObservable: Observable<Movie>) {
        disposable.add(movieObservable
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<Movie>() {
                override fun onComplete() {}

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

                override fun onNext(movie: Movie) {
                    setImage(activity as Context, image, movie.backdropPath, ImageType.BACK_DROP)
                    onLoadingListener.onFinishedLoading(movie.title)
                    voteAverage.text = movie.voteAverage
                    runtime.text = DateUtils.fromMinutesToHHmm(movie.runtime)
                    date.text = movie.releaseDate.substringBefore("-")
                    overview.text = movie.overview
                    genre.text = movie.genres.joinToString(separator = " | ") { it.name }
                    cast.text = movie.credits.cast.joinToString(separator = ", ") { it.name }
                    hideProgressDialog()
                }
            }))
    }
}