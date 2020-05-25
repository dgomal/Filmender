package com.bossdga.filmender.presentation.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.bossdga.filmender.OnLoadingListener
import com.bossdga.filmender.R
import com.bossdga.filmender.model.content.ImageType
import com.bossdga.filmender.model.content.TVShow
import com.bossdga.filmender.presentation.viewmodel.TVShowDetailViewModel
import com.bossdga.filmender.util.DateUtils
import com.bossdga.filmender.util.ImageUtils.setImage
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers


/**
 * A simple Fragment that will show Event details
 */
class TVShowDetailFragment : BaseFragment() {
    private lateinit var tvShowDetailViewModel: TVShowDetailViewModel
    private var id: Int? = 0

    private lateinit var image: ImageView
    private lateinit var voteAverage: TextView
    private lateinit var date: TextView
    private lateinit var overview: TextView
    private lateinit var genre: TextView
    private lateinit var cast: TextView
    private lateinit var numberOfSeasons: TextView
    private lateinit var onLoadingListener: OnLoadingListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showProgressDialog()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_tv_show_detail, container, false)

        image = requireActivity().findViewById(R.id.image)
        voteAverage = rootView.findViewById(R.id.voteAverage)
        date = rootView.findViewById(R.id.date)
        overview = rootView.findViewById(R.id.overview)
        genre = rootView.findViewById(R.id.genre)
        cast = rootView.findViewById(R.id.cast)
        numberOfSeasons = rootView.findViewById(R.id.numberOfSeasons)

        tvShowDetailViewModel = ViewModelProvider(requireActivity()).get(TVShowDetailViewModel::class.java)
        id = extras?.getIntExtra("id", 0)
        subscribeTVShow(tvShowDetailViewModel.loadTVShow(id,
            "videos,images,credits"))

        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()

        disposable.clear()
    }

    override fun onDestroy() {
        super.onDestroy()

        disposable.dispose()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        this.onLoadingListener = (context as OnLoadingListener)
    }

    /**
     * Method that adds a Disposable to the CompositeDisposable
     * @param moviesObservable
     */
    private fun subscribeTVShow(tvShowObservable: Observable<TVShow>) {
        disposable.add(tvShowObservable
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<TVShow>() {
                override fun onComplete() {}

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

                override fun onNext(tvShow: TVShow) {
                    setImage(activity as Context, image, tvShow.backdropPath, ImageType.BACK_DROP)
                    onLoadingListener.onFinishedLoading(tvShow.title)
                    voteAverage.text = tvShow.voteAverage
                    numberOfSeasons.text = tvShow.numberOfSeasons.toString().plus(" Seasons")
                    date.text = tvShow.releaseDate.substringBefore("-")
                    overview.text = tvShow.overview
                    genre.text = tvShow.genres.joinToString(separator = " | ") { it.name }
                    cast.text = tvShow.credits.cast.joinToString(separator = ", ") { it.name }
                    hideProgressDialog()
                }
            }))
    }
}