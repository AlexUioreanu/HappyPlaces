package com.example.happyplaces.model

import androidx.room.*

@Dao
interface PlaceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPlace(place: Place?)

    @Update
    fun updatePlace(place: Place?)

    @Delete
    fun deletePlace(place: Place?)

    @Query("SELECT * FROM Place WHERE isFavorite = 1")
    fun getAllFavoritePlaces(): List<Place?>?

    @Query("DELETE FROM Place WHERE id = :placeId")
    fun deletePlaceById(placeId: Long)

    @Query("UPDATE Place SET isFavorite = 1 WHERE id = :placeId")
    fun setFavorite(placeId: Int)

    @Query("UPDATE Place SET isFavorite = 0 WHERE id = :placeId")
    fun removeFavorite(placeId: Int)

    @Query("SELECT * FROM Place ORDER BY id ")
    fun getAllPlaces(): List<Place?>?

    @Query("SELECT * FROM Place WHERE id = :placeId")
    fun getPlace(placeId: Int): Place?
}