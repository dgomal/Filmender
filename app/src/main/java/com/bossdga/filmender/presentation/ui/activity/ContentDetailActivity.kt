package com.bossdga.filmender.presentation.ui.activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bossdga.filmender.R
import com.bossdga.filmender.presentation.viewmodel.BaseViewModel
import com.bossdga.filmender.presentation.viewmodel.DetailViewModel

/**
 * Activity that provides the Event details
 */
class ContentDetailActivity : BaseActivity<BaseViewModel>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_detail)

        setUpActionBar(R.string.title_activity_event_detail, true)
    }

    override fun createViewModel(): DetailViewModel {
        return ViewModelProvider(this, factory).get(DetailViewModel::class.java)
    }
}