package com.example.happyplaces.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.happyplaces.adapter.PlaceAdapter
import com.example.happyplaces.databinding.FragmentHomeBinding
import com.example.happyplaces.model.Place
import com.example.happyplaces.model.PlaceDao
import com.example.happyplaces.model.PlaceDataBase

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    lateinit var dataBase: PlaceDataBase
    lateinit var dao: PlaceDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dataBase = context?.let { PlaceDataBase.getDatabase(it) }!!
        dao = dataBase.getTripDao()


        prePopulateData()


        showData(dao.getAllPlaces() as List<Place?>)

        return root
    }

    private fun prePopulateData() {

        val place1 = Place(
            1,
            "Honeymoon",
            "Paris",
            "City Break",
            "City",
            "13-12-2021",
            "Very nice was",
            true,
            "https://www.aerocenter.ro/LocationFirstFileHandler/1200/1200/1023.jpg",
            2,
            48.8566, 2.3522
        )
        val place2 = Place(
            id = 2,
            name = "Boys Night",
            destination = "Prague",
            activityType = "City Break",
            placeType = "City",
            dateTime = "11-12-2021",
            note = "I drink a lot ",
            isFavorite = false,
            image = "https://www.unde-si-cand.net/site/images/illustration/oualler/republique-tcheque-prague_245.jpg",
            overallMood = 1,
            latitude = 50.07,
            longitude = 14.43
        )
        val place3 = Place(
            id = 3,
            name = "Shopping",
            destination = "Geneva",
            activityType = "City Break",
            placeType = "Mountains",
            dateTime = "20-12-2021",
            note = "I spend a lot of money Shitt !!",
            isFavorite = false,
            image = "https://s.iw.ro/gateway/g/ZmlsZVNvdXJjZT1odHRwJTNBJTJGJTJG/c3RvcmFnZTA2dHJhbnNjb2Rlci5yY3Mt/cmRzLnJvJTJGc3RvcmFnZSUyRjIwMjAl/MkYwOCUyRjI0JTJGMTIxOTg5NV8xMjE5/ODk1X0dldHR5SW1hZ2VzLTQ3NzE1OTMw/Ni5qcGcmdz0xMjAwJmg9NjMwJnpjPTEm/aGFzaD0yYWMxMWE3YzljMzVkNmE3YmQwYjFhMWUzODZmOGI2Ng==.thumb.jpg",
            overallMood = 2,
            latitude = 46.20, longitude = 6.14
        )
        val place4 = Place(
            id = 4,
            name = "Sky",
            destination = "Kaprun",
            placeType = "Mountains",
            activityType = "Sky",
            note = "I got muscle fever, 5 days in the bed also got sick",
            dateTime = "26-12-2021",
            isFavorite = false,
            image = "https://wallpaperaccess.com/full/3396997.jpg",
            overallMood = 2,
            latitude = 47.27, longitude = 12.75
        )
        val place5 = Place(
            id = 5,
            name = "Sea Party",
            destination = "Hawaii",
            placeType = "Sea Side",
            activityType = "Party",
            note = "I got sun burn and a lot of fun",
            dateTime = "16-07-2022",
            isFavorite = false,
            image = "https://thumbor.forbes.com/thumbor/960x0/https%3A%2F%2Fspecials-images.forbesimg.com%2Fimageserve%2F611d773f9569f3956a5e10a0%2FDiamond-Head-State-Park-Aerial%2F960x0.jpg%3Ffit%3Dscale",
            overallMood = 2,
            latitude = 19.89, longitude = 155.58
        )
        val place6 = Place(
            id = 6,
            name = "Visiting Trump",
            destination = "New York",
            activityType = "City Break",
            placeType = "City",
            note = "I hate Trump !!",
            dateTime = "12-03-2022",
            isFavorite = false,
            image = "https://i0.wp.com/www.agoda.com/wp-content/uploads/2020/07/Statue-of-Liberty-things-to-do-in-new-york-USA.jpg?ssl=1",
            overallMood = 0,
            latitude = 40.71, longitude = 74.00
        )
        dao.addPlace(place1)
        dao.addPlace(place2)
        dao.addPlace(place3)
        dao.addPlace(place4)
        dao.addPlace(place5)
        dao.addPlace(place6)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        showData(dao.getAllPlaces() as List<Place>)
    }

    private fun showData(houses: List<Place?>) {
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = PlaceAdapter(houses as List<Place>)
    }
}