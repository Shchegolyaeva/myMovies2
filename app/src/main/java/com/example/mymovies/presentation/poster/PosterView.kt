package com.example.mymovies.presentation.poster

import moxy.MvpView

interface PosterView: MvpView {

    fun setupPosterImage(url: String)
}