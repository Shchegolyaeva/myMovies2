package com.example.mymovies.presentation.movies

import android.content.Context
import com.example.mymovies.domain.models.Movie
import com.example.mymovies.ui.movies.models.MoviesState
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface MoviesView: MvpView {
    // Методы, меняющие внешний вид экрана
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun render(state: MoviesState)

    // Методы «одноразовых событий»
 //   fun showMessage(message: String)
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showToast(message: String)

//    fun showPlaceholderMessage(isVisible: Boolean)
//
//    fun showMoviesList(isVisible: Boolean)
//
//    fun showProgressBar(isVisible: Boolean)
//
//    fun changePlaceholderText(newPlaceholderText: String)
//
//    fun updateMoviesList(newMoviesList: List<Movie>)
}