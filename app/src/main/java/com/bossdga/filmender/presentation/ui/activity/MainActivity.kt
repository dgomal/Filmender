package com.bossdga.filmender.presentation.ui.activity

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
import com.bossdga.filmender.presentation.ui.fragment.MovieFragment
import com.bossdga.filmender.presentation.ui.fragment.TVShowFragment
import com.bossdga.filmender.presentation.viewmodel.BaseViewModel
import com.bossdga.filmender.presentation.viewmodel.MainViewModel
import com.bossdga.filmender.util.PreferenceUtils
import com.google.android.material.bottomnavigation.BottomNavigationView


/**
 * Main Activity that holds several fragments
 */
class MainActivity : BaseActivity<BaseViewModel>(), OnLoadingListener {
    private lateinit var moviesFragment: FragmentContainerView
    private lateinit var showsFragment: FragmentContainerView
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var fragmentMovie: MovieFragment
    private lateinit var fragmentTVShow: TVShowFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpActionBar(R.string.title_activity_main, false)

        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            val intent = Intent(this, MovieDetailActivity::class.java)
            startActivity(intent)
        }
        mSwipeRefreshLayout = findViewById(R.id.SwipeRefreshLayout)
        mSwipeRefreshLayout.setOnRefreshListener(onRefreshListener)
        moviesFragment = findViewById(R.id.FragmentMovie)
        showsFragment = findViewById(R.id.FragmentTVShow)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.BottomNavigation)
        bottomNavigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
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

    private inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
        beginTransaction().func().commit()
    }

    fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int){
        supportFragmentManager.inTransaction { add(frameId, fragment) }
    }

    fun AppCompatActivity.attachFragment(fragment: Fragment){
        supportFragmentManager.inTransaction { attach(fragment) }
    }

    fun AppCompatActivity.removeFragment(fragment: Fragment) {
        supportFragmentManager.inTransaction{ remove(fragment) }
    }

    fun AppCompatActivity.detachFragment(fragment: Fragment) {
        supportFragmentManager.inTransaction{ detach(fragment) }
    }

    fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
        supportFragmentManager.inTransaction{ replace(frameId, fragment) }
    }
}