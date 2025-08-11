package com.example.simplegeolocationapp

import android.Manifest
import android.content.ContentProviderClient
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.widget.TextView
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient:FusedLocationProviderClient

    private lateinit var txtvLat: TextView
    private lateinit var txtvLong: TextView
    private lateinit var txtvArea: TextView
    private var latitude: Double = 0.0
    private  var longitude: Double = 0.0

    private val LOCATION_PERMISSION_REQUEST_CODE = 100

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        txtvLat = findViewById(R.id.txtvLat)
        txtvLong = findViewById(R.id.txtvLong)
        txtvArea = findViewById(R.id.txtvArea)
        var btnGetLocation = findViewById<Button>(R.id.btnGetLocation)

        btnGetLocation.setOnClickListener{
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                //permission request if not granted
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            } else {
                //only if permissions are already granted
                getLastKnownLocation()
                getAreaFromLocation()
            }
        }

    }
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun getLastKnownLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location->
                if (location != null) {
                    // use your location object
                    latitude = location.latitude
                    // get latitude , longitude and other info from this
                    longitude = location.longitude

                    //putting to textview
                    txtvLat.setText("Latitude: $latitude")
                    txtvLong.setText("Longetude: $longitude")
                }
            }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE &&
            grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLastKnownLocation()
            getAreaFromLocation()
        } else {
            txtvLat.text = "Permission denied"
            txtvLong.text = ""
        }
    }

    fun getAreaFromLocation() {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0]
                val area = address.adminArea // Or address.subAdminArea, address.adminArea, etc.
                txtvArea.setText("Current Area: $area")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}