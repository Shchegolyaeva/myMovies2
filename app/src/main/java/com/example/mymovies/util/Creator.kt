package com.example.mymovies.util

import android.app.Activity
import android.content.Context
import com.example.mymovies.data.MoviesRepositoryImpl
import com.example.mymovies.data.network.RetrofitNetworkClient
import com.example.mymovies.domain.api.MoviesInteractor
import com.example.mymovies.domain.api.MoviesRepository
import com.example.mymovies.domain.impl.MoviesInteractorImpl
import com.example.mymovies.presentation.PosterPresenter
import com.example.mymovies.presentation.movies.MoviesSearchPresenter
import com.example.mymovies.presentation.movies.MoviesView
import com.example.mymovies.presentation.poster.PosterView
import com.example.mymovies.ui.movies.MoviesAdapter

object Creator {
    private fun getMoviesRepository(context: Context): MoviesRepository {
        return MoviesRepositoryImpl(RetrofitNetworkClient(context))
    }

    fun provideMoviesInteractor(context: Context): MoviesInteractor {
        return MoviesInteractorImpl(getMoviesRepository(context))
    }


    fun provideMoviesSearchPresenter(context: Context): MoviesSearchPresenter {
        return MoviesSearchPresenter(context)
    }

    fun providePosterPresenter(posterView: PosterView,
                               imageUrl: String): PosterPresenter {
        return PosterPresenter(posterView, imageUrl)
    }
}