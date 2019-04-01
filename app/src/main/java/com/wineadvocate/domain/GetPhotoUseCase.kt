package com.wineadvocate.domain

import com.wineadvocate.data.PhotoRepository
import io.reactivex.Observable

/**
 *  Created by Christian on Wednesday Mar, 2019
 */


object GetPhotoUseCase {

    fun getPhotos() : Observable<PhotoViewState>? {

        return PhotoRepository.loadPhoto()
            ?.map<PhotoViewState> { PhotoViewState.DataState(it) }
            ?.startWith(PhotoViewState.LoadingState)
            ?.onErrorReturn { PhotoViewState.ErrorState(it) }
    }
}