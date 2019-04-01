package com.wineadvocate.domain

import com.wineadvocate.model.DataClassPhoto

/**
 *  Created by Christian on Tuesday Mar, 2019
 */

sealed class PhotoViewState {

    object LoadingState : PhotoViewState()
    data class DataState(val photos: ArrayList<DataClassPhoto>) : PhotoViewState()
    data class ErrorState(val error: Throwable) : PhotoViewState()
}