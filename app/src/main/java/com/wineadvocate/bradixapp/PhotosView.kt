package com.wineadvocate.bradixapp

import com.hannesdorfmann.mosby3.mvp.MvpView
import com.wineadvocate.domain.PhotoViewState
import com.wineadvocate.model.DataClassPhoto
import io.reactivex.Observable

/**
 *  Created by Christian on Wednesday Mar, 2019
 */

interface PhotosView : MvpView {

    /*
     *  Emits item clicks
     *
     */

    fun showAlbumByIdIntent() : Observable<DataClassPhoto>

    /*
     * Emits item from json response
     */

    fun showPhotos() : Observable<Boolean>

    /*
     * Renders the state in the UI
     *
     */

    fun render(state: PhotoViewState)
}