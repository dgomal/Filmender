package com.bossdga.filmender.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.bossdga.filmender.R
import com.bossdga.filmender.presentation.viewmodel.BaseViewModel
import com.bossdga.filmender.presentation.viewmodel.MainViewModel


class FilterSettingsActivity : BaseActivity<BaseViewModel>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setUpActionBar(R.string.title_activity_settings, true)
    }

    override fun createViewModel(): MainViewModel {
        return ViewModelProvider(this, factory).get(MainViewModel::class.java)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this, MainActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        finish()
        startActivity(Intent(this, MainActivity::class.java))
    }
}