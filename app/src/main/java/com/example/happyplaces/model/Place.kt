package com.example.happyplaces.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Place(

    @PrimaryKey(autoGenerate = true) var id: Int,

    var name: String,
    var destination: String,
    var activityType: String,
    var placeType: String,
    var dateTime: String,
    var note: String,
    var isFavorite: Boolean,
    var image: String
)
