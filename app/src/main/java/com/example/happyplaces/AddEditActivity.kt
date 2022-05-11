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
    private var photoURL: String? = null

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


            when(editablePlace.overallMood){
                0 -> binding.editImageMood.setImageResource(R.drawable.ic_mood_bad_fill0_wght400_grad0_opsz48)
                1 -> binding.editImageMood.setImageResource(R.drawable.ic_neutral)
                2 -> binding.editImageMood.setImageResource(R.drawable.ic_sentiment_very_satisfied_fill0_wght400_grad0_opsz48)
            }

            binding.editSeekBar.progress = editablePlace.overallMood

                photoURL = editablePlace.image
            uriGallery = Uri.parse(editablePlace.image)

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
            var image = binding.editImagePlace.toString()

            if(photoURL!=null){
                image= photoURL as String
            }else {image = uriGallery.toString().trim()
            }
            var uri: String = (image)


            val place = Place(
                name = name,
                destination = destination,
                activityType = activityType,
                placeType = placeType,
                note = note,
                dateTime = date,
                overallMood = mood,
                image = image
            )


            if (intent.extras!= null) {
                place.id = editablePlace.id
                dao.updatePlace(place)
            } else {
                dao.addPlace(place)
            }

            finish()
        })


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

//        editablePlace = Place(
//            1, "", "", "", "", "", "", false, "", 0, 46.77, 23.62
//        )
        if(intent.extras!=null){
            val place = this.editablePlace.latitude.let {
                this.editablePlace.longitude.let { it1 ->
                    LatLng(
                        it,
                        it1
                    )
                }
            }
            place.let {
                MarkerOptions()
                    .position(it)
            }.let {
                mMap.addMarker(
                    it
                )
            }
            place.let { CameraUpdateFactory.newLatLngZoom(it, 13f) }.let { mMap.moveCamera(it) }


        }else {

            val house = LatLng(46.77, 23.62)
            mMap.addMarker(
                MarkerOptions()
                    .position(house)
            )

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(house, 15f))
        }

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