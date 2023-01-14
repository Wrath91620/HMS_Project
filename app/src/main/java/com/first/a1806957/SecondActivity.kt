package com.first.a1806957




import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.hbb20.CountryCodePicker
import com.huawei.agconnect.auth.AGConnectAuth
import androidx.appcompat.widget.Toolbar
import com.huawei.hms.location.*


class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_screen)

        val locationText = findViewById<TextView>(R.id.LocationSchema)
        val locationButton = findViewById<ImageButton>(R.id.locationButton)
        val ccp: CountryCodePicker? = findViewById(R.id.ccp)

        ccp?.setOnCountryChangeListener {
            Toast.makeText(
                ccp.context,
                ccp.selectedCountryName,
                Toast.LENGTH_SHORT
            ).show()
        }
        val textView = findViewById<TextView>(R.id.text_view_userinfo)
        val LogInname = intent.getStringExtra("name")

        textView.setText("Hello " + LogInname + "!")

        locationButton.setOnClickListener {
            getLocation(locationText)
        }


        search()




    }

    fun getLocation(locationText: TextView) {
        // Create a FusedLocationProviderClient object


       val FLPC = LocationServices.getFusedLocationProviderClient(this)



        val locationRequest = LocationRequest().apply {
            interval = 10000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    if (location != null) {
                        locationText.text = "Latitude: ${location.latitude} \nLongitude: ${location.longitude}"





                    } else {
                        locationText.text = "Unable to get location"
                    }

                }
            }
        }
        FLPC.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())



        val locationButton = findViewById<ImageButton>(R.id.locationButton)
       locationButton.setOnClickListener {
               FLPC.lastLocation.addOnSuccessListener { location ->
                   if (location != null) {
                       locationText.text = "Latitude: ${location.latitude} \nLongitude: ${location.longitude}"

                       val intent = Intent(this, ThirdActivity::class.java)
                       intent.putExtra("latitude", location.latitude)
                       intent.putExtra("longitude", location.longitude)
                   } else {
                      locationText.text = "Unable to get Location"

                   }
               }
                   .addOnFailureListener { e ->
                       locationText.text = "Error: ${e.message}"
                   }



       }







       val clearButton = findViewById<Button>(R.id.ClearLocation)
       clearButton.setOnClickListener {
           locationText.text = "Location: "
       }





   }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_logout -> {


                    AGConnectAuth.getInstance().signOut()
                true

            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun search()
    {
        val searchPress = findViewById<Button>(R.id.search_button)
        searchPress.setOnClickListener {

            startActivity(Intent(this, ThirdActivity::class.java))


        }

    }






}


