package com.bossdga.filmender.presentation.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.Toast
import com.bossdga.filmender.R
import com.bossdga.filmender.util.PreferenceUtils
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAdView


/**
 * A simple Fragment that will show a Watch List
 */
class WatchListFragment : BaseFragment() {
    private lateinit var addFrame: FrameLayout

    private lateinit var fragmentMovie: MovieDBFragment
    private lateinit var fragmentTVShow: TVShowDBFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        refreshAd()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_watchlist, container, false)

        addFrame = rootView.findViewById(R.id.AddFrame)

        loadFragments()

        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    private fun loadFragments() {
        fragmentMovie = childFragmentManager.findFragmentById(R.id.FragmentDBMovie) as MovieDBFragment
        fragmentTVShow = childFragmentManager.findFragmentById(R.id.FragmentDBTVShow) as TVShowDBFragment

        attachFragment(fragmentMovie)
        attachFragment(fragmentTVShow)
        fragmentMovie.refreshContent()
        fragmentTVShow.refreshContent()
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
                .inflate(R.layout.ad_unified, null) as UnifiedNativeAdView
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