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
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bossdga.filmender.OnImageClickListener
import com.bossdga.filmender.R
import com.bossdga.filmender.model.content.*
import com.bossdga.filmender.presentation.adapter.NetworksAdapter
import com.bossdga.filmender.presentation.adapter.PeopleAdapter
import com.bossdga.filmender.presentation.viewmodel.TVShowDetailViewModel
import com.bossdga.filmender.util.ImageUtils.setImage
import com.bossdga.filmender.util.NumberUtils
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAdView
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
    private lateinit var addFrame: FrameLayout

    private lateinit var peopleAdapter: PeopleAdapter
    private lateinit var networksAdapter: NetworksAdapter
    private lateinit var peopleRecyclerView: RecyclerView
    private lateinit var networksRecyclerView: RecyclerView
    private lateinit var peopleLayoutManager: LinearLayoutManager
    private lateinit var networksLayoutManager: LinearLayoutManager
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

        refreshAd()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_tv_show_detail, container, false)

        tvShowDetailViewModel = ViewModelProvider(requireActivity()).get(TVShowDetailViewModel::class.java)

        addFrame = rootView.findViewById(R.id.AddFrame)

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
        switchViewState(true, addToWatchlist)
        addToWatchlist.setOnClickListener {
            if(addToWatchlist.tag.equals("selected")) {
                switchViewState(false, addToWatchlist)
                tvShowDetailViewModel.deleteTVShow(this.tvShow)
            } else {
                switchViewState(true, addToWatchlist)
                tvShowDetailViewModel.saveTVShow(this.tvShow)
            }
        }

        id = extras?.getIntExtra("id", 0)
        source = extras?.getStringExtra("source")
        if(id == 0) {
            switchViewState(false, addToWatchlist)
            subscribeTVShows(tvShowDetailViewModel.loadTVShows())
        } else {
            subscribeTVShow(tvShowDetailViewModel.loadTVShow(id, true))
        }

        peopleLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        peopleRecyclerView = rootView.findViewById(R.id.peopleRecyclerView)
        peopleRecyclerView.layoutManager = peopleLayoutManager
        peopleAdapter = PeopleAdapter(activity as Context, object : OnImageClickListener {
            override fun onImageClick(people: People) {
                if (people.profilePath != null) {
                    val view = LayoutInflater.from(activity as Context).inflate(R.layout.image_layout, container, false)
                    val imageView: ImageView = view.findViewById(R.id.ProfileImage)
                    setImage(imageView, people.profilePath, ImageType.PROFILE_LARGE)
                    val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(activity as Context)
                    alertDialogBuilder.setView(view).show()
                }
            }
        })
        peopleRecyclerView.setAdapter(peopleAdapter)

        networksLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        networksRecyclerView = rootView.findViewById(R.id.networksRecyclerView)
        networksRecyclerView.layoutManager = networksLayoutManager
        networksAdapter = NetworksAdapter(activity as Context)
        networksRecyclerView.setAdapter(networksAdapter)

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
                    switchViewState(false, addToWatchlist)
                }

                override fun onSuccess(tvShow: TVShow) {
                    renderView(tvShow)
                    tvShowDetailViewModel.loaded.postValue(tvShow.title)
                    hideProgressDialog()
                }
            }))
    }

    private fun renderView(tvShow: TVShow) {
        setImage(image, tvShow.backdropPath, ImageType.BACK_DROP)
        voteAverage.text = tvShow.voteAverage
        numberOfSeasons.text = tvShow.numberOfSeasons.toString().plus(" Seasons")
        date.text = tvShow.releaseDate.substringBefore("-")
        overview.text = tvShow.overview
        genre.text = tvShow.genres.joinToString(separator = " \u2022 ") { it.name }
        if(tvShow.credits.cast.isNotEmpty()) {
            peopleAdapter.setItems(tvShow.credits.cast)
        }
        if(tvShow.networks.isNotEmpty()) {
            networksAdapter.setItems(tvShow.networks)
        }
        if(!tvShow.videos.results.isEmpty()) {
            trailer.visibility = View.VISIBLE
        }
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

    fun switchViewState(select: Boolean, view: View) {
        view as FloatingActionButton
        if(select) {
            view.tag = "selected"
            view.setImageResource(R.mipmap.bookmark_white)
            view.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity as Context, R.color.template_red))
            view.supportImageTintList = ColorStateList.valueOf(ContextCompat.getColor(activity as Context, R.color.white))
        } else {
            view.tag = "not_selected"
            view.setImageResource(R.mipmap.bookmark_red)
            view.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(activity as Context, R.color.white))
            view.supportImageTintList = ColorStateList.valueOf(ContextCompat.getColor(activity as Context, R.color.template_red))
        }
    }

    /**
     * Creates a request for a new native ad based on the boolean parameters and calls the
     * corresponding "populate" method when one is successfully returned.
     *
     */
    private fun refreshAd() {
        val builder = AdLoader.Builder(requireActivity(), getString(R.string.banner_test))

        builder.forUnifiedNativeAd { unifiedNativeAd ->
            // OnUnifiedNativeAdLoadedListener implementation.
            val adView = layoutInflater
                .inflate(R.layout.ad_unified_small, null) as UnifiedNativeAdView
            populateUnifiedNativeAdView(unifiedNativeAd, adView)
            addFrame.removeAllViews()
            addFrame.addView(adView)
        }

        val adOptions = NativeAdOptions.Builder().build()

        builder.withNativeAdOptions(adOptions)

        val adLoader = builder.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(errorCode: Int) {
                Toast.makeText(requireActivity(), "Failed to load native ad: " + errorCode, Toast.LENGTH_SHORT).show()
            }
        }).build()

        adLoader.loadAd(AdRequest.Builder().build())
    }
}