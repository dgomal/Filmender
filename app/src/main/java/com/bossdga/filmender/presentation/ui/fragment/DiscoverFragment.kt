package com.bossdga.filmender.presentation.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bossdga.filmender.R
import com.bossdga.filmender.model.ApiConfig
import com.bossdga.filmender.model.content.AdType
import com.bossdga.filmender.presentation.ui.activity.MovieDetailActivity
import com.bossdga.filmender.presentation.ui.activity.TVShowDetailActivity
import com.bossdga.filmender.presentation.viewmodel.MainViewModel
import com.bossdga.filmender.util.NumberUtils
import com.bossdga.filmender.util.PreferenceUtils
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers


/**
 * A simple Fragment that will show a list of movies and tv shows
 */
class DiscoverFragment : BaseFragment() {
    private lateinit var addFrame: FrameLayout

    private lateinit var fragmentMovie: MovieFragment
    private lateinit var fragmentTVShow: TVShowFragment
    private lateinit var mainViewModel: MainViewModel
    private lateinit var shuffleButton: Button
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_discover, container, false)

        addFrame = rootView.findViewById(R.id.AddFrame)
        mSwipeRefreshLayout = rootView.findViewById(R.id.SwipeRefreshLayout)
        mSwipeRefreshLayout.setOnRefreshListener(onRefreshListener)
        shuffleButton = rootView.findViewById(R.id.ShuffleButton)
        shuffleButton.setOnClickListener { randomizeAndStart() }

        subscribeApiConfig(mainViewModel.loadApiConfig())

        loadFragments()

        observeLoaded(mainViewModel)

        refreshAd()

        return rootView
    }

    /**
     * Listener for swipe to refresh functionality
     */
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        loadFragments()
    }

    override fun onDestroyView() {
        currentNativeAd?.destroy()
        super.onDestroyView()

        disposable.clear()
    }

    override fun onDestroy() {
        super.onDestroy()

        disposable.dispose()
    }

    private fun loadFragments() {
        fragmentMovie = childFragmentManager.findFragmentById(R.id.FragmentMovie) as MovieFragment
        fragmentTVShow = childFragmentManager.findFragmentById(R.id.FragmentTVShow) as TVShowFragment

        when (PreferenceUtils.getType()) {
            "0" -> {
                attachFragment(fragmentMovie)
                attachFragment(fragmentTVShow)
                fragmentMovie.refreshContent()
                fragmentTVShow.refreshContent()
            }
            "1" -> {
                attachFragment(fragmentMovie)
                detachFragment(fragmentTVShow)
                fragmentMovie.refreshContent()
            }
            else -> {
                attachFragment(fragmentTVShow)
                detachFragment(fragmentMovie)
                fragmentTVShow.refreshContent()
            }
        }
    }

    private fun randomizeAndStart() {
        val random: Int = NumberUtils.getRandomNumberInRange(1, 2)

        when (PreferenceUtils.getType()) {
            "0" -> {
                val intent: Intent = if(random == 1) {
                    Intent(requireActivity(), MovieDetailActivity::class.java)
                } else {
                    Intent(requireActivity(), TVShowDetailActivity::class.java)
                }
                intent.putExtra("id", 0)
                startActivity(intent)
            }
            "1" -> {
                val intent = Intent(requireActivity(), MovieDetailActivity::class.java)
                intent.putExtra("id", 0)
                startActivity(intent)
            }
            "2" -> {
                val intent = Intent(requireActivity(), TVShowDetailActivity::class.java)
                intent.putExtra("id", 0)
                startActivity(intent)
            }
        }
    }

    /**
     * Method that adds a Disposable to the CompositeDisposable
     * @param moviesObservable
     */
    private fun subscribeApiConfig(apiConfigObservable: Observable<ApiConfig>) {
        disposable.add(apiConfigObservable
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<ApiConfig>() {
                override fun onComplete() {}

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

                override fun onNext(apiConfig: ApiConfig) {
                    PreferenceUtils.setImageUrl(apiConfig.images.secureBaseUrl)
                }
            }))
    }

    private fun observeLoaded(mainViewModel: MainViewModel) {
        mainViewModel.loaded.observe(requireActivity(), Observer {
            it?.let {
                mSwipeRefreshLayout.isRefreshing = !it.toBoolean()
                shuffleButton.visibility = View.VISIBLE
            }
        })
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
            val adView = layoutInflater.inflate(R.layout.ad_unified_small, null) as UnifiedNativeAdView
            populateUnifiedNativeAdView(unifiedNativeAd, adView, AdType.SMALL)
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