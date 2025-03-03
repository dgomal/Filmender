package com.bossdga.filmender.presentation.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bossdga.filmender.R
import com.bossdga.filmender.model.content.AdType
import com.bossdga.filmender.presentation.viewmodel.MainViewModel


/**
 * A simple Fragment that will show a Watch List
 */
class WatchListFragment : BaseFragment() {
    private lateinit var adFrame: FrameLayout

    private lateinit var fragmentMovie: MovieDBFragment
    private lateinit var fragmentTVShow: TVShowDBFragment
    private lateinit var mainViewModel: MainViewModel
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var empty: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_watchlist, container, false)

        adFrame = rootView.findViewById(R.id.AdFrame)

        empty = rootView.findViewById(R.id.empty)
        mSwipeRefreshLayout = rootView.findViewById(R.id.SwipeRefreshLayout)
        mSwipeRefreshLayout.setOnRefreshListener(onRefreshListener)

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    override fun onDestroy() {
        currentNativeAd?.destroy()
        super.onDestroy()
    }

    private fun loadFragments() {
        fragmentMovie = childFragmentManager.findFragmentById(R.id.FragmentDBMovie) as MovieDBFragment
        fragmentTVShow = childFragmentManager.findFragmentById(R.id.FragmentDBTVShow) as TVShowDBFragment

        attachFragment(fragmentMovie)
        attachFragment(fragmentTVShow)
        fragmentMovie.refreshContent()
        fragmentTVShow.refreshContent()

        refreshAd(AdType.SMALL, adFrame)
    }

    private fun observeLoaded(mainViewModel: MainViewModel) {
        mainViewModel.loadedDB.observe(requireActivity(), Observer {
            it?.let {
                mSwipeRefreshLayout.isRefreshing = !it.toBoolean()
                if(!fragmentMovie.isEmpty() || !fragmentTVShow.isEmpty()) {
                    adFrame.visibility = View.VISIBLE
                    empty.visibility = View.GONE
                } else {
                    adFrame.visibility = View.GONE
                    empty.visibility = View.VISIBLE
                }
            }
        })
    }

}