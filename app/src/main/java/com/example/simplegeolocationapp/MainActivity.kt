package com.example.simplegeolocationapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
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
import android.widget.ProgressBar
import androidx.core.app.ActivityCompat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient:FusedLocationProviderClient
    private lateinit var txtvLat: TextView
    private lateinit var txtvLong: TextView
    private lateinit var txtvArea: TextView
    private lateinit var txtvWeatherToday: TextView
    private lateinit var txtvWeatherTomorrow: TextView
    private lateinit var progressBar: ProgressBar
    private var latitude: Double = 0.0
    private  var longitude: Double = 0.0
    private val LOCATION_PERMISSION_REQUEST_CODE = 100

    companion object {
        //sir wants a : private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    /*
    The way sir wanted it done:
    private lateinit var fusedLocationClient:FusedLocationProviderClient
    private lateinit var locationCallback:LocationCallback
     //Then it would be declaring the textviews(there is like 5
     // there is also the progress bar which ill add in my own as well but i imagine it will be here as well
     */

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
        txtvWeatherToday = findViewById(R.id.txtvWeatherToday)
        txtvWeatherTomorrow = findViewById(R.id.txtvWeatherTomorrow)
        progressBar = findViewById(R.id.progressBar)
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