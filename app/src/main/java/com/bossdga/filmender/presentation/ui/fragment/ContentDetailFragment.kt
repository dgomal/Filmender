package com.bossdga.filmender.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.bossdga.filmender.R
import com.bossdga.filmender.model.Movie
import com.bossdga.filmender.presentation.viewmodel.DetailViewModel
import com.bossdga.filmender.util.ImageUtils.setImage
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers


/**
 * A simple Fragment that will show Event details
 */
class ContentDetailFragment : BaseFragment() {
    private lateinit var detailViewModel: DetailViewModel
    private var contentId: Int? = 0

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
        val rootView = inflater.inflate(R.layout.fragment_content_detail, container, false)

        image = rootView.findViewById(R.id.image)
        name = rootView.findViewById(R.id.name)
        voteAverage = rootView.findViewById(R.id.voteAverage)
        date = rootView.findViewById(R.id.date)
        overview = rootView.findViewById(R.id.overview)
        genre = rootView.findViewById(R.id.genre)

        detailViewModel = ViewModelProvider(requireActivity()).get(DetailViewModel::class.java)
        contentId = activity?.intent?.getIntExtra("id", 0)
        subscribeContent(detailViewModel.loadContent(contentId))

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
    private fun subscribeContent(movieObservable: Observable<Movie>) {
        disposable.add(movieObservable
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<Movie>() {
                override fun onComplete() {}

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

                override fun onNext(movie: Movie) {
                    var genres = ""
                    setImage(image, movie.posterPath)
                    name.setText(movie.title)
                    voteAverage.setText(movie.voteAverage)
                    date.setText(movie.releaseDate)
                    overview.setText(movie.overview)
                    movie.genres.forEach { e -> genres = genres.plus(e.name).plus(" | ")}
                    genre.text = genres
                    hideProgressDialog()
                }
            }))
    }
}