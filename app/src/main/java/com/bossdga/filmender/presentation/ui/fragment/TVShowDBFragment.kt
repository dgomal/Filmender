package com.bossdga.filmender.presentation.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bossdga.filmender.OnItemClickListener
import com.bossdga.filmender.R
import com.bossdga.filmender.model.content.BaseContent
import com.bossdga.filmender.model.content.TVShow
import com.bossdga.filmender.presentation.adapter.TVShowDBAdapter
import com.bossdga.filmender.presentation.ui.activity.TVShowDetailActivity
import com.bossdga.filmender.presentation.viewmodel.MainViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

/**
 * Fragment that will show a list of tv shows
 */
class TVShowDBFragment : BaseFragment() {
    private lateinit var adapter: TVShowDBAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var mainViewModel: MainViewModel
    private lateinit var showsHeader: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_db_tv_show, container, false)

        showsHeader = rootView.findViewById(R.id.ShowsHeader)
        mRecyclerView = rootView.findViewById(R.id.recyclerView)
        linearLayoutManager = LinearLayoutManager(activity)
        mRecyclerView.setLayoutManager(linearLayoutManager)
        adapter = TVShowDBAdapter(activity as Context, object : OnItemClickListener {
            override fun onItemClick(content: BaseContent) {
                val intent = Intent(activity, TVShowDetailActivity::class.java)
                intent.putExtra("id", content.id)
                requireActivity().startActivity(intent)
            }
        })
        mRecyclerView.setAdapter(adapter)

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
     * @param tvShowsObservable
     */
    private fun subscribeTVShowsFromDB(tvShowsObservable: Observable<List<TVShow>>) {
        disposable.add(tvShowsObservable
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<List<TVShow>>() {
                override fun onComplete() {}

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

                override fun onNext(shows: List<TVShow>) {
                    adapter.setItems(shows)
                    if(shows.isNotEmpty()) {
                        showsHeader.visibility = View.VISIBLE
                    }
                }
            }))
    }

    fun refreshContent() {
        subscribeTVShowsFromDB(mainViewModel.loadTVShowsFromDB())
    }
}