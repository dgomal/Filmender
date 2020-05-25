package com.bossdga.filmender.presentation.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.bossdga.filmender.R
import com.bossdga.filmender.model.content.ImageType
import com.bossdga.filmender.model.content.TVShow
import com.bossdga.filmender.presentation.viewmodel.TVShowDetailViewModel
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
    private lateinit var name: TextView
    private lateinit var voteAverage: TextView
    private lateinit var date: TextView
    private lateinit var overview: TextView
    private lateinit var genre: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showProgressDialog()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_tv_show_detail, container, false)

        image = requireActivity().findViewById(R.id.image)
        name = rootView.findViewById(R.id.name)
        voteAverage = rootView.findViewById(R.id.voteAverage)
        date = rootView.findViewById(R.id.date)
        overview = rootView.findViewById(R.id.overview)
        genre = rootView.findViewById(R.id.genre)

        tvShowDetailViewModel = ViewModelProvider(requireActivity()).get(TVShowDetailViewModel::class.java)
        id = extras?.getIntExtra("id", 0)
        subscribeTVShow(tvShowDetailViewModel.loadTVShow(id,
            "videos,images"))

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
                    name.setText(tvShow.title)
                    voteAverage.setText(tvShow.voteAverage)
                    date.setText(tvShow.releaseDate)
                    overview.setText(tvShow.overview)
                    genre.text = tvShow.genres.joinToString(separator = " | ") { it.name }
                    hideProgressDialog()
                }
            }))
    }
}