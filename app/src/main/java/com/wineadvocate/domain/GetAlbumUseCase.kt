package com.wineadvocate.domain

import com.wineadvocate.data.AlbumRepository
import io.reactivex.Observable

/**
 *  Created by Christian on Wednesday Mar, 2019
 */

object GetAlbumUseCase {

    fun getAlbums(albumId: String) : Observable<AlbumViewState>? {

        return AlbumRepository.loadAlbum(albumId)
            ?.map<AlbumViewState> { AlbumViewState.DataState(it) }
            ?.startWith(AlbumViewState.LoadingState)
            ?.onErrorReturn { AlbumViewState.ErrorState(it) }
    }
}