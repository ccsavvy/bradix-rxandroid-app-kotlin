package com.wineadvocate.bradixapp

import android.annotation.SuppressLint
import com.google.gson.Gson
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import com.wineadvocate.domain.GetPhotoUseCase
import com.wineadvocate.domain.PhotoViewState
import com.wineadvocate.model.DataClassPhoto
import com.wineadvocate.presentation.PhotoActionState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 *  Created by Christian on Wednesday Mar, 2019
 */

class PhotosPresenter : MviBasePresenter<PhotosView, PhotoViewState>() {

    @SuppressLint("BinaryOperationInTimber")
    override fun bindIntents() {
        val photoState : Observable<PhotoActionState> = intent(PhotosView::showPhotos)
            .subscribeOn(Schedulers.io())
            .debounce(400, TimeUnit.MILLISECONDS)
            .doOnNext { Timber.d("Photos received new state: %s", it)}
            .flatMap { GetPhotoUseCase.getPhotos() }
            .observeOn(AndroidSchedulers.mainThread())


        val albumState : Observable<PhotoActionState>? = intent(PhotosView::showAlbumByIdIntent)
            .subscribeOn(Schedulers.io())
            .debounce(400, TimeUnit.MILLISECONDS)
            .doOnNext { Timber.d("Albums received new state: %s", it)}
            .flatMap { GetPhotoUseCase.getAlbums(albumId = it.albumId) }
            .observeOn(AndroidSchedulers.mainThread())


        val allIntents = Observable.merge(photoState, albumState)
        val initializeState = PhotoViewState(isLoading = true)
        val stateObservable = allIntents
            .scan(initializeState, this::viewStateReducer)
            .doOnNext{ Timber.d("State" + Gson().toJson(it))}


        subscribeViewState(stateObservable, PhotosView::render)
    }

    private fun viewStateReducer(previousState: PhotoViewState,
                                 currentState: PhotoActionState): PhotoViewState {

        return when(currentState) {

            is PhotoActionState.LoadingState -> {
                previousState
                    .copy()
                    .isLoading(true)
                    .photos(null)
                    .error(null)
                    .build()
            }
            is PhotoActionState.DataState -> {

                val data = ArrayList<DataClassPhoto>()
                data.addAll(currentState.photos!!)
                previousState
                    .copy()
                    .photos(data)
                    .error(null)
                    .build()
            }
            is PhotoActionState.ErrorState -> {

                previousState
                    .copy()
                    .photos(null)
                    .error(currentState.error)
                    .build()
            }
        }
    }

    public override fun getViewStateObservable(): Observable<PhotoViewState> {
        return super.getViewStateObservable()
    }

}