package com.bossdga.filmender.presentation.ui.activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bossdga.filmender.R
import com.bossdga.filmender.presentation.viewmodel.BaseViewModel
import com.bossdga.filmender.presentation.viewmodel.MovieDetailViewModel
import com.bossdga.filmender.presentation.viewmodel.TVShowDetailViewModel


/**
 * Activity that provides the Event details
 */
class TVShowDetailActivity : BaseActivity<BaseViewModel>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tv_show_detail)

        setUpActionBar(R.string.title_activity_event_detail, true)
    }

    override fun createViewModel(): TVShowDetailViewModel {
        return ViewModelProvider(this, factory).get(TVShowDetailViewModel::class.java)
    }
}