package com.wineadvocate.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 *  Created by Christian on Thursday Mar, 2019
 */

data class DataClassPhoto(@SerializedName("albumId")
                          @Expose val albumId: String,
                          @SerializedName("url")
                          @Expose val url: String,
                          @SerializedName("title")
                          @Expose val title: String)