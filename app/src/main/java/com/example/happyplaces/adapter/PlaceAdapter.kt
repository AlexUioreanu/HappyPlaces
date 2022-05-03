package com.example.happyplaces.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplaces.R
import com.example.happyplaces.databinding.ItemPlacesBinding
import com.example.happyplaces.model.Place
import com.squareup.picasso.Picasso

class PlaceAdapter(var placesList: List<Place>) :
    RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        return PlaceViewHolder(
            ItemPlacesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val currentPlace: Place = placesList[position]
        holder.itemBinding.apply {
            itemPlaceName.text = currentPlace.name
            itemDestination.text = currentPlace.destination
            itemType.text = currentPlace.placeType
            Picasso.get().load(currentPlace.image).into(itemImage)
            if(currentPlace.isFavorite){
                buttonFavorite.setImageResource(R.drawable.ic_baseline_whatshot_24)
            }
        }
    }

    override fun getItemCount(): Int {
        return placesList.size
    }


    class PlaceViewHolder(val itemBinding: ItemPlacesBinding) :
        RecyclerView.ViewHolder(itemBinding.root)
}