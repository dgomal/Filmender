package com.bossdga.filmender.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.bossdga.filmender.R
import com.bossdga.filmender.presentation.ui.fragment.DiscoverFragment
import com.bossdga.filmender.presentation.ui.fragment.WatchListFragment
import com.bossdga.filmender.presentation.viewmodel.BaseViewModel
import com.bossdga.filmender.presentation.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Main Activity that holds several fragments
 */
class MainActivity : BaseActivity<BaseViewModel>() {
    private lateinit var fragmentDiscover: DiscoverFragment
    private lateinit var fragmentWatchList: WatchListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpActionBar(R.string.app_name, false)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.BottomNavigation)
        bottomNavigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        fragmentDiscover = DiscoverFragment()
        fragmentWatchList = WatchListFragment()

        replaceFragment(fragmentDiscover, R.id.FragmentContainer)
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

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.action_discover -> {
                replaceFragment(fragmentDiscover, R.id.FragmentContainer)
                return@OnNavigationItemSelectedListener true
            }
            R.id.action_watchlist -> {
                replaceFragment(fragmentWatchList, R.id.FragmentContainer)
                return@OnNavigationItemSelectedListener true
            }
            else -> false
        }
    }

}