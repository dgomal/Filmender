package com.bossdga.filmender.presentation.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bossdga.filmender.presentation.viewmodel.BaseViewModel
import com.bossdga.filmender.presentation.viewmodel.ViewModelFactory
import com.bossdga.filmender.R

/**
 * Base Activity that implements common functionality for the rest of the activities
 */
abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity() {
    protected lateinit var factory: ViewModelFactory
    protected lateinit var viewModel: VM

    protected abstract fun createViewModel(): VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        factory = ViewModelFactory.getInstance(this.application)
        viewModel = createViewModel()
    }

    /**
     * Sets up the action bar
     * @param titleId
     * @param enableHomeAsUp
     */
    protected fun setUpActionBar(titleId: Int, enableHomeAsUp: Boolean) {
        setUpActionBar(getString(titleId), enableHomeAsUp)
    }

    /**
     * Sets up the action bar
     * @param title
     * @param enableHomeAsUp
     */
    protected fun setUpActionBar(title: String, enableHomeAsUp: Boolean) {
        setToolbar()

        supportActionBar?.setDisplayHomeAsUpEnabled(enableHomeAsUp)
        supportActionBar?.title = title
    }

    /**
     * Sets up the toolbar
     */
    private fun setToolbar() {
        val mToolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(mToolbar)
    }
}