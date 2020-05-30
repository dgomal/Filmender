package com.bossdga.filmender.presentation.ui.activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bossdga.filmender.OnLoadingListener
import com.bossdga.filmender.R
import com.bossdga.filmender.presentation.viewmodel.BaseViewModel
import com.bossdga.filmender.presentation.viewmodel.MovieDetailViewModel


/**
 * Activity that provides the Event details
 */
class MovieDetailActivity : BaseActivity<BaseViewModel>(), OnLoadingListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

    }

    override fun createViewModel(): MovieDetailViewModel {
        return ViewModelProvider(this, factory).get(MovieDetailViewModel::class.java)
    }

    override fun onFinishedLoading(text: String) {
        setUpActionBar(text, true)
    }
}