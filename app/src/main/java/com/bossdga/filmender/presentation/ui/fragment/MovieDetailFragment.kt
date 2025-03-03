package com.bossdga.filmender.presentation.ui.fragment

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bossdga.filmender.OnImageClickListener
import com.bossdga.filmender.R
import com.bossdga.filmender.model.content.*
import com.bossdga.filmender.presentation.adapter.PeopleAdapter
import com.bossdga.filmender.presentation.viewmodel.MovieDetailViewModel
import com.bossdga.filmender.util.AnalyticsUtils
import com.bossdga.filmender.util.DateUtils
import com.bossdga.filmender.util.ImageUtils.setImage
import com.bossdga.filmender.util.NumberUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.analytics.FirebaseAnalytics
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers


/**
 * A simple Fragment that will show a movie
 */
class MovieDetailFragment : BaseFragment() {
    private lateinit var adFrame: FrameLayout

    private lateinit var peopleAdapter: PeopleAdapter
    private lateinit var peopleRecyclerView: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var movieDetailViewModel: MovieDetailViewModel
    private var id: Int? = 0
    private var source: String? = ""

    private lateinit var image: ImageView
    private lateinit var voteAverage: TextView
    private lateinit var date: TextView
    private lateinit var overview: TextView
    private lateinit var genre: TextView
    private lateinit var runtime: TextView
    private lateinit var trailer: Button
    private lateinit var addToWatchlist: FloatingActionButton

    private lateinit var movie: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showProgressDialog()

        movieDetailViewModel = ViewModelProvider(requireActivity()).get(MovieDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false)

        adFrame = rootView.findViewById(R.id.AdFrame)

        image = requireActivity().findViewById(R.id.image)
        voteAverage = rootView.findViewById(R.id.voteAverage)
        date = rootView.findViewById(R.id.date)
        overview = rootView.findViewById(R.id.overview)
        genre = rootView.findViewById(R.id.genre)
        runtime = rootView.findViewById(R.id.runtime)
        trailer = rootView.findViewById(R.id.TrailerButton)
        trailer.setOnClickListener {
            if(!movie.videos.results.isEmpty()) {
                watchYoutubeVideo(movie.videos.results.get(0).key)
            }
        }
        addToWatchlist = requireActivity().findViewById(R.id.AddToWatchlist)
        switchViewState(true, addToWatchlist)
        addToWatchlist.setOnClickListener {
            val itemName: String
            if(addToWatchlist.tag.equals("selected")) {
                switchViewState(false, addToWatchlist)
                movieDetailViewModel.deleteMovie(this.movie)
                itemName = "action_add_movie_watchlist"
            } else {
                switchViewState(true, addToWatchlist)
                movieDetailViewModel.saveMovie(this.movie)
                itemName = "action_delete_movie_watchlist"
            }
            bundle = AnalyticsUtils.selectContent(resources.getResourceEntryName(R.id.AddToWatchlist), itemName, "button")
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        }

        id = extras?.getIntExtra("id", 0)
        source = extras?.getStringExtra("source")
        if(id == 0) {
            switchViewState(false, addToWatchlist)
            subscribeMovies(movieDetailViewModel.loadMovies())
        } else {
            subscribeMovie(movieDetailViewModel.loadMovie(id, true))
        }

        linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        peopleRecyclerView = rootView.findViewById(R.id.peopleRecyclerView)
        peopleRecyclerView.isNestedScrollingEnabled = false
        peopleRecyclerView.layoutManager = linearLayoutManager
        peopleAdapter = PeopleAdapter(activity as Context, object : OnImageClickListener {
            override fun onImageClick(people: People) {
                if (people.profilePath != null) {
                    val view = LayoutInflater.from(activity as Context).inflate(R.layout.image_layout, container, false)
                    val imageView: ImageView = view.findViewById(R.id.ProfileImage)
                    setImage(imageView, people.profilePath, ImageType.PROFILE_LARGE)
                    val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(activity as Context)
                    alertDialogBuilder.setView(view).show()

                    bundle = AnalyticsUtils.selectContent(resources.getResourceEntryName(R.id.ProfileImage), "action_view_movie_cast", "image_view")
                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
                }
            }
        })
        peopleRecyclerView.setAdapter(peopleAdapter)

        refreshAd(AdType.MEDIUM, adFrame)

        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()

        disposable.clear()
    }

    override fun onDestroy() {
        currentNativeAd?.destroy()
        super.onDestroy()

        disposable.dispose()
    }

    /**
     * Method that adds a Disposable to the CompositeDisposable
     * @param moviesObservable
     */
    private fun subscribeMovie(movieObservable: Single<Movie>) {
        disposable.add(movieObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<Movie>() {
                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    subscribeMovie(movieDetailViewModel.loadMovie(id, false))
                    switchViewState(false, addToWatchlist)
                }

                override fun onSuccess(movie: Movie) {
                    renderView(movie)
                    movieDetailViewModel.loaded.postValue(movie.title)
                    hideProgressDialog()
                }
            }))
    }

    private fun renderView(movie: Movie) {
        setImage(image, movie.backdropPath, ImageType.BACK_DROP)
        voteAverage.text = movie.voteAverage
        runtime.text = DateUtils.fromMinutesToHHmm(movie.runtime)
        date.text = movie.releaseDate.substringBefore("-")
        overview.text = movie.overview
        genre.text = movie.genres.joinToString(separator = " \u2022 ") { it.name }
        peopleAdapter.setItems(movie.credits.cast)
        if(!movie.videos.results.isEmpty()) {
            trailer.visibility = View.VISIBLE
        }
        this.movie = movie
    }

    private fun watchYoutubeVideo(id: String) {
        bundle = AnalyticsUtils.selectContent(resources.getResourceEntryName(R.id.TrailerButton), "action_watch_movie_trailer", "button")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)

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
                        subscribeMovie(movieDetailViewModel.loadMovie(content.id, false))
                    }
                }
            }))
    }

    fun switchViewState(select: Boolean, view: View) {
        view as FloatingActionButton
        if(select) {
            view.tag = "selected"
            view.setImageResource(R.drawable.bookmark_white)
            view.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity as Context, R.color.template_red))
            view.supportImageTintList = ColorStateList.valueOf(ContextCompat.getColor(activity as Context, R.color.white))
        } else {
            view.tag = "not_selected"
            view.setImageResource(R.drawable.bookmark_red)
            view.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity as Context, R.color.white))
            view.supportImageTintList = ColorStateList.valueOf(ContextCompat.getColor(activity as Context, R.color.template_red))
        }
    }
}