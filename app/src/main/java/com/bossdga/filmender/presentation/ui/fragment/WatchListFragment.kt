package com.bossdga.filmender.presentation.ui.fragment

import android.os.Bundle
import android.view.*
import com.bossdga.filmender.R
import com.bossdga.filmender.util.PreferenceUtils


/**
 * A simple Fragment that will show a Watch List
 */
class WatchListFragment : BaseFragment() {
    private lateinit var fragmentMovie: MovieDBFragment
    private lateinit var fragmentTVShow: TVShowDBFragment

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
        fragmentMovie = childFragmentManager.findFragmentById(R.id.FragmentDBMovie) as MovieDBFragment
        fragmentTVShow = childFragmentManager.findFragmentById(R.id.FragmentDBTVShow) as TVShowDBFragment

        attachFragment(fragmentMovie)
        attachFragment(fragmentTVShow)
        fragmentMovie.refreshContent()
        fragmentTVShow.refreshContent()
    }

}