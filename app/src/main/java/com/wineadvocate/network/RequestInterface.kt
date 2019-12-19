package com.wineadvocate.network

import com.wineadvocate.model.Album
import com.wineadvocate.model.Photo
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *  Created by Christian on Thursday Mar, 2019
 */

interface RequestInterface {

    @GET("/photos")
    fun getPhotos(@Query("albumId") albumId: String) : Observable<ArrayList<Photo>>

    @GET("/albums")
    fun getAlbums(@Query("userId") userId: String) : Single<List<Album>>
}
