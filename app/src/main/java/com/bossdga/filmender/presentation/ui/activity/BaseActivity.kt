package com.bossdga.filmender.presentation.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bossdga.filmender.R
import com.bossdga.filmender.presentation.viewmodel.BaseViewModel
import com.bossdga.filmender.presentation.viewmodel.ViewModelFactory

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

    private inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
        beginTransaction().func().commit()
    }

    protected fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int){
        supportFragmentManager.inTransaction { add(frameId, fragment) }
    }

    protected fun AppCompatActivity.attachFragment(fragment: Fragment){
        supportFragmentManager.inTransaction { attach(fragment) }
    }

    protected fun AppCompatActivity.removeFragment(fragment: Fragment) {
        supportFragmentManager.inTransaction{ remove(fragment) }
    }

    protected fun AppCompatActivity.detachFragment(fragment: Fragment) {
        supportFragmentManager.inTransaction{ detach(fragment) }
    }

    protected fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
        supportFragmentManager.inTransaction{ replace(frameId, fragment) }
    }
}