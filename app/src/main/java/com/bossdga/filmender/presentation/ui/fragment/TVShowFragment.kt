package com.bossdga.filmender.presentation.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bossdga.filmender.OnItemClickListener
import com.bossdga.filmender.R
import com.bossdga.filmender.model.content.*
import com.bossdga.filmender.presentation.adapter.TVShowAdapter
import com.bossdga.filmender.presentation.ui.activity.TVShowDetailActivity
import com.bossdga.filmender.presentation.viewmodel.MainViewModel
import com.bossdga.filmender.util.PreferenceUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

/**
 * Fragment that will show a list of tv shows
 */
class TVShowFragment : BaseFragment() {
    private var fromDB: Boolean = false
    private lateinit var adapter: TVShowAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mainViewModel: MainViewModel
    private lateinit var showsHeader: TextView
    private var isEmpty: Boolean = false

    companion object {
        private const val ARG_FROM_DB = "fromDB"

        fun newInstance(fromDB: Boolean) = TVShowFragment().apply {
            arguments = Bundle().apply {
                putBoolean(ARG_FROM_DB, fromDB)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getBoolean(ARG_FROM_DB)?.let {
            fromDB = it as Boolean
        }

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_tv_show, container, false)

        showsHeader = rootView.findViewById(R.id.ShowsHeader)
        mRecyclerView = rootView.findViewById(R.id.recyclerView)

        mRecyclerView.layoutManager = when (fromDB) {
            true -> LinearLayoutManager(activity)
            false -> GridLayoutManager(activity, 3)
        }

        adapter = TVShowAdapter(activity as Context, fromDB, object : OnItemClickListener {
            override fun onItemClick(content: BaseContent) {
                val intent = Intent(activity, TVShowDetailActivity::class.java)
                intent.putExtra("id", content.id)
                requireActivity().startActivity(intent)
            }
        })

        mRecyclerView.setAdapter(adapter)

        loadContent()

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
    private fun subscribeTVShows(tvShowsObservable: Observable<TVShowResponse>) {
        disposable.add(tvShowsObservable
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<TVShowResponse>() {
                override fun onComplete() {

                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

                override fun onNext(tvShowResponse: TVShowResponse) {
                    val showList: List<TVShow> = tvShowResponse.results.shuffled().take(PreferenceUtils.getResults()!!)
                    adapter.setItems(showList)
                    if(showList.isEmpty()) {
                        showsHeader.visibility = View.GONE
                        isEmpty = true
                    } else {
                        showsHeader.visibility = View.VISIBLE
                        isEmpty = false
                    }
                    mainViewModel.loaded.postValue("true")
                }
            }))
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
                    if(shows.isEmpty()) {
                        showsHeader.visibility = View.GONE
                        isEmpty = true
                    } else {
                        showsHeader.visibility = View.VISIBLE
                        isEmpty = false
                    }
                    mainViewModel.loadedDB.postValue("true")
                }
            }))
    }

    private fun loadContent() {
        when (fromDB) {
            true -> subscribeTVShowsFromDB(mainViewModel.loadTVShowsFromDB())
            false -> subscribeTVShows(mainViewModel.loadTVShows())
        }
    }

    fun isEmpty(): Boolean {
        return isEmpty
    }
}