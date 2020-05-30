package com.bossdga.filmender.presentation.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.bossdga.filmender.R
import com.bossdga.filmender.model.content.BaseContent
import com.bossdga.filmender.model.content.ImageType
import com.bossdga.filmender.model.content.Movie
import com.bossdga.filmender.model.content.MovieResponse
import com.bossdga.filmender.presentation.viewmodel.MovieDetailViewModel
import com.bossdga.filmender.util.DateUtils
import com.bossdga.filmender.util.ImageUtils.setImage
import com.bossdga.filmender.util.NumberUtils
import com.bossdga.filmender.util.PreferenceUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers


/**
 * A simple Fragment that will show a movie
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
        if(id == 0) {
            subscribeMovies(movieDetailViewModel.loadMovies(
                PreferenceUtils.getYearFrom(activity as Context),
                PreferenceUtils.getYearTo(activity as Context),
                PreferenceUtils.getRating(activity as Context),
                PreferenceUtils.getGenres(activity as Context)))
        } else {
            subscribeMovie(movieDetailViewModel.loadMovie(id, "videos,images,credits"))
        }

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
                    renderView(movie)
                    movieDetailViewModel.loaded.postValue(movie.title)
                    hideProgressDialog()
                }
            }))
    }

    private fun renderView(movie: Movie) {
        setImage(activity as Context, image, movie.backdropPath, ImageType.BACK_DROP)
        voteAverage.text = movie.voteAverage
        runtime.text = DateUtils.fromMinutesToHHmm(movie.runtime)
        date.text = movie.releaseDate.substringBefore("-")
        overview.text = movie.overview
        genre.text = movie.genres.joinToString(separator = " | ") { it.name }
        cast.text = movie.credits.cast.joinToString(separator = ", ") { it.name }
    }

    /**
     * Method that adds a Disposable to the CompositeDisposable
     * @param moviesObservable
     */
    private fun subscribeMovies(moviesObservable: Observable<MovieResponse>) {
        disposable.add(moviesObservable
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<MovieResponse>() {
                override fun onComplete() {}

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

                override fun onNext(movieResponse: MovieResponse) {
                    if(movieResponse.results.isNotEmpty()) {
                        val content: BaseContent = movieResponse.results.get(NumberUtils.getRandomNumberInRange(0, movieResponse.results.size.minus(1)))
                        subscribeMovie(movieDetailViewModel.loadMovie(content.id, "videos,images,credits"))
                    }
                }
            }))
    }
}