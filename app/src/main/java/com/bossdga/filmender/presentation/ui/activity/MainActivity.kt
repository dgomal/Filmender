package com.bossdga.filmender.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.bossdga.filmender.R
import com.bossdga.filmender.presentation.ui.fragment.DiscoverFragment
import com.bossdga.filmender.presentation.ui.fragment.WatchListFragment
import com.bossdga.filmender.presentation.viewmodel.BaseViewModel
import com.bossdga.filmender.presentation.viewmodel.MainViewModel
import com.bossdga.filmender.util.NumberUtils
import com.bossdga.filmender.util.PreferenceUtils
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

        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener {
            randomizeAndStart()
        }

        fragmentDiscover = DiscoverFragment()
        fragmentWatchList = WatchListFragment()

        replaceFragment(fragmentDiscover, R.id.FragmentContainer)
    }

    private fun randomizeAndStart() {
        val random: Int = NumberUtils.getRandomNumberInRange(1, 2)

        when (PreferenceUtils.getType(this)) {
            "0" -> {
                val intent: Intent = if(random == 1) {
                    Intent(this@MainActivity, MovieDetailActivity::class.java)
                } else {
                    Intent(this@MainActivity, TVShowDetailActivity::class.java)
                }
                intent.putExtra("id", 0)
                startActivity(intent)
            }
            "1" -> {
                val intent = Intent(this@MainActivity, MovieDetailActivity::class.java)
                intent.putExtra("id", 0)
                startActivity(intent)
            }
            "2" -> {
                val intent = Intent(this@MainActivity, TVShowDetailActivity::class.java)
                intent.putExtra("id", 0)
                startActivity(intent)
            }
        }
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

}