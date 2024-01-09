package com.example.mymovies.presentation.movies

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovies.util.Creator
import com.example.mymovies.R
import com.example.mymovies.domain.api.MoviesInteractor
import com.example.mymovies.domain.models.Movie
import com.example.mymovies.ui.movies.MoviesAdapter
import com.example.mymovies.ui.movies.models.MoviesState
import moxy.MvpPresenter

class MoviesSearchPresenter(
                            private val context: Context,
) : MvpPresenter<MoviesView>(){
//    private var view: MoviesView? = null
//    private var state: MoviesState? = null
    private var latestSearchText: String? = null
    private val moviesInteractor = Creator.provideMoviesInteractor(context)


    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }


    private val movies = ArrayList<Movie>()
    private val handler = Handler(Looper.getMainLooper())
    private var lastSearchText: String? = null

    private val searchRunnable = Runnable {
        val newSearchText = lastSearchText ?: ""
        searchRequest(newSearchText)
    }


    override  fun onDestroy() {
        handler.removeCallbacks(searchRunnable)
//        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }
        this.latestSearchText = changedText
            this.lastSearchText = changedText
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun searchRequest(newSearchText: String) {
//        if (queryInput.text.isNotEmpty()) {
//
//            placeholderMessage.visibility = View.GONE
//            moviesList.visibility = View.GONE
//            progressBar.visibility = View.VISIBLE
        if (newSearchText.isNotEmpty()) {

            renderState(MoviesState.Loading)

            moviesInteractor.searchMovies(newSearchText, object : MoviesInteractor.MoviesConsumer {
                override fun consume(foundMovies: List<Movie>?, errorMessage: String?) {
                    handler.post {
                        if (foundMovies != null) {
                            movies.clear()
                            movies.addAll(foundMovies)
                        }

                        when {
                            errorMessage != null -> {
                                renderState(
                                    MoviesState.Error(
                                        errorMessage = context.getString(R.string.something_went_wrong),
                                    )
                                )
                                viewState.showToast(errorMessage)
                            }

                            movies.isEmpty() -> {
                                renderState(
                                    MoviesState.Empty(
                                        message = context.getString(R.string.nothing_found),
                                    )
                                )
                            }

                            else -> {
                                renderState(
                                    MoviesState.Content(
                                        movies = movies,
                                    )
                                )
                            }
                        }

                    }
                }
            })
        }
    }

    private fun renderState(state: MoviesState) {
//        this.state = state
//        this.view?.render(state)
        viewState.render(state)

    }
}