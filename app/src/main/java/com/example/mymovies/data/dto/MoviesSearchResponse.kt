package com.example.mymovies.data.dto

import com.example.mymovies.domain.models.Movie

data class MoviesSearchResponse(val searchType: String,
                                val expression: String,
                                val results: List<Movie>)
