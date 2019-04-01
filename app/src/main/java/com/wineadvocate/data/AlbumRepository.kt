package com.wineadvocate.data

import com.wineadvocate.model.DataClassPhoto
import com.wineadvocate.repository.RequestInterface
import com.wineadvocate.repository.ServiceGenerator
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *  Created by Christian on Wednesday Mar, 2019
 */

object AlbumRepository {

    fun loadAlbum(albumId: String): Observable<ArrayList<DataClassPhoto>>? = getAlbumById(albumId)

    private fun getAlbumById(albumId: String): Observable<ArrayList<DataClassPhoto>>? {

        return ServiceGenerator
            .createAPIService(RequestInterface::class.java)
            .getAlbum(albumId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }
}