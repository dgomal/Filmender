package com.bossdga.filmender.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
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
class MainActivity : BaseActivity<BaseViewModel>() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var moviesHeader: TextView
    private lateinit var moviesFragment: FragmentContainerView
    private lateinit var showsHeader: TextView
    private lateinit var showsFragment: FragmentContainerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpActionBar(R.string.title_activity_main, false)

        val tryAgainButton: Button = findViewById(R.id.TryAgainButton)
        val shuffleButton: Button = findViewById(R.id.ShuffleButton)
        moviesHeader = findViewById(R.id.MoviesHeader)
        moviesFragment = findViewById(R.id.FragmentMovie)
        showsHeader = findViewById(R.id.ShowsHeader)
        showsFragment = findViewById(R.id.FragmentTVShow)

        tryAgainButton.setOnClickListener {
            val fragmentMovie = supportFragmentManager.findFragmentById(R.id.FragmentMovie) as MovieFragment
            fragmentMovie.refreshContent()
            val fragmentTVShow = supportFragmentManager.findFragmentById(R.id.FragmentTVShow) as TVShowFragment
            fragmentTVShow.refreshContent()

            setVisibility()
        }

        val bottomNavigation: BottomNavigationView = findViewById(R.id.BottomNavigation)
        bottomNavigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
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

    private fun setVisibility() {
        val type: String? = PreferenceUtils.getType(this)

        when (type) {
            "0" -> {
                moviesHeader.visibility = View.VISIBLE
                moviesFragment.visibility = View.VISIBLE
                showsHeader.visibility = View.VISIBLE
                showsFragment.visibility = View.VISIBLE
            }
            "1" -> {
                moviesHeader.visibility = View.VISIBLE
                moviesFragment.visibility = View.VISIBLE
                showsHeader.visibility = View.GONE
                showsFragment.visibility = View.GONE
            }
            else -> {
                moviesHeader.visibility = View.GONE
                moviesFragment.visibility = View.GONE
                showsHeader.visibility = View.VISIBLE
                showsFragment.visibility = View.VISIBLE
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
}