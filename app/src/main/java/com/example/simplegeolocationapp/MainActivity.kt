@file:Suppress("DEPRECATION")

package com.example.simplegeolocationapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.PixelCopy
import android.view.View
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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.privacysandbox.tools.core.model.Method
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
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
        val btnGetLocation = findViewById<Button>(R.id.btnGetLocation)

        btnGetLocation.setOnClickListener{
            progressBar.visibility = View.VISIBLE

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
        progressBar.visibility=View.VISIBLE
        /*
        Sirs button and main thing
         */
    }

    /*
    Sirs methods:


    private fun checkGPSEnabled(){
        val locationManager = GetSystemService(LOCATION_SERVICE) as locationManeger
        if(!locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)){
        Toast.makeText(this,"Please enable GPS")//more stuff
        startActivity(Intent(Settings.Action_LOCATION_SOURCE_SETTINGS))
        }
    }

    private fun getCurrentLocation(){
        if(ContextCompact.checkSelfPermission(this, Manifestr.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSIN_GRANTED){
            ReequestNewLocation()
        }else{
            ActivityCompact.requestPermissions(this,arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COURSE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun requestNewLocation(){

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            1000L).apply{
                setWaitForAccurateLocation(true)
                setMinUpdateIntervalMillis(500)
                setMaxUpdates(1)
    }.build()
    }

    private fun showLocationDetails(location: Location){


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            geocoder.getFromLocation(lat,lon, (Auto added greyed out thing "maxResults")1) { addresses ->
                if(!addresses.isNullOrEmpty()) {
                    runOnUiThread {
                        tvAddress.text = "Address:${addresses[0].getAddressLine((greyed out thing "index")0)}"
                    }
                }else {
                    runOnUiThread{
                        tvAddress.text = "Address not found"
                    }
                }
                progressBar.visibilty = View.GONE
            }
        }else{
            try{
                val addresses = geocoder.getFromLocation(lat,lon,(greyed out thing "maxResults")1)
                tvAdress.text = if(!addresses.isNullOrEmpty()){
                    "Address:${addresses[0].getAddressLine((greyed out hting "index")0)}"
                } else {
                    "Address : Not Found"
                }
            } catch (e:Exception){
                e.printStackTrace()
                tvAddress.text = "Address:Error retriving"
            }finally{
                progressBar.visibility = View.GONE
            }
        }else{
            tvAddress.text = "Address:Geocoder not available"
            progressBar.visibilty = View.Gone
        }
    }


     */

    @SuppressLint("SetTextI18n")
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

    @SuppressLint("SetTextI18n")
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

    @SuppressLint("SetTextI18n")
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

    @SuppressLint("SetTextI18n")
    private fun fetchweatherForcast(latitude: Double, longitude: Double){
        val apiKey = "a28e280d385ad5d8379a873d8d135ec2"//put api key here
        val url = "https://api.openweathermap.org/data/2.5/forecast?lat=$latitude&lon=$longitude&appid=$apiKey"
        val request = JsonObjectRequest(DownloadManager.Request.Method.GET,url,null,)
        {response ->
            try {
                val list = response.getJSONArray("list")
                val today = list.getJSONObject(0)
                val todayTempreture = today.getJSONObject("main").getDouble("temp")
                val todayDescription = today.getJSONArray("weather").getJSONObject(0).getString("description")

                val tomorrow = list.getJSONObject(8)//24 hours later
                val tomorrowsTempreture = tomorrow.getJSONObject("main").getDouble("temp")
                val tomorrowsDescription = tomorrow.getJSONArray("weather").getJSONObject(8).getString("description")

                txtvWeatherToday.text = "Todays weather: $todayTempreture°C and will be like $todayDescription"
                txtvWeatherTomorrow.text = "Tomorrows weather: $tomorrowsTempreture°C and will be like $tomorrowsDescription"

            }catch (e: Exception){
                e.printStackTrace()
                Toast.makeText(this,"Error parsing weather data", Toast.LENGTH_SHORT).show()
            }
            progressBar.visibility = View.GONE
        },
    }

}