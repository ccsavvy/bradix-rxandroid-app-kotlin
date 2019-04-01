package com.wineadvocate.domain

import com.wineadvocate.model.DataClassPhoto

/**
 *  Created by Christian on Wednesday Mar, 2019
 */

sealed class AlbumViewState {

    object LoadingState : AlbumViewState()
    data class DataState(val albums: ArrayList<DataClassPhoto>) : AlbumViewState()
    data class ErrorState(val error: Throwable) : AlbumViewState()
}