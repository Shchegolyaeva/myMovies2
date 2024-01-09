package com.example.mymovies.ui.movies

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.example.mymovies.ui.poster.PosterActivity
import com.example.mymovies.R
import com.example.mymovies.domain.models.Movie
import com.example.mymovies.presentation.movies.MoviesSearchPresenter
import com.example.mymovies.presentation.movies.MoviesView
import com.example.mymovies.ui.movies.models.MoviesState
import moxy.MvpActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

class MoviesActivity : MvpActivity(), MoviesView {

    @InjectPresenter
    lateinit var moviesSearchPresenter: MoviesSearchPresenter

    @ProvidePresenter
    fun providePresenter(): MoviesSearchPresenter {
        return Creator.provideMoviesSearchPresenter(
            context = this.applicationContext,
        )
    }


    private val imdbBaseUrl = "https://imdb-api.com"
    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
//        private var moviesSearchPresenter: MoviesSearchPresenter? = null
    }

    private lateinit var queryInput: EditText
    private lateinit var placeholderMessage: TextView
    private lateinit var moviesList: RecyclerView
    private lateinit var progressBar: ProgressBar

    private var textWatcher: TextWatcher? = null


//    private val retrofit = Retrofit.Builder()
//        .baseUrl(imdbBaseUrl)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//
//    private val imdbService = retrofit.create(IMDbApiService::class.java)


    private val adapter = MoviesAdapter {
        if (clickDebounce()) {
            val intent = Intent(this, PosterActivity::class.java)
            intent.putExtra("poster", it.image)
            startActivity(intent)
        }
    }

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

//    override fun onStart() {
//        super.onStart()
//        moviesSearchPresenter?.attachView(this)
//    }

//    override fun onResume() {
//        super.onResume()
//        moviesSearchPresenter?.attachView(this)
//    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)


        placeholderMessage = findViewById(R.id.placeholderMessage)
        queryInput = findViewById(R.id.queryInput)
        moviesList = findViewById(R.id.locations)
        progressBar = findViewById(R.id.progressBar)


        moviesList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        moviesList.adapter = adapter

         textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                moviesSearchPresenter.searchDebounce(
                    changedText = s?.toString() ?: ""
                )
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        textWatcher?.let { queryInput.addTextChangedListener(it) }


    }

//    override fun onPause() {
//        super.onPause()
//        moviesSearchPresenter?.detachView()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        moviesSearchPresenter?.detachView()
//    }
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        moviesSearchPresenter?.detachView()
//    }

//    override fun onDestroy() {
//        super.onDestroy()
//        textWatcher?.let { queryInput.removeTextChangedListener(it) }
////        moviesSearchPresenter?.detachView()
//        moviesSearchPresenter?.onDestroy()
//    }





    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

     fun showLoading() {
        moviesList.visibility = View.GONE
        placeholderMessage.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

     fun showError(errorMessage: String) {
        moviesList.visibility = View.GONE
        placeholderMessage.visibility = View.VISIBLE
        progressBar.visibility = View.GONE

        placeholderMessage.text = errorMessage
    }

     fun showEmpty(emptyMessage: String) {
        showError(emptyMessage)
    }

     fun showContent(movies: List<Movie>) {
        moviesList.visibility = View.VISIBLE
        placeholderMessage.visibility = View.GONE
        progressBar.visibility = View.GONE

        adapter.movies.clear()
        adapter.movies.addAll(movies)
        adapter.notifyDataSetChanged()
    }

    override fun render(state: MoviesState) {
        when (state) {
            is MoviesState.Loading -> showLoading()
            is MoviesState.Content -> showContent(state.movies)
            is MoviesState.Error -> showError(state.errorMessage)
            is MoviesState.Empty -> showEmpty(state.message)
        }
    }

}
