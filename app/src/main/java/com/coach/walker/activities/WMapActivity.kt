package com.coach.walker.activities

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.support.v4.app.FragmentActivity
import com.coach.walker.R
import com.google.android.gms.maps.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.CameraUpdateFactory
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.*
import android.location.LocationListener
import android.os.*
import android.support.v4.content.ContextCompat
import com.google.android.gms.maps.model.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.Toast
import android.view.animation.LinearInterpolator
import android.view.View
import com.coach.walker.controllers.WServiceController
import com.coach.walker.events.WEvent
import com.coach.walker.utils.WApplicationConstants
import com.coach.walker.utils.WDirectionsJSONParser
import com.coach.walker.utils.WPrefsManager
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.w_map_activity.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject
import retrofit2.http.HTTP
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by pierre-alexandrevezinet on 07/12/2017.
 */

class WMapActivity : FragmentActivity(), OnMapReadyCallback, LocationSource.OnLocationChangedListener, SensorEventListener {

    public var mMap: GoogleMap? = null
    public var markerPoints: ArrayList<LatLng> = ArrayList()

    private val MY_PERMISSION_ACCESS_COARSE_LOCATION = 11
    private var mapFragment: SupportMapFragment? = null
    private var map: GoogleMap? = null
    private val mRotationMatrix = FloatArray(16)
    var mDeclination: Float = 0F
    var mSensorManager: SensorManager? = null
    var mAccelerometer: Sensor? = null
    var mRotator: Sensor? = null
    var mGyroscope: Sensor? = null
    var mTypeOrientation: Sensor? = null
    var isMarkerRotating = false
    var markerUser: Marker? = null
    var markerObjective: Marker? = null
    var markerOptionsUser: MarkerOptions? = null
    var markerOptionsObjective: MarkerOptions? = null
    var positionUser: LatLng? = null
    var positionGift: LatLng? = null
    var locationListener: LocationListener? = null
    var mobileLocation: Location? = null
    var locationManager: LocationManager? = null
    var isLocationChanged: Int = 0
    var prefsManager: WPrefsManager? = null
    var isMarkerUserChanged: Int = 0
    var bus = EventBus.getDefault()

    private var mLocationRequest: LocationRequest? = null;
    private var UPDATE_INTERVAL: Long = 10 * 1000;  /* 10 secs */
    private var FASTEST_INTERVAL: Long = 2000; /* 2 sec */
    private var builder: LocationSettingsRequest.Builder? = null
    val REQUEST_FINE_LOCATION = 99
    private var wServiceController: WServiceController? = null

    var polyLine: Polyline? = null
    var polyLineOptions: PolylineOptions = PolylineOptions()

    private var mMagnetometer: Sensor? = null
    private val mLastAccelerometer = FloatArray(3)
    private val mLastMagnetometer = FloatArray(3)
    private var mLastAccelerometerSet = false
    private var mLastMagnetometerSet = false
    private val mR = FloatArray(9)
    private val mOrientation = FloatArray(3)
    private val mCurrentDegree = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager!!.getSensorList(Sensor.TYPE_ACCELEROMETER)[0]
        mMagnetometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        mRotator = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        mGyroscope = mSensorManager!!.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        mTypeOrientation = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ORIENTATION)
        isMarkerRotating = false
        prefsManager = WPrefsManager(this)
        wServiceController = WServiceController(this, prefsManager!!)
        positionGift = LatLng(48.8737793,
                2.2950155999999424)

        setContentView(R.layout.w_map_activity)
        startLocationUpdates()
        mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment!!.getMapAsync(this)

        //LOCATION MANAGER
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        //add location change listener with update duration 2000 millicseconds or 10 meters

        //LOCATION LISTENER
        locationListener = object : LocationListener {
            override fun onStatusChanged(provider: String, status: Int,
                                         extras: Bundle) {
            }

            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
            override fun onLocationChanged(location: Location) {
                println("mobile location is in listener=" + location)
                mobileLocation = location
            }
        }

    }

    /*@SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {
        this.map = map
        if (checkPermissions()) {
            this.map!!.isMyLocationEnabled = true
            this.map!!.mapType = GoogleMap.MAP_TYPE_NORMAL
            this.map!!.isTrafficEnabled = false;
            this.map!!.isIndoorEnabled = false;
            this.map!!.isBuildingsEnabled = true;
            this.map!!.uiSettings.isCompassEnabled = true
            //updateUserMarkerPosition(LatLng(mobileLocation!!.latitude, mobileLocation!!.longitude))
        }
    }*/

    ///////////////////NEW CODE////////////////

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        var sydney = LatLng(-34.0, 151.0);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16f));

        mMap!!.setOnMapClickListener(object : GoogleMap.OnMapClickListener {
            override fun onMapClick(latLng: LatLng?) {
                if (markerPoints.size > 1) {
                    markerPoints.clear();
                    mMap!!.clear()
                }

                // Adding new item to the ArrayList
                markerPoints.add(latLng!!)

                // Creating MarkerOptions
                var options = MarkerOptions()

                // Setting the position of the marker
                options.position(latLng);

                if (markerPoints.size == 1) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                } else if (markerPoints.size == 2) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }

                // Add new marker to the Google Map Android API V2
                mMap!!.addMarker(options);

                // Checks, whether start and end locations are captured
                if (markerPoints.size >= 2) {
                    var origin: LatLng = markerPoints[0]
                    var dest: LatLng = markerPoints[1]

                    // Getting URL to the Google Directions API
                    var url = getDirectionsUrl(origin, dest)

                    var downloadTask = DownloadTask();

                    // Start downloading json data from Google Directions API
                    downloadTask.execute(url)
                }

            }

        })

    }

    inner class DownloadTask : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg url: String): String {
            var data: String = "";

            try {
                data = downloadUrl(url[0]);
            } catch (e: Exception) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val parserTask = ParserTask()
            parserTask.execute(result)
        }
    }

    inner class ParserTask : AsyncTask<String, Int, List<List<LatLng>>>() {

        override fun doInBackground(vararg jsonData: String?): List<List<LatLng>> {
            var jObject: JSONObject
            var routes: ArrayList<List<LatLng>> = ArrayList()

            try {
                jObject = JSONObject(jsonData[0]);
                var parser = WDirectionsJSONParser();

                routes = parser.parse(jObject) as ArrayList<List<LatLng>>
            } catch (e: Exception) {
                e.printStackTrace();
            }
            return routes;
        }

        override fun onPostExecute(result: List<List<LatLng>>?) {
            super.onPostExecute(result)

            var points: ArrayList<LatLng> = ArrayList()
            var lineOptions = PolylineOptions()
            var markerOptions = MarkerOptions()


            for (i in 0 until result!!.size) {
                points = ArrayList()
                lineOptions = PolylineOptions()

                var path = result[i]

                for (j in 0 until path.size) {
                    var point = path[j];

                    var lat = point.latitude
                    var lng = point.longitude
                    var position = LatLng(lat, lng)

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12f);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);

            }

// Drawing polyline in the Google Map for the i-th route
            mMap!!.addPolyline(lineOptions);
        }
    }

    fun getDirectionsUrl(origin: LatLng, dest: LatLng): String {

        // Origin of route
        val str_origin = "origin=" + origin.latitude + "," + origin.longitude

        // Destination of route
        val str_dest = "destination=" + dest.latitude + "," + dest.longitude

        // Sensor enabled
        val sensor = "sensor=false"
        val mode = "mode=driving"
        val key = "key=AIzaSyCYRbuFzCWkrjE07-g5WMx8IE8ygEFSXZE"

        // Building the parameters to the web service
        val parameters = "$str_origin&$str_dest&$sensor&$mode&$key"

        // Output format
        val output = "json"

        // Building the url to the web service

        return "https://maps.googleapis.com/maps/api/directions/$output?$parameters"
    }

    fun downloadUrl(strUrl: String): String {
        var data = "";
        var iStream: InputStream? = null
        var urlConnection: HttpURLConnection? = null
        try {
            var url = URL(strUrl)
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.connect()
            iStream = urlConnection.inputStream
            var br = BufferedReader(InputStreamReader(iStream))

            var sb: StringBuffer = StringBuffer()

            var line = "";
            while ((line == br.readLine()) != null) {
                sb.append(line)
            }

            data = sb.toString()
            br.close()

        } catch (e: Exception) {
            Log.d("Exception", e.toString());
        } finally {
            iStream!!.close();
            urlConnection!!.disconnect()
        }
        return data;
    }

///////////////END NEW CODE//////////////

    override fun onLocationChanged(location: Location?) {
       /* this.map!!.uiSettings.isCompassEnabled = true
        // New location has now been determined
        tv_maps_location.text = "Latitude = " + location!!.latitude + ", Longitude = " + location!!.longitude + "\n zoom level : " + this.map!!.cameraPosition.zoom
        // You can now create a LatLng Object for use with maps
        val latLng = LatLng(location!!.latitude, location.longitude)
        isLocationChanged++
        if (isLocationChanged == 1) {
            //wServiceController!!.execute(location!!.latitude.toString() + "," + location.longitude.toString() + "|" + positionGift!!.latitude.toString() + "," + positionGift!!.longitude.toString(), "true", ULApplicationConstants.ULP_GOOGLE_MAP_KEY_DEVELOPPER, ULApplicationConstants.GL_GET_SNAP_TO_ROADS)
        }

        updateUserMarkerPosition(latLng)pax*/
        //changePositionSmoothly(markerUser,latLng)

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent) {

        /*  if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD){
              mLastMagnetometerSet = true
          }

          if (event.sensor.type == Sensor.TYPE_ACCELEROMETER){
              mLastAccelerometerSet = true
          }


         if (mLastAccelerometerSet && mLastMagnetometerSet) {
              //Toast.makeText(applicationContext, "L'accelérometre bouge", Toast.LENGTH_SHORT).show()
              SensorManager.getRotationMatrixFromVector(
                      mRotationMatrix, event.values)
              val orientation = FloatArray(3)
              SensorManager.getOrientation(mRotationMatrix, orientation)
              var bearing : Double = Math.toDegrees(orientation[2].toDouble()) + mDeclination

              //SI ACCESS_COARSE_LOCATION N EST PAS AUTORISE
              if (ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                  ActivityCompat.requestPermissions( this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),
                          MY_PERMISSION_ACCESS_COARSE_LOCATION )
                  mobileLocation = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)

              }else{
                  locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10f, locationListener)

              }

              if (mobileLocation != null) {
                  //locationManager!!.removeUpdates(locationListener)


                  //bearing = bearingBetweenLocations(LatLng(mobileLocation!!.latitude, mobileLocation!!.longitude)!!, positionGift!!)
                  updateCamera(bearing.toFloat())

                  tv_maps_location.text = "Latitude = " + mobileLocation!!.latitude + ", Longitude = " + mobileLocation!!.longitude

              }
          }*/

    }

    private fun updateUserMarkerPosition(mobileLocation: LatLng) {

        positionUser = mobileLocation
        markerOptionsUser = MarkerOptions().position(positionUser!!).title("My new position : " + positionUser!!.latitude.toString() + ", " + positionUser!!.longitude.toString() + "")
        markerOptionsObjective = MarkerOptions().position(positionGift!!).title("My Gift position")

        this.map!!.uiSettings.isCompassEnabled = true
        this.map!!.mapType = GoogleMap.MAP_TYPE_NORMAL

        //REFRESH PARKER USER POSITION
        if (markerUser != null) {
            markerUser!!.remove()
        }

        markerUser = this.map!!.addMarker(markerOptionsUser)
        markerUser!!.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.markeruser))
        markerUser!!.position = positionUser
        //rotateMarker(markerUser!!,bearingBetweenLocations(positionUser!!, positionGift!!).toFloat(),this!!.map!!)
        markerObjective = this.map!!.addMarker(markerOptionsObjective)
        markerObjective!!.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.johnny))

        //REFRESH POLYLINE POSITIONS
        if (polyLine != null) {
            polyLine!!.remove()
        }

        polyLineOptions = PolylineOptions()
        polyLineOptions
                .color(ContextCompat.getColor(this, R.color.colorOrange))
                .add(positionUser)
                .add(positionGift)
        polyLine = this.map!!.addPolyline(polyLineOptions)

        val cameraPosition = CameraPosition.Builder()
                .target(positionUser)
                .zoom(21f)
                .tilt(33f)
                .target(LatLng(positionUser!!.latitude, positionUser!!.longitude))
                .bearing(bearingBetweenLocations(positionUser!!, positionGift!!).toFloat())
                .build()
        isMarkerUserChanged++
        if (isMarkerUserChanged == 1) {
            this.map!!.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            //this.map!!.animateCamera(CameraUpdateFactory.newLatLngZoom(positionUser, 26f))
        }

    }

/*  fun bearingBetweenLocations(latLng1: LatLng, latLng2: LatLng): Double {

      var PI = 3.14159;
      var lat1 = latLng1.latitude * PI / 180;
      var long1 = latLng1.longitude * PI / 180;
      var lat2 = latLng2.latitude * PI / 180;
      var long2 = latLng2.longitude * PI / 180;

      var dLon = (long2 - long1);

      var y = Math.sin(dLon) * Math.cos(lat2);
      var x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon);

      var brng = Math.atan2(y, x);

      brng = Math.toDegrees(brng);
      brng = (brng + 360) % 360;

      return brng;

  }*/

    fun bearingBetweenLocations(startPoint: LatLng, endPoint: LatLng): Double {
        var longitude1 = startPoint.longitude;
        var latitude1 = Math.toRadians(startPoint.latitude);

        var longitude2 = endPoint.longitude;
        var latitude2 = Math.toRadians(endPoint.latitude);

        var longDiff = Math.toRadians(longitude2 - longitude1);

        var y = Math.sin(longDiff) * Math.cos(latitude2);
        var x = Math.cos(latitude1) * Math.sin(latitude2) - Math.sin(latitude1) * Math.cos(latitude2) * Math.cos(longDiff);

        return Math.toDegrees(Math.atan2(y, x));

    }

    fun changePositionSmoothly(marker: Marker?, newLatLng: LatLng) {
        if (marker == null) {
            return;
        }
        val animation = ValueAnimator.ofFloat(0f, 100f)
        var previousStep = 0f
        val deltaLatitude = newLatLng.latitude
        val deltaLongitude = newLatLng.longitude
        animation.duration = 1500
        animation.addUpdateListener { updatedAnimation ->
            val deltaStep = updatedAnimation.animatedValue as Float - previousStep
            previousStep = updatedAnimation.animatedValue as Float
            marker.position = LatLng(marker.position.latitude + deltaLatitude * deltaStep * 1 / 100, marker.position.longitude + deltaStep * deltaLongitude * 1 / 100)
        }
        animation.start()
    }

    fun updateCamera(bearing: Float) {
        var currentPlace: CameraPosition = CameraPosition.Builder()
                .target(LatLng(mobileLocation!!.latitude, mobileLocation!!.longitude))
                .bearing(bearing).tilt(65.5f).zoom(18f).build();
        //this.map!!.moveCamera(CameraUpdateFactory.newCameraPosition(currentPlace));
        this.map!!.animateCamera(CameraUpdateFactory.newCameraPosition(currentPlace), 10, null);
    }

    @SuppressLint("MissingPermission")
            // Trigger new location updates at interval
    fun startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = LocationRequest();
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY;
        mLocationRequest!!.interval = UPDATE_INTERVAL;
        mLocationRequest!!.fastestInterval = FASTEST_INTERVAL;

        // Create LocationSettingsRequest object using location request
        builder = LocationSettingsRequest.Builder()
        builder!!.addLocationRequest(mLocationRequest!!);
        var locationSettingsRequest: LocationSettingsRequest = builder!!.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        var settingsClient: SettingsClient = LocationServices.getSettingsClient(this)
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                // do work here
                onLocationChanged(locationResult.lastLocation)
            }

        }, Looper.myLooper())

    }

    /**
     * This method is automatically called when a eventbus event is worked
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMapActivityEvent(event: WEvent) {

        var mObject: Any? = event.wEvent
        var jsonObject: Any? = null
        if (event.eventName.equals(WApplicationConstants.GL_GET_SNAP_TO_ROADS)) {

            try {
                //IF THERE ARE ERRORS
                if (mObject.toString().contains("error")) {
                    Toast.makeText(this, "Erreur de connexion", Toast.LENGTH_SHORT).show()
                    circular_progress_maps_activity.visibility = View.GONE
                    circular_progress_maps_activity.stop()
                } else {

                }

            } catch (e: Exception) {
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun checkPermissions(): Boolean {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mobileLocation = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            return true
        } else {
            requestPermissions()
            mobileLocation = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            return false
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_FINE_LOCATION)
    }

    override fun onResume() {
        super.onResume()
        // turnGPSOn()
        if (!bus.isRegistered(this)) {
            bus.register(this)
        }
        //getPermission()
        mSensorManager!!.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        //mSensorManager!!.registerListener(this, mRotator, SensorManager.SENSOR_DELAY_NORMAL)
        //mSensorManager!!.registerListener(this, mTypeOrientation, SensorManager.SENSOR_DELAY_NORMAL)
        // mSensorManager!!.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL)
        mSensorManager!!.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL)

    }

    override fun onStop() {
        super.onStop()
        if (bus.isRegistered(this)) {
            bus.unregister(this)
        }
    }

    fun rotateMarker(marker: Marker, toRotation: Float, map: GoogleMap) {
        val handler = Handler()
        val start = SystemClock.uptimeMillis()
        val startRotation = marker.rotation
        val duration: Long = 1555

        val interpolator = LinearInterpolator()

        handler.post(object : Runnable {
            override fun run() {
                val elapsed = SystemClock.uptimeMillis() - start
                val t = interpolator.getInterpolation(elapsed.toFloat() / duration)

                val rot = t * toRotation + (1 - t) * startRotation

                marker.rotation = if (-rot > 180) rot / 2 else rot
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16)
                }
            }
        })
    }

}
