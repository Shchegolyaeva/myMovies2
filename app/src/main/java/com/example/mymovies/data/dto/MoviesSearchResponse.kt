package com.example.mymovies.data.dto


data class MoviesSearchResponse(val searchType: String,
                                val expression: String,
                                val results: List<MovieDto>) : Response()
