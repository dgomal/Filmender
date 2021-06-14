package com.bossdga.filmender.presentation.ui.activity

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bossdga.filmender.R
import com.bossdga.filmender.presentation.viewmodel.BaseViewModel
import com.bossdga.filmender.presentation.viewmodel.TVShowDetailViewModel


/**
 * Activity that provides the Event details
 */
class TVShowDetailActivity : BaseActivity<BaseViewModel>() {
    private lateinit var tvShowDetailViewModel: TVShowDetailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tv_show_detail)

        tvShowDetailViewModel = viewModel as TVShowDetailViewModel

        observeLoaded(tvShowDetailViewModel)
    }

    override fun createViewModel(): TVShowDetailViewModel {
        return ViewModelProvider(this, factory).get(TVShowDetailViewModel::class.java)
    }

    private fun observeLoaded(tvShowDetailViewModel: TVShowDetailViewModel) {
        tvShowDetailViewModel.loaded.observe(this, Observer {
            it?.let {
                setUpActionBar(it, true)
            }
        })
    }
}