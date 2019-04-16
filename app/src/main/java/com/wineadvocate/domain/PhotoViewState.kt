package com.wineadvocate.domain

import com.wineadvocate.model.DataClassPhoto

/**
 *  Created by Christian on Tuesday Mar, 2019
 */

data class PhotoViewState(
    val isLoading: Boolean = false,
    val photos: ArrayList<DataClassPhoto>? = null,
    val error: Throwable? = null) {

    fun copy(): Builder {
        return Builder(this)
    }

    class Builder(photoViewState: PhotoViewState) {
        private var isLoading = photoViewState.isLoading
        private var photosObject = photoViewState.photos
        private var error = photoViewState.error


        fun isLoading(isLoading: Boolean): Builder {
            this.isLoading = isLoading
            return this
        }

        fun photos(photosObject: ArrayList<DataClassPhoto>?): Builder {
            this.photosObject = photosObject
            return this
        }

        fun error(error: Throwable?): Builder {
            this.error = error
            return this
        }

        fun build(): PhotoViewState {
            return PhotoViewState(isLoading, photosObject, error)
        }
    }
}