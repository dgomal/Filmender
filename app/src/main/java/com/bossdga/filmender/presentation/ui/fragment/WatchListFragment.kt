package com.bossdga.filmender.presentation.ui.fragment

import android.os.Bundle
import android.view.*
import com.bossdga.filmender.R
import com.bossdga.filmender.util.PreferenceUtils


/**
 * A simple Fragment that will show a Watch List
 */
class WatchListFragment : BaseFragment() {
    private lateinit var fragmentMovie: MovieFragment
    private lateinit var fragmentTVShow: TVShowFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_watchlist, container, false)

        loadFragments()

        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    private fun loadFragments() {
        fragmentMovie = childFragmentManager.findFragmentById(R.id.FragmentMovie) as MovieFragment
        fragmentTVShow = childFragmentManager.findFragmentById(R.id.FragmentTVShow) as TVShowFragment

        when (PreferenceUtils.getType()) {
            "0" -> {
                attachFragment(fragmentMovie)
                attachFragment(fragmentTVShow)
                fragmentMovie.refreshContent(true)
                fragmentTVShow.refreshContent(true)
            }
            "1" -> {
                attachFragment(fragmentMovie)
                detachFragment(fragmentTVShow)
                fragmentMovie.refreshContent(true)
            }
            else -> {
                attachFragment(fragmentTVShow)
                detachFragment(fragmentMovie)
                fragmentTVShow.refreshContent(true)
            }
        }
    }

}