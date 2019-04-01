package com.wineadvocate.bradixapp

import com.hannesdorfmann.mosby3.mvp.MvpView
import com.wineadvocate.domain.AlbumViewState
import io.reactivex.Observable

/**
 *  Created by Christian on Wednesday Mar, 2019
 */

interface AlbumsView : MvpView {

    fun loadAlbums() : Observable<String>

    /*
     * Renders the state in the UI
     */

    fun render(state: AlbumViewState)

}