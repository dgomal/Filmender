package com.bossdga.filmender.presentation.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.bossdga.filmender.R
import com.bossdga.filmender.model.ApiConfig
import com.bossdga.filmender.presentation.ui.activity.MovieDetailActivity
import com.bossdga.filmender.presentation.ui.activity.TVShowDetailActivity
import com.bossdga.filmender.presentation.viewmodel.MainViewModel
import com.bossdga.filmender.util.NumberUtils
import com.bossdga.filmender.util.PreferenceUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

/**
 * A simple Fragment that will show a list of movies and tv shows
 */
class DiscoverFragment : BaseFragment() {
    private lateinit var moviesFragment: FragmentContainerView
    private lateinit var showsFragment: FragmentContainerView
    private lateinit var fragmentMovie: MovieFragment
    private lateinit var fragmentTVShow: TVShowFragment
    private lateinit var mainViewModel: MainViewModel
    private lateinit var reloadButton: View
    private lateinit var shuffleButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_discover, container, false)

        reloadButton = rootView.findViewById(R.id.ReloadButton)
        reloadButton.setOnClickListener { loadFragments() }
        shuffleButton = rootView.findViewById(R.id.ShuffleButton)
        shuffleButton.setOnClickListener { randomizeAndStart() }
        moviesFragment = rootView.findViewById(R.id.FragmentMovie)
        showsFragment = rootView.findViewById(R.id.FragmentTVShow)

        subscribeApiConfig(mainViewModel.loadApiConfig())

        loadFragments()

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

    private fun loadFragments() {
        fragmentMovie = childFragmentManager.findFragmentById(R.id.FragmentMovie) as MovieFragment
        fragmentTVShow = childFragmentManager.findFragmentById(R.id.FragmentTVShow) as TVShowFragment

        when (PreferenceUtils.getType(activity as Context)) {
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

        when (PreferenceUtils.getType(requireActivity())) {
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
                    PreferenceUtils.setImageUrl(activity as Context, apiConfig.images.secureBaseUrl)
                }
            }))
    }

}