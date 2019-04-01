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

object PhotoRepository {

    fun loadPhoto() : Observable<ArrayList<DataClassPhoto>>? = getPhoto()

    private fun getPhoto() : Observable<ArrayList<DataClassPhoto>>? {

        return ServiceGenerator
            .createAPIService(RequestInterface::class.java)
            .getPhotos()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }
}