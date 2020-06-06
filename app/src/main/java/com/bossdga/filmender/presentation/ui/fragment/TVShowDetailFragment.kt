package com.bossdga.filmender.presentation.ui.fragment

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bossdga.filmender.OnImageClickListener
import com.bossdga.filmender.R
import com.bossdga.filmender.model.content.*
import com.bossdga.filmender.presentation.adapter.PeopleAdapter
import com.bossdga.filmender.presentation.viewmodel.TVShowDetailViewModel
import com.bossdga.filmender.util.ImageUtils.setImage
import com.bossdga.filmender.util.NumberUtils
import com.bossdga.filmender.util.PreferenceUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers


/**
 * A simple Fragment that will show a tv show
 */
class TVShowDetailFragment : BaseFragment() {
    private lateinit var adapter: PeopleAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var tvShowDetailViewModel: TVShowDetailViewModel
    private var id: Int? = 0
    private var source: String? = ""

    private lateinit var image: ImageView
    private lateinit var voteAverage: TextView
    private lateinit var date: TextView
    private lateinit var overview: TextView
    private lateinit var genre: TextView
    private lateinit var numberOfSeasons: TextView
    private lateinit var trailer: Button
    private lateinit var addToWatchlist: FloatingActionButton

    private lateinit var tvShow: TVShow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showProgressDialog()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_tv_show_detail, container, false)

        image = requireActivity().findViewById(R.id.image)
        voteAverage = rootView.findViewById(R.id.voteAverage)
        date = rootView.findViewById(R.id.date)
        overview = rootView.findViewById(R.id.overview)
        genre = rootView.findViewById(R.id.genre)
        numberOfSeasons = rootView.findViewById(R.id.numberOfSeasons)
        trailer = rootView.findViewById(R.id.TrailerButton)
        trailer.setOnClickListener {
            if(!tvShow.videos.results.isEmpty()) {
                watchYoutubeVideo(tvShow.videos.results.get(0).key)
            }
        }
        addToWatchlist = requireActivity().findViewById(R.id.AddToWatchlist)
        addToWatchlist.setOnClickListener { tvShowDetailViewModel.saveTVShow(this.tvShow) }

        tvShowDetailViewModel = ViewModelProvider(requireActivity()).get(TVShowDetailViewModel::class.java)
        id = extras?.getIntExtra("id", 0)
        source = extras?.getStringExtra("source")
        if(id == 0) {
            subscribeTVShows(tvShowDetailViewModel.loadTVShows(false))
        } else {
            subscribeTVShow(tvShowDetailViewModel.loadTVShow(id, true))
        }

        mRecyclerView = rootView.findViewById(R.id.recyclerView)
        linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager)
        adapter = PeopleAdapter(activity as Context, object : OnImageClickListener {
            override fun onImageClick(people: People) {
                if (people.profilePath != null) {
                    val view = LayoutInflater.from(activity as Context).inflate(R.layout.image_layout, container, false)
                    val imageView: ImageView = view.findViewById(R.id.ProfileImage)
                    setImage(activity as Context, imageView, people.profilePath, ImageType.BACK_DROP)
                    val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(activity as Context)
                    alertDialogBuilder.setView(view).show()
                }
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
    private fun subscribeTVShow(tvShowObservable: Single<TVShow>) {
        disposable.add(tvShowObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<TVShow>() {
                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    subscribeTVShow(tvShowDetailViewModel.loadTVShow(id, false))
                }

                override fun onSuccess(tvShow: TVShow) {
                    renderView(tvShow)
                    tvShowDetailViewModel.loaded.postValue(tvShow.title)
                    hideProgressDialog()
                }
            }))
    }

    private fun renderView(tvShow: TVShow) {
        setImage(activity as Context, image, tvShow.backdropPath, ImageType.BACK_DROP)
        voteAverage.text = tvShow.voteAverage
        numberOfSeasons.text = tvShow.numberOfSeasons.toString().plus(" Seasons")
        date.text = tvShow.releaseDate.substringBefore("-")
        overview.text = tvShow.overview
        genre.text = tvShow.genres.joinToString(separator = " \u2022 ") { it.name }
        adapter.setItems(tvShow.credits.cast)
        this.tvShow = tvShow
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
     * @param tvShowsObservable
     */
    private fun subscribeTVShows(tvShowsObservable: Observable<TVShowResponse>) {
        disposable.add(tvShowsObservable
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<TVShowResponse>() {
                override fun onComplete() {}

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

                override fun onNext(tvShowResponse: TVShowResponse) {
                    if(tvShowResponse.results.isNotEmpty()) {
                        val content: BaseContent = tvShowResponse.results.get(NumberUtils.getRandomNumberInRange(0, tvShowResponse.results.size.minus(1)))
                        subscribeTVShow(tvShowDetailViewModel.loadTVShow(content.id, false))
                    }
                }
            }))
    }
}