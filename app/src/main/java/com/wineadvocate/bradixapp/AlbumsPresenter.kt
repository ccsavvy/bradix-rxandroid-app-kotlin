package com.wineadvocate.bradixapp

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import com.wineadvocate.domain.AlbumViewState
import com.wineadvocate.domain.GetAlbumUseCase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 *  Created by Christian on Wednesday Mar, 2019
 */

class AlbumsPresenter: MviBasePresenter<AlbumsView, AlbumViewState>() {

    override fun bindIntents() {

        val albumState : Observable<AlbumViewState>? = intent(AlbumsView::loadAlbums)
            .subscribeOn(Schedulers.io())
            .debounce(400, TimeUnit.MILLISECONDS)
            .flatMap { GetAlbumUseCase.getAlbums(albumId = it) }
            .doOnNext { Timber.d("Received new state: %s", it)}
            .observeOn(AndroidSchedulers.mainThread())

        subscribeViewState(albumState!!, AlbumsView::render)
    }
}