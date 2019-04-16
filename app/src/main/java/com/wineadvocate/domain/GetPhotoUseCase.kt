package com.wineadvocate.domain

import com.wineadvocate.data.AlbumRepository
import com.wineadvocate.data.PhotoRepository
import com.wineadvocate.presentation.PhotoActionState
import io.reactivex.Observable

/**
 *  Created by Christian on Wednesday Mar, 2019
 */


object GetPhotoUseCase {

    fun getPhotos() : Observable<PhotoActionState>? {

        return PhotoRepository.loadPhoto()
            ?.map<PhotoActionState> { PhotoActionState.DataState(it) }
            ?.startWith(PhotoActionState.LoadingState)
            ?.onErrorReturn { PhotoActionState.ErrorState(it) }
    }

    fun getAlbums(albumId: String) : Observable<PhotoActionState>? {

        return AlbumRepository.loadAlbum(albumId)
            ?.map<PhotoActionState> { PhotoActionState.DataState(it) }
            ?.startWith(PhotoActionState.LoadingState)
            ?.onErrorReturn { PhotoActionState.ErrorState(it) }
    }
}