package com.bossdga.filmender.presentation.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bossdga.filmender.R
import com.bossdga.filmender.model.ApiConfig
import com.bossdga.filmender.model.content.AdType
import com.bossdga.filmender.model.content.LayoutType
import com.bossdga.filmender.presentation.ui.activity.MovieDetailActivity
import com.bossdga.filmender.presentation.ui.activity.TVShowDetailActivity
import com.bossdga.filmender.presentation.viewmodel.MainViewModel
import com.bossdga.filmender.presentation.viewmodel.ViewModelFactory
import com.bossdga.filmender.util.AnalyticsUtils
import com.bossdga.filmender.util.NumberUtils
import com.bossdga.filmender.util.PreferenceUtils
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.analytics.FirebaseAnalytics
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers


/**
 * A simple Fragment that will show a list of movies and tv shows
 */
class DiscoverFragment : BaseFragment() {
    private lateinit var adFrame: FrameLayout

    private lateinit var fragmentMovie: MovieFragment
    private lateinit var fragmentTVShow: TVShowFragment
    private lateinit var mainViewModel: MainViewModel
    private lateinit var shuffleButton: Button
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity(), ViewModelFactory.getInstance(requireActivity().application)).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_discover, container, false)

        progressBar = rootView.findViewById(R.id.progressBar)
        adFrame = rootView.findViewById(R.id.AdFrame)
        mSwipeRefreshLayout = rootView.findViewById(R.id.SwipeRefreshLayout)
        mSwipeRefreshLayout.setOnRefreshListener(onRefreshListener)
        shuffleButton = rootView.findViewById(R.id.ShuffleButton)
        shuffleButton.setOnClickListener { randomizeAndStart() }

        subscribeApiConfig(mainViewModel.loadApiConfig())

        loadFragments()

        observeLoaded(mainViewModel)

        return rootView
    }

    /**
     * Listener for swipe to refresh functionality
     */
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        loadFragments()
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

    private fun loadFragments() {
        showLoading()

        fragmentMovie = MovieFragment.newInstance(LayoutType.SIMPLE)
        fragmentTVShow = TVShowFragment.newInstance(LayoutType.SIMPLE)

        when (PreferenceUtils.getType()) {
            "0" -> {
                addFragment(R.id.FragmentMovie, fragmentMovie, "fragmentMovie")
                addFragment(R.id.FragmentTVShow, fragmentTVShow, "fragmentTVShow")
            }
            "1" -> {
                addFragment(R.id.FragmentMovie, fragmentMovie, "fragmentMovie")
                removeFragment(fragmentTVShow)
            }
            else -> {
                addFragment(R.id.FragmentTVShow, fragmentTVShow, "fragmentTVShow")
                removeFragment(fragmentMovie)
            }
        }

        //refreshAd(AdType.SMALL, adFrame)
    }

    private fun randomizeAndStart() {
        bundle = AnalyticsUtils.selectContent(resources.getResourceEntryName(R.id.ShuffleButton), "action_feeling_lucky", "button")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)

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
                hideLoading()
            }
        })
    }

}