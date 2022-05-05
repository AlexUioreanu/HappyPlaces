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
    var image: String,
    var overallMood: Int,
    var lat: Double,
    var long: Double
){
    constructor() : this(0,"","","","","","",false,"",0,0.0,0.0)
    constructor(name: String, destination: String, activityType: String, placeType: String, note: String, date: String, mood: Int, image: String) : this()
}
