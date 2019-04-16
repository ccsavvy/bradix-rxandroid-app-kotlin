package com.wineadvocate.presentation

import com.wineadvocate.model.DataClassPhoto

/**
 *  Created by Christian on Monday Apr, 2019
 */

sealed class PhotoActionState {

    object LoadingState : PhotoActionState()
    data class DataState(val photos: ArrayList<DataClassPhoto>) : PhotoActionState()
    data class ErrorState(val error: Throwable) : PhotoActionState()
}