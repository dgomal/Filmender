package com.bossdga.filmender.presentation.ui.activity

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bossdga.filmender.R
import com.bossdga.filmender.presentation.viewmodel.BaseViewModel
import com.bossdga.filmender.presentation.viewmodel.MovieDetailViewModel


/**
 * Activity that provides the Event details
 */
class MovieDetailActivity : BaseActivity<BaseViewModel>() {
    private lateinit var movieDetailViewModel: MovieDetailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        movieDetailViewModel = ViewModelProvider(this).get(MovieDetailViewModel::class.java)

        observeLoaded(movieDetailViewModel)
    }

    override fun createViewModel(): MovieDetailViewModel {
        return ViewModelProvider(this, factory).get(MovieDetailViewModel::class.java)
    }

    private fun observeLoaded(movieDetailViewModel: MovieDetailViewModel) {
        movieDetailViewModel.loaded.observe(this, Observer {
            it?.let {
                setUpActionBar(it, true)
            }
        })
    }
}