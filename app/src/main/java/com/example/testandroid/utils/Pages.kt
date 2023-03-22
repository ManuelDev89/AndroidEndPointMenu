package com.example.testandroid.utils

object Pages {
    var popularPage: Int = 0
    var topRatedPage: Int = 0
    var upcomingPage: Int = 0
    var pageMaxSize = 20

    fun scrollPopular() {
        Pages.popularPage +=1
    }
    fun scrollTopRated() {
        Pages.topRatedPage +=1
    }
    fun scrollUpcoming() {
        Pages.upcomingPage +=1
    }
    fun resetPages() {
        popularPage = 1
        topRatedPage = 1
        upcomingPage = 1
    }
}