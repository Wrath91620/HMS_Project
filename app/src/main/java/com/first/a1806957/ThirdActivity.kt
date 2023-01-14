package com.first.a1806957

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.MapView
import com.huawei.hms.maps.MapsInitializer
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.adv.model.CustomLayerOption.TAG
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.Marker
import com.huawei.hms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ThirdActivity : AppCompatActivity() , OnMapReadyCallback {

    private lateinit var mapView : MapView


   override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_screen)
        mapView = findViewById(R.id.map_view)

        MapsInitializer.setApiKey("691F69600D96CCED688C17A31BCFE295213DED0757900A172678C60DBD950536")
        MapsInitializer.initialize(this)
        var mapViewBundle : Bundle? =null
        if(savedInstanceState != null ){

            mapViewBundle = savedInstanceState.getBundle("691F69600D96CCED688C17A31BCFE295213DED0757900A172678C60DBD950536")

        }

        mapView.onCreate(mapViewBundle)
        mapView.getMapAsync {
            getData();
        }

    }

    private fun getData() {
        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openchargemap.io/v3/")
            .build()
            .create(OMI::class.java)

        val retrofitData = retrofit.FetchPOILists("b90eb4f7-a6f7-476e-b18c-b865c58ae664");

        retrofitData.enqueue(object: Callback<List<POIListItem>> {
            override fun onResponse(call: Call<List<POIListItem>>, response: Response<List<POIListItem>>) {
                val response = response.body();

            }

            override fun onFailure(call: Call<List<POIListItem>>, t: Throwable) {

            }
        })
     }
    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onStop() {
        mapView.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onMapReady(huaweiMap: HuaweiMap) {
        Log.d(TAG, "onMapReady: ")
        val hmap = huaweiMap
        hmap?.mapType = HuaweiMap.MAP_TYPE_NORMAL
        hmap.isMyLocationEnabled = true
        hmap.uiSettings.isMyLocationButtonEnabled = true
        var myMarker : Marker? = null

        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val longitude = intent.getDoubleExtra("longitude", 0.0)



            val options = MarkerOptions()
               .position(LatLng(latitude, longitude))

                .title("you are here!")

            hmap.addMarker(options)


    }







}
