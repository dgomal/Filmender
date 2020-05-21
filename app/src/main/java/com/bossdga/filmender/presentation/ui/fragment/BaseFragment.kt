package com.bossdga.filmender.presentation.ui.fragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import com.bossdga.filmender.ProgressDialogHandler
import com.bossdga.filmender.R
import io.reactivex.disposables.CompositeDisposable

/**
 * Base Fragment that implements common functionality for all the fragments
 */
open class BaseFragment : Fragment(), ProgressDialogHandler {
    protected var disposable = CompositeDisposable()
    private lateinit var mProgressDialog: ProgressDialog
    private var showDialog = false
    protected var extras: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        extras = activity?.intent
    }

    override fun onPause() {
        super.onPause()

        showDialog = false
    }

    /**
     * Creates and shows a ProgressDialog
     */
    override fun showProgressDialog() {
        showDialog = true

        Handler().postDelayed({
            if (showDialog) {
                mProgressDialog = ProgressDialog(activity)
                mProgressDialog.setTitle(R.string.please_wait)
                mProgressDialog.setMessage(resources.getString(R.string.loading))
                mProgressDialog.show()
            }
        }, 1)
    }

    /**
     * Hides the ProgressDialog
     */
    override fun hideProgressDialog() {
        showDialog = false
        mProgressDialog.dismiss()
    }
}