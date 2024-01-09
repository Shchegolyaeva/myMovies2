package com.example.mymovies.domain.api

import com.example.mymovies.domain.models.Movie
import com.example.mymovies.util.Resource

interface MoviesRepository {
    fun searchMovies(expression: String): Resource<List<Movie>>
}