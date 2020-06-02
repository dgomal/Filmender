package com.bossdga.filmender.presentation.ui.fragment

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bossdga.filmender.OnImageClickListener
import com.bossdga.filmender.R
import com.bossdga.filmender.model.content.*
import com.bossdga.filmender.presentation.adapter.PeopleAdapter
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
    private lateinit var adapter: PeopleAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var movieDetailViewModel: MovieDetailViewModel
    private var id: Int? = 0

    private lateinit var image: ImageView
    private lateinit var voteAverage: TextView
    private lateinit var date: TextView
    private lateinit var overview: TextView
    private lateinit var genre: TextView
    private lateinit var runtime: TextView
    private lateinit var trailer: ImageView

    private lateinit var movie: Movie

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
        runtime = rootView.findViewById(R.id.runtime)
        trailer = rootView.findViewById(R.id.TrailerImage)
        trailer.setOnClickListener {
            if(!movie.videos.results.isEmpty()) {
                watchYoutubeVideo(movie.videos.results.get(0).key)
            }
        }

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

        mRecyclerView = rootView.findViewById(R.id.recyclerView)
        gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        mRecyclerView.setLayoutManager(gridLayoutManager)
        adapter = PeopleAdapter(activity as Context, object : OnImageClickListener {
            override fun onImageClick(people: People) {
                val view = LayoutInflater.from(activity as Context).inflate(R.layout.image_layout, container, false)
                val imageView: ImageView = view.findViewById(R.id.ProfileImage)
                setImage(activity as Context, imageView, people.profilePath, ImageType.BACK_DROP)
                val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(activity as Context)
                alertDialogBuilder.setView(view).show()
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
        genre.text = movie.genres.joinToString(separator = " \u2022 ") { it.name }
        adapter.setItems(movie.credits.cast)
        this.movie = movie
    }

    private fun watchYoutubeVideo(id: String) {
        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=$id"))
        try {
            startActivity(appIntent)
        } catch (ex: ActivityNotFoundException) {
            startActivity(webIntent)
        }
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