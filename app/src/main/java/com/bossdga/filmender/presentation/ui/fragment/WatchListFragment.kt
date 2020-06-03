package com.bossdga.filmender.presentation.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.bossdga.filmender.R
import com.bossdga.filmender.presentation.viewmodel.MainViewModel
import com.bossdga.filmender.util.PreferenceUtils


/**
 * A simple Fragment that will show a Watch List
 */
class WatchListFragment : BaseFragment() {
    private lateinit var moviesFragment: FragmentContainerView
    private lateinit var showsFragment: FragmentContainerView
    private lateinit var fragmentMovie: MovieFragment
    private lateinit var fragmentTVShow: TVShowFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_watchlist, container, false)

        moviesFragment = rootView.findViewById(R.id.FragmentMovie)
        showsFragment = rootView.findViewById(R.id.FragmentTVShow)

        loadFragments()

        return rootView
    }

    private fun loadFragments() {
        fragmentMovie = childFragmentManager.findFragmentById(R.id.FragmentMovie) as MovieFragment
        fragmentTVShow = childFragmentManager.findFragmentById(R.id.FragmentTVShow) as TVShowFragment

        when (PreferenceUtils.getType(activity as Context)) {
            "0" -> {
                attachFragment(fragmentMovie)
                attachFragment(fragmentTVShow)
                fragmentMovie.refreshFromDB()
                fragmentTVShow.refreshFromDB()
            }
            "1" -> {
                attachFragment(fragmentMovie)
                detachFragment(fragmentTVShow)
                fragmentMovie.refreshFromDB()
            }
            else -> {
                attachFragment(fragmentTVShow)
                detachFragment(fragmentMovie)
                fragmentTVShow.refreshFromDB()
            }
        }
    }

    private inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
        beginTransaction().func().commit()
    }

    private fun addFragment(fragment: Fragment, frameId: Int){
        childFragmentManager.inTransaction { add(frameId, fragment) }
    }

    private fun attachFragment(fragment: Fragment){
        childFragmentManager.inTransaction { attach(fragment) }
    }

    private fun removeFragment(fragment: Fragment) {
        childFragmentManager.inTransaction{ remove(fragment) }
    }

    private fun detachFragment(fragment: Fragment) {
        childFragmentManager.inTransaction{ detach(fragment) }
    }

    private fun replaceFragment(fragment: Fragment, frameId: Int) {
        childFragmentManager.inTransaction{ replace(frameId, fragment) }
    }

}