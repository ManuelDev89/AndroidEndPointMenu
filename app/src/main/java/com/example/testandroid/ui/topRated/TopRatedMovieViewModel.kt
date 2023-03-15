package com.example.testandroid.ui.topRated

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.testandroid.data.entities.MovieEntity
import com.example.testandroid.data.repository.MovieRepository
import com.example.testandroid.utils.Pages
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.testandroid.utils.Resource
import javax.inject.Inject

@HiltViewModel
class TopRatedMovieViewModel @Inject constructor (private val repository: MovieRepository) : ViewModel() {

    val fetchTopRatedMovies: LiveData<Resource<List<MovieEntity>>> = repository.getTopRatedMovies(
        Pages.topRatedPage)

}