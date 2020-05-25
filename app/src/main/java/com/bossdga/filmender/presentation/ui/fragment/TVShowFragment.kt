package com.bossdga.filmender.presentation.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bossdga.filmender.OnItemClickListener
import com.bossdga.filmender.OnLoadingListener
import com.bossdga.filmender.R
import com.bossdga.filmender.model.content.BaseContent
import com.bossdga.filmender.model.content.TVShow
import com.bossdga.filmender.model.content.TVShowResponse
import com.bossdga.filmender.presentation.adapter.TVShowAdapter
import com.bossdga.filmender.presentation.ui.activity.TVShowDetailActivity
import com.bossdga.filmender.presentation.viewmodel.MainViewModel
import com.bossdga.filmender.util.PreferenceUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

/**
 * Fragment that will show a Health Prompt
 */
class TVShowFragment : BaseFragment() {
    private lateinit var adapter: TVShowAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var mainViewModel: MainViewModel
    private lateinit var onLoadingListener: OnLoadingListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_tv_show, container, false)

        mRecyclerView = rootView.findViewById(R.id.recyclerView)
        gridLayoutManager = GridLayoutManager(activity, 3)
        mRecyclerView.setLayoutManager(gridLayoutManager)
        adapter = TVShowAdapter(activity as Context, object : OnItemClickListener {
            override fun onItemClick(content: BaseContent) {
                val intent = Intent(activity, TVShowDetailActivity::class.java)
                intent.putExtra("id", content.id)
                activity?.startActivity(intent)
            }
        })
        mRecyclerView.setAdapter(adapter)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        subscribeTVShows(mainViewModel.loadTVShows(PreferenceUtils.getYearFrom(activity as Context),
            PreferenceUtils.getYearTo(activity as Context),
            PreferenceUtils.getRating(activity as Context),
            PreferenceUtils.getGenres(activity as Context)))

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
     * @param tvShowsObservable
     */
    private fun subscribeTVShows(tvShowsObservable: Observable<TVShowResponse>) {
        disposable.add(tvShowsObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<TVShowResponse>() {
                    override fun onComplete() {}

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                    override fun onNext(tvShowResponse: TVShowResponse) {
                        val showList: List<TVShow> = tvShowResponse.results.take(PreferenceUtils.getResults(activity as Context)!!)
                        adapter.setItems(showList)
                        onLoadingListener.onFinishedLoading()
                    }
                }))
    }

    fun refreshContent() {
        subscribeTVShows(mainViewModel.loadTVShows(PreferenceUtils.getYearFrom(activity as Context),
            PreferenceUtils.getYearTo(activity as Context),
            PreferenceUtils.getRating(activity as Context),
            PreferenceUtils.getGenres(activity as Context)))
    }
}