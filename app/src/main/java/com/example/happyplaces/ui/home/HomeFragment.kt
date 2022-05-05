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


        val trip1 = Place(
            1,
            "Boys Night",
            "Paris",
            "City Break",
            "City",
            "13-12-2021",
            "Very nice was",
            true,
            "https://www.aerocenter.ro/LocationFirstFileHandler/1200/1200/1023.jpg",
            2,
            48.8566,2.3522
        )
        dao.addPlace(trip1)

        showData(dao.getAllPlaces() as List<Place?>)

        return root
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