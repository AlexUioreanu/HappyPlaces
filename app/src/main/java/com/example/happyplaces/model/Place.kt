package com.example.happyplaces.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Place(
    @PrimaryKey(autoGenerate = true) var id: Int=-1,
    var name: String,
    var destination: String,
    var activityType: String,
    var placeType: String,
    var dateTime: String,
    var note: String,
    var isFavorite: Boolean=false,
    var image: String,
    var overallMood: Int,
    var latitude: Double = 46.77,
    var longitude: Double = 23.62
) {
//    constructor(name: String,destination: String,activityType: String,placeType: String,dateTime: String,note: String,image: String,overallMood: Int) : this(name,destination,activityType,placeType,dateTime,note,image,overallMood){
//
//    }

}
