package com.wineadvocate.bradixapp

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import com.wineadvocate.domain.GetPhotoUseCase
import com.wineadvocate.domain.PhotoViewState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 *  Created by Christian on Wednesday Mar, 2019
 */

class PhotosPresenter : MviBasePresenter<PhotosView, PhotoViewState>() {

    override fun bindIntents() {
        val photoState : Observable<PhotoViewState> = intent(PhotosView::showPhotos)
            .subscribeOn(Schedulers.io())
            .debounce(400, TimeUnit.MILLISECONDS)
            .flatMap { GetPhotoUseCase.getPhotos() }
            .doOnNext { Timber.d("Received new state: %s", it)}
            .observeOn(AndroidSchedulers.mainThread())

        subscribeViewState(photoState, PhotosView::render)
    }

}