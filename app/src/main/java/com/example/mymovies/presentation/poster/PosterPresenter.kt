package com.example.mymovies.presentation

import android.app.Activity
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.mymovies.R
import com.example.mymovies.presentation.poster.PosterView

class PosterPresenter (private val view: PosterView,
                       private val imageUrl: String,  ) {


    fun onCreate() {
        view.setupPosterImage(imageUrl)
    }
}