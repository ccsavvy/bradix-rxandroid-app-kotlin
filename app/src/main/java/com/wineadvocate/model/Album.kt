package com.wineadvocate.model

import com.google.gson.annotations.SerializedName

/*
    userIdLabel": 1,
    "id": 1,
    "title": "quidem molestiae enim"
 */

data class Album(
    val userId: String,
    val id: String,
    val title: String
)
