package com.example.mikki.locaweatherkotlin.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.mikki.locaweatherkotlin.R
import com.example.mikki.locaweatherkotlin.adapter.PlacesAdapter
import com.example.mikki.locaweatherkotlin.data.model.Location
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.PlaceBufferResponse
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.app_bar_weather.*
import kotlinx.android.synthetic.main.content_weather.*


class WeatherActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var placesAdapter: PlacesAdapter
    lateinit var latLng: LatLng
    lateinit var mGeoDataClient: GeoDataClient
    var isAutoCompleteLocation = false
    val REQUEST_LOCATION = 1011
    val BOUNDS_INDIA = LatLngBounds(LatLng(23.63936, 68.14712), LatLng(28.20453, 97.34466))


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        setSupportActionBar(toolbar)

        mGeoDataClient = Places.getGeoDataClient(this)

        placesAdapter = PlacesAdapter(this, android.R.layout.simple_list_item_1, mGeoDataClient, null, BOUNDS_INDIA)
        enter_place.setAdapter(placesAdapter)
        enter_place.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count > 0) {
                    cancel.visibility = View.VISIBLE
                } else {
                    cancel.visibility = View.GONE
                }
            }
        })
        enter_place.setOnItemClickListener({ parent, view, position, id ->

            hideKeyboard()
            val item = placesAdapter.getItem(position)
            val placeId = item?.getPlaceId()
            val primaryText = item?.getPrimaryText(null)

            Log.i("Autocomplete", "Autocomplete item selected: "
                    + primaryText)


            val placeResult = mGeoDataClient.getPlaceById(placeId)

            placeResult.addOnCompleteListener(object : OnCompleteListener<PlaceBufferResponse> {
                override fun onComplete(task: Task<PlaceBufferResponse>) {
                    val places = task.getResult()
                    val place = places!!.get(0)

                    val placeId = place.id
                    isAutoCompleteLocation = true
                    latLng = place.latLng

                    var str = latLng.toString()
                    Log.i("Autocomplete",
                        "place: " + primaryText + " latLng " + latLng)
                    val latlongStr = str.subSequence(10,str.length-1)

                    Log.i("Autocomplete",
                        "place: " + primaryText + " latLng " + latlongStr)

                    val latLngArr = latlongStr.split(",".toRegex())

                    val cityLatLon = Location(
                        city = primaryText.toString(),
                        lat = latLngArr[0],
                        lon = latLngArr[1]
                    )

                    val bundle = Bundle()
                    bundle.putParcelable("data", cityLatLon)
                    val fragment = WeatherFragment()
                    fragment.arguments = bundle
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.mainContent, fragment).commit()

                    places.release()

                }
            })

        })
        cancel.setOnClickListener {
            enter_place.setText("")
        }




        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    /* To hide Keyboard */
    fun hideKeyboard() {
        try {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //initLocation()
            } else {
                Toast.makeText(this@WeatherActivity, R.string.permission_denied, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION)

    }

    override fun onResume() {
        super.onResume()
        if (checkPermissions()) {

        } else {
            requestPermissions();
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.weather, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
