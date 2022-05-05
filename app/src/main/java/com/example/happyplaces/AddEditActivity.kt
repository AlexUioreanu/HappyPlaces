package com.example.happyplaces

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.AdapterView
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.example.happyplaces.databinding.ActivityAddEditBinding
import com.example.happyplaces.model.Place
import com.example.happyplaces.model.PlaceDao
import com.example.happyplaces.model.PlaceDataBase
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AddEditActivity : AppCompatActivity(), OnMapReadyCallback {
    val CAMERA_REQUEST_CODE = 101
    val GALLERY_REQUEST_CODE = 102
    private lateinit var pathToFile: String
    private lateinit var uriGallery: Uri
    private lateinit var binding: ActivityAddEditBinding
    private lateinit var dataBase: PlaceDataBase
    private lateinit var dao: PlaceDao
    private lateinit var extras: Bundle
    private lateinit var editablePlace: Place
    private lateinit var mMap: GoogleMap

    private var year = 0
    private var month = 0
    private var day = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

        dataBase = PlaceDataBase.getDatabase(applicationContext)
        dao = dataBase.getTripDao()

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.edit_map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)


        if (intent.extras != null) {
            extras = intent.extras!!
            val id = extras.getInt(PlaceDataBase.DATABASE_NAME)
            editablePlace = dao.getPlace(id)!!

            binding.editName.setText(editablePlace.name)
            binding.editDestination.setText(editablePlace.destination)

            binding.editActivityType.setSelection(
                resources.getStringArray(R.array.activityType)
                    .indexOf(editablePlace.activityType)
            )
            binding.editPlaceType.setSelection(
                resources.getStringArray(R.array.placeType).indexOf(editablePlace.placeType)
            )
            binding.editNote.setText(editablePlace.note)
            binding.editDateTxT.setText(editablePlace.dateTime)

            if (editablePlace.overallMood == 0) {
                binding.editImageMood.setImageResource(R.drawable.ic_mood_bad_fill0_wght400_grad0_opsz48)
            } else if (editablePlace.overallMood == 1) {
                binding.editImageMood.setImageResource(R.drawable.ic_neutral)
            } else {
                binding.editImageMood.setImageResource(R.drawable.ic_sentiment_very_satisfied_fill0_wght400_grad0_opsz48)
            }

            binding.editSeekBar.progress = editablePlace.overallMood
            Picasso.get().load(editablePlace.image).into(binding.editImagePlace)
        }


        binding.editBtImagePlace.setOnClickListener(View.OnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(
                gallery, GALLERY_REQUEST_CODE
            )
        })

        binding.editBtDate.setOnClickListener(View.OnClickListener {
            val calendar = Calendar.getInstance()
            year = calendar[Calendar.YEAR]
            month = calendar[Calendar.MONTH]
            day = calendar[Calendar.DAY_OF_MONTH]
            calendar.time

            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, month, dayOfMonth ->
                    binding.editDateTxT.text =
                        dayOfMonth.toString() + "-" + (month + 1) + "-" + year
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        })

        binding.editSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                binding.editSeekBar.progress = p1
                if (binding.editSeekBar.progress == 0) {
                    binding.editImageMood.setImageResource(R.drawable.ic_mood_bad_fill0_wght400_grad0_opsz48)
                } else if (binding.editSeekBar.progress == 1) {
                    binding.editImageMood.setImageResource(R.drawable.ic_neutral)
                } else {
                    binding.editImageMood.setImageResource(R.drawable.ic_sentiment_very_satisfied_fill0_wght400_grad0_opsz48)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                if (binding.editSeekBar.progress == 0) {
                    binding.editImageMood.setImageResource(R.drawable.ic_mood_bad_fill0_wght400_grad0_opsz48)
                } else if (binding.editSeekBar.progress == 1) {
                    binding.editImageMood.setImageResource(R.drawable.ic_neutral)
                } else {
                    binding.editImageMood.setImageResource(R.drawable.ic_sentiment_very_satisfied_fill0_wght400_grad0_opsz48)
                }
            }
        })

        binding.editSaveBt.setOnClickListener(View.OnClickListener {
            val name = binding.editName.text.toString()
            val destination = binding.editDestination.text.toString()
            val activityType = binding.editActivityType.selectedItem.toString()
            val placeType = binding.editPlaceType.selectedItem.toString()
            val note = binding.editNote.text.toString()
            val date = binding.editDateTxT.text.toString()
            val mood = binding.editSeekBar.progress
            val image = binding.editImagePlace.toString()


            val place = Place(name, destination, activityType, placeType, note, date, mood, image)

            place.id = editablePlace.id
            dao.updatePlace(place)

            finish()
        })


//        val trip1 = Place(
//            1,
//            "Boys Night",
//            "Paris",
//            "Party",
//            "City",
//            "13-12-2021",
//            "Very nice was",
//            false,
//            "https://www.aerocenter.ro/LocationFirstFileHandler/1200/1200/1023.jpg",
//            99
//        )
//        dao.addPlace(trip1)

//        val trip2 = Trip(
//            "Boys Night",
//            "Prague",
//            "City Break",
//            3800,
//            4.5,
//            "13-01-2022",
//            "15-12-2022",
//            "https://www.unde-si-cand.net/site/images/illustration/oualler/republique-tcheque-prague_245.jpg"
//        )
//        val trip3 = Trip(
//            "Shopping",
//            "Geneva",
//            "City Break",
//            5000,
//            3.5,
//            "20-12-2021",
//            "26-12-2021",
//            "https://s.iw.ro/gateway/g/ZmlsZVNvdXJjZT1odHRwJTNBJTJGJTJG/c3RvcmFnZTA2dHJhbnNjb2Rlci5yY3Mt/cmRzLnJvJTJGc3RvcmFnZSUyRjIwMjAl/MkYwOCUyRjI0JTJGMTIxOTg5NV8xMjE5/ODk1X0dldHR5SW1hZ2VzLTQ3NzE1OTMw/Ni5qcGcmdz0xMjAwJmg9NjMwJnpjPTEm/aGFzaD0yYWMxMWE3YzljMzVkNmE3YmQwYjFhMWUzODZmOGI2Ng==.thumb.jpg"
//        )
//        val trip4 = Trip(
//            "Sky",
//            "Kaprun",
//            "Mountains",
//            3000,
//            5,
//            "26-12-2021",
//            "01-01-2022",
//            "https://wallpaperaccess.com/full/3396997.jpg"
//        )
//        val trip5 = Trip(
//            "Sea Party",
//            "Hawaii",
//            "Sea Side",
//            4500,
//            5,
//            "16-07-2022",
//            "23-07-2022",
//            "https://thumbor.forbes.com/thumbor/960x0/https%3A%2F%2Fspecials-images.forbesimg.com%2Fimageserve%2F611d773f9569f3956a5e10a0%2FDiamond-Head-State-Park-Aerial%2F960x0.jpg%3Ffit%3Dscale"
//        )
//        val trip6 = Trip(
//            "Visiting Trump",
//            "New York",
//            "City Break",
//            2000,
//            1,
//            "12-03-2022",
//            "20-03-2022",
//            "https://i0.wp.com/www.agoda.com/wp-content/uploads/2020/07/Statue-of-Liberty-things-to-do-in-new-york-USA.jpg?ssl=1"
//        )


        val activityType = resources.getStringArray(R.array.activityType)
//        val arrayAdapter =
//            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,activityType)
//        binding.editActivityType.adapter = arrayAdapter


        binding.editActivityType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    activityType[p2]
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0

        editablePlace = Place(
            1, "", "", "", "", "", "", false, "", 0, 46.77, 23.62
        )


        // Add a marker in Sydney and move the camera

        // Add a marker in Sydney and move the camera
        val place = LatLng(this.editablePlace.lat, this.editablePlace.long)
        mMap.addMarker(
            MarkerOptions()
                .position(place)
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 13f))

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val f: File = File(pathToFile)
                binding.editImagePlace.setImageURI(Uri.fromFile(f))
                Log.d("tag", "Photo URL: " + Uri.fromFile(f))
                val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                val contentUri = Uri.fromFile(f)
                mediaScanIntent.data = contentUri
                this.sendBroadcast(mediaScanIntent)
            }
        }
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                assert(data != null)
                uriGallery = data!!.data!!
                val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val imageFileName = "TravelJournal_" + timeStamp + "." + getFileExt(uriGallery)
                Log.d("tag", "Gallery URI:  $imageFileName")
                binding.editImagePlace.setImageURI(uriGallery)
            }
        }
    }

    private fun getFileExt(contentUri: Uri): String? {
        val c = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(c.getType(contentUri))
    }

    override fun onResume() {
        super.onResume()
    }
}