package com.bossdga.filmender.presentation.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bossdga.filmender.OnLoadingListener
import com.bossdga.filmender.R
import com.bossdga.filmender.model.ApiConfig
import com.bossdga.filmender.model.content.*
import com.bossdga.filmender.presentation.ui.fragment.MovieFragment
import com.bossdga.filmender.presentation.ui.fragment.TVShowFragment
import com.bossdga.filmender.presentation.viewmodel.BaseViewModel
import com.bossdga.filmender.presentation.viewmodel.MainViewModel
import com.bossdga.filmender.util.NumberUtils
import com.bossdga.filmender.util.PreferenceUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers


/**
 * Main Activity that holds several fragments
 */
class MainActivity : BaseActivity<BaseViewModel>(), OnLoadingListener {
    private lateinit var moviesFragment: FragmentContainerView
    private lateinit var showsFragment: FragmentContainerView
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var fragmentMovie: MovieFragment
    private lateinit var fragmentTVShow: TVShowFragment
    private var disposable = CompositeDisposable()
    private lateinit var mainViewModel: MainViewModel
    private var random: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpActionBar(R.string.title_activity_main, false)

        val fab: View = findViewById(R.id.fab)
        mSwipeRefreshLayout = findViewById(R.id.SwipeRefreshLayout)
        mSwipeRefreshLayout.setOnRefreshListener(onRefreshListener)
        moviesFragment = findViewById(R.id.FragmentMovie)
        showsFragment = findViewById(R.id.FragmentTVShow)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.BottomNavigation)
        bottomNavigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        subscribeApiConfig(mainViewModel.loadApiConfig())

        fab.setOnClickListener { view ->
            random = NumberUtils.getRandomNumberInRange(1, 2)

            if(random == 1) {
                subscribeMovies(mainViewModel.loadMovies(PreferenceUtils.getYearFrom(this),
                    PreferenceUtils.getYearTo(this),
                    PreferenceUtils.getRating(this),
                    PreferenceUtils.getGenres(this)))
            } else {
                subscribeTVShows(mainViewModel.loadTVShows(PreferenceUtils.getYearFrom(this),
                    PreferenceUtils.getYearTo(this),
                    PreferenceUtils.getRating(this),
                    PreferenceUtils.getGenres(this)))
            }

        }

        loadFragments()
    }

    override fun onDestroy() {
        super.onDestroy()

        disposable.clear()
        disposable.dispose()
    }

    /**
     * Listener for swipe to refresh functionality
     */
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        loadFragments()
    }

    override fun createViewModel(): MainViewModel {
        return ViewModelProvider(this, factory).get(MainViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, FilterSettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadFragments() {
        fragmentMovie = supportFragmentManager.findFragmentById(R.id.FragmentMovie) as MovieFragment
        fragmentTVShow = supportFragmentManager.findFragmentById(R.id.FragmentTVShow) as TVShowFragment

        val type: String? = PreferenceUtils.getType(this)

        when (type) {
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

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.action_discover -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.action_watchlist -> {
                return@OnNavigationItemSelectedListener true
            }
            else -> false
        }
    }

    override fun onFinishedLoading(text: String) {
        mSwipeRefreshLayout.isRefreshing = false
    }

    private fun randomizeAndStart(id: Int) {
        val type: String? = PreferenceUtils.getType(this)

        when (type) {
            "0" -> {
                val intent: Intent

                if(random == 1) {
                    intent = Intent(this@MainActivity, MovieDetailActivity::class.java)
                } else {
                    intent = Intent(this@MainActivity, TVShowDetailActivity::class.java)
                }
                intent.putExtra("id", id)
                startActivity(intent)
            }
            "1" -> {
                val intent = Intent(this@MainActivity, MovieDetailActivity::class.java)
                intent.putExtra("id", id)
                startActivity(intent)
            }
            else -> {
                val intent = Intent(this@MainActivity, TVShowDetailActivity::class.java)
                intent.putExtra("id", id)
                startActivity(intent)
            }
        }
    }

    private inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
        beginTransaction().func().commit()
    }

    private fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int){
        supportFragmentManager.inTransaction { add(frameId, fragment) }
    }

    private fun AppCompatActivity.attachFragment(fragment: Fragment){
        supportFragmentManager.inTransaction { attach(fragment) }
    }

    private fun AppCompatActivity.removeFragment(fragment: Fragment) {
        supportFragmentManager.inTransaction{ remove(fragment) }
    }

    private fun AppCompatActivity.detachFragment(fragment: Fragment) {
        supportFragmentManager.inTransaction{ detach(fragment) }
    }

    private fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
        supportFragmentManager.inTransaction{ replace(frameId, fragment) }
    }

    /**
     * Method that adds a Disposable to the CompositeDisposable
     * @param moviesObservable
     */
    private fun subscribeMovies(moviesObservable: Observable<MovieResponse>) {
        disposable.add(moviesObservable
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<MovieResponse>() {
                override fun onComplete() {}

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

                override fun onNext(movieResponse: MovieResponse) {
                    if(movieResponse.results.isNotEmpty()) {
                        val content: BaseContent = movieResponse.results.get(NumberUtils.getRandomNumberInRange(0, movieResponse.results.size.minus(1)))
                        randomizeAndStart(content.id)
                    }
                }
            }))
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
                        randomizeAndStart(content.id)
                    }
                }
            }))
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
                    PreferenceUtils.setImageUrl(this@MainActivity, apiConfig.images.secureBaseUrl)
                }
            }))
    }
}