package com.example.happyplaces

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.happyplaces.databinding.ActivityViewBinding
import com.example.happyplaces.model.Place
import com.example.happyplaces.model.PlaceDao
import com.example.happyplaces.model.PlaceDataBase
import com.example.happyplaces.retrofit.RetrofitClientInstance
import com.example.happyplaces.retrofit.Weather
import com.example.happyplaces.retrofit.WeatherApi
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewActivity : AppCompatActivity(), OnMapReadyCallback {

    private val API_TOKEN = "39faa825fd03798946bc67fe7fb7a0db"

    lateinit var binding: ActivityViewBinding
    private lateinit var dataBase: PlaceDataBase
    private lateinit var dao: PlaceDao
    private lateinit var extras: Bundle
    private lateinit var viewPlace: Place
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

        dataBase = PlaceDataBase.getDatabase(applicationContext)
        dao = dataBase.getTripDao()




        extras = intent.extras!!
        val id = extras.getInt(PlaceDataBase.DATABASE_NAME)
        viewPlace = dao.getPlace(id)!!

        binding.apply {
            viewName.text = viewPlace.name
            viewDestination.text = viewPlace.destination
            viewActivityType.text = viewPlace.activityType
            viewPlaceType.text = viewPlace.placeType
            viewDate.text = viewPlace.dateTime
            viewNote.text = viewPlace.note
            Picasso.get().load(viewPlace.image).into(viewImage)

            when (viewPlace.overallMood) {
                0 -> binding.viewMood.setImageResource(R.drawable.ic_mood_bad_fill0_wght400_grad0_opsz48)
                1 -> binding.viewMood.setImageResource(R.drawable.ic_neutral)
                2 -> binding.viewMood.setImageResource(R.drawable.ic_sentiment_very_satisfied_fill0_wght400_grad0_opsz48)
            }
        }

        binding.viewBackBt.setOnClickListener(View.OnClickListener {
            finish()
        })

        val service: WeatherApi =
            RetrofitClientInstance.getRetrofitInstance().create(WeatherApi::class.java)
        val call: Call<Weather> = service.getWeather(
            viewPlace.destination,
            API_TOKEN,
            "metric"
        )
        call.enqueue(object : Callback<Weather?> {
            override fun onResponse(call: Call<Weather?>, response: Response<Weather?>) {
                if (response.isSuccessful) {
                    val main = response.body()!!
                    binding.viewTemp.text = main.currentTemperature.toString() +" C°"
                }
            }

            override fun onFailure(call: Call<Weather?>, t: Throwable) {
            }
        })


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val house = LatLng(viewPlace.latitude, viewPlace.longitude)
        mMap.addMarker(
            MarkerOptions()
                .position(house)
        )

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(house, 13F))

    }
}