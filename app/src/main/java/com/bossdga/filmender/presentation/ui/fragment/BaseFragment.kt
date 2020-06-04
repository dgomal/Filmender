package com.bossdga.filmender.presentation.ui.fragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bossdga.filmender.ProgressDialogHandler
import com.bossdga.filmender.R
import io.reactivex.disposables.CompositeDisposable

open class BaseFragment : Fragment(), ProgressDialogHandler {
    protected lateinit var mProgressDialog: ProgressDialog
    protected var showDialog = false
    protected var disposable = CompositeDisposable()
    protected var extras: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        extras = requireActivity().intent
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

    private inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
        beginTransaction().func().commit()
    }

    protected fun addFragment(fragment: Fragment, frameId: Int){
        childFragmentManager.inTransaction { add(frameId, fragment) }
    }

    protected fun attachFragment(fragment: Fragment){
        childFragmentManager.inTransaction { attach(fragment) }
    }

    protected fun removeFragment(fragment: Fragment) {
        childFragmentManager.inTransaction{ remove(fragment) }
    }

    protected fun detachFragment(fragment: Fragment) {
        childFragmentManager.inTransaction{ detach(fragment) }
    }

    protected fun replaceFragment(fragment: Fragment, frameId: Int) {
        childFragmentManager.inTransaction{ replace(frameId, fragment) }
    }
}