package com.example.happyplaces.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplaces.AddEditActivity
import com.example.happyplaces.R
import com.example.happyplaces.ViewActivity
import com.example.happyplaces.databinding.ItemPlacesBinding
import com.example.happyplaces.model.Place
import com.example.happyplaces.model.PlaceDao
import com.example.happyplaces.model.PlaceDataBase
import com.squareup.picasso.Picasso

class PlaceAdapter(var placesList: List<Place>) :
    RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {

    class PlaceViewHolder(val itemBinding: ItemPlacesBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    lateinit var dataBase: PlaceDataBase
    lateinit var dao: PlaceDao


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        dataBase = PlaceDataBase.getDatabase(parent.context)
        dao = dataBase.getTripDao()

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
            if(currentPlace.image!="")
                Picasso.get().load(currentPlace.image).into(itemImage)

            if (currentPlace.isFavorite) {
                editBtFavorite.setImageResource(R.drawable.ic_baseline_whatshot_24)
            }else{editBtFavorite.setImageResource(R.drawable.ic_whatshot_light)}
            editBtFavorite.setOnClickListener(View.OnClickListener { it ->
                if (!currentPlace.isFavorite) {
                    dao.setFavorite(currentPlace.id)
                    editBtFavorite.setImageResource(R.drawable.ic_baseline_whatshot_24)

                } else   {
                    dao.removeFavorite(currentPlace.id)
                    editBtFavorite.setImageResource(R.drawable.ic_whatshot_light)
                }
            })

            when(currentPlace.overallMood){
                0 ->itemMood.setImageResource(R.drawable.ic_mood_bad_fill0_wght400_grad0_opsz48)
                1 -> itemMood.setImageResource(R.drawable.ic_neutral)
                2 -> itemMood.setImageResource(R.drawable.ic_sentiment_very_satisfied_fill0_wght400_grad0_opsz48)
            }

            itemLayout.setOnLongClickListener(View.OnLongClickListener {
                val i = Intent(it.context, AddEditActivity::class.java)
                i.putExtra(PlaceDataBase.DATABASE_NAME, currentPlace.id)
                it.context.startActivity(i)
                true
            })
            itemLayout.setOnClickListener(View.OnClickListener {
                val i = Intent(it.context, ViewActivity::class.java)
                i.putExtra(PlaceDataBase.DATABASE_NAME, currentPlace.id)
                it.context.startActivity(i)
            })
        }
    }

    fun getPlaceAt(position: Int): Place {
        return placesList[position]
    }

    override fun getItemCount(): Int {
        return placesList.size
    }


}