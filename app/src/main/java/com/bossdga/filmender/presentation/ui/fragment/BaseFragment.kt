package com.bossdga.filmender.presentation.ui.fragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup.OnHierarchyChangeListener
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bossdga.filmender.ProgressDialogHandler
import com.bossdga.filmender.R
import com.bossdga.filmender.model.content.AdType
import com.google.android.gms.ads.formats.MediaView
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import io.reactivex.disposables.CompositeDisposable


open class BaseFragment : Fragment(), ProgressDialogHandler {
    protected var currentNativeAd: UnifiedNativeAd? = null

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

    /**
     * Populates a [UnifiedNativeAdView] object with data from a given
     * [UnifiedNativeAd].
     *
     * @param nativeAd the object containing the ad's assets
     * @param adView the view to be populated
     */
    protected fun populateUnifiedNativeAdView(nativeAd: UnifiedNativeAd, adView: UnifiedNativeAdView, adType: AdType) {
        // You must call destroy on old ads when you are done with them,
        // otherwise you will have a memory leak.
        currentNativeAd?.destroy()
        currentNativeAd = nativeAd

        // Set other ad assets.
        adView.headlineView = adView.findViewById(R.id.ad_headline)

        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_app_icon)
        adView.advertiserView = adView.findViewById(R.id.ad_advertiser)

        if (adType == AdType.MEDIUM) {
            // Set the media view.
            adView.mediaView = adView.findViewById<MediaView>(R.id.ad_media)
            adView.bodyView = adView.findViewById(R.id.ad_body)
            adView.priceView = adView.findViewById(R.id.ad_price)
            adView.storeView = adView.findViewById(R.id.ad_store)
            adView.starRatingView = adView.findViewById(R.id.ad_stars)

            adView.mediaView.setMediaContent(nativeAd.mediaContent)

            if (nativeAd.body == null) {
                adView.bodyView.visibility = View.INVISIBLE
            } else {
                adView.bodyView.visibility = View.VISIBLE
                (adView.bodyView as TextView).text = nativeAd.body
            }

            if (nativeAd.price == null) {
                adView.priceView.visibility = View.INVISIBLE
            } else {
                adView.priceView.visibility = View.VISIBLE
                (adView.priceView as TextView).text = nativeAd.price
            }

            if (nativeAd.store == null) {
                adView.storeView.visibility = View.INVISIBLE
            } else {
                adView.storeView.visibility = View.VISIBLE
                (adView.storeView as TextView).text = nativeAd.store
            }

            if (nativeAd.starRating == null) {
                adView.starRatingView.visibility = View.INVISIBLE
            } else {
                (adView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
                adView.starRatingView.visibility = View.VISIBLE
            }
        }

        // The headline and media content are guaranteed to be in every UnifiedNativeAd.
        (adView.headlineView as TextView).text = nativeAd.headline

        if (nativeAd.callToAction == null) {
            adView.callToActionView.visibility = View.INVISIBLE
        } else {
            adView.callToActionView.visibility = View.VISIBLE
            (adView.callToActionView as Button).text = nativeAd.callToAction
        }

        if (nativeAd.icon == null) {
            adView.iconView.visibility = View.GONE
        } else {
            (adView.iconView as ImageView).setImageDrawable(
                nativeAd.icon.drawable)
            adView.iconView.visibility = View.VISIBLE
        }

        if (nativeAd.advertiser == null) {
            adView.advertiserView.visibility = View.INVISIBLE
        } else {
            (adView.advertiserView as TextView).text = nativeAd.advertiser
            adView.advertiserView.visibility = View.VISIBLE
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd)
    }
}