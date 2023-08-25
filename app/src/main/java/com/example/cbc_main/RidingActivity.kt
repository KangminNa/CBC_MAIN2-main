package com.example.cbc_main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.room.Room
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.threeten.bp.format.DateTimeFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.ExperimentalTime


class RidingActivity : AppCompatActivity(), LocationListener, OnMapReadyCallback {
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private val INTERVAL: Long = 2000
    private val FASTEST_INTERVAL: Long = 1000
    private var mLastLocation: Location? = null
    internal lateinit var mLocationRequest: LocationRequest
    private val REQUEST_PERMISSION_LOCATION = 10

    private var time = 0
    private var isRunning = false
    private var timerTask: Timer? = null
    private var index :Int = 1
    private var i:Int = 0

    private var speed = 0.0
    private var sumTime: Int = 0
    private var sumKcal: Int = 0

    private val retrofit = Retrofit.Builder().baseUrl("http://parkbomin.iptime.org:18000/")
        .addConverterFactory(GsonConverterFactory.create()).build()
    private val service = retrofit.create(LocationService::class.java)
    private val serviceStatus = retrofit.create(LocationServiceStatus::class.java)


    private val tvSpeed: TextView by lazy { findViewById(R.id.tvSpeed) }
    private val tvRidingTime: TextView by lazy { findViewById(R.id.tvRidingTime) }
    private val tvAverage: TextView by lazy { findViewById(R.id.tvAverage) }
    private val tvDistance: TextView by lazy { findViewById(R.id.tvDistance) }
    private val btnStartRiding: FloatingActionButton by lazy { findViewById(R.id.btnStartRiding) }
    private val btnStopRiding: FloatingActionButton by lazy { findViewById(R.id.btnStopRiding) }
    private val btnSaveAndReset: FloatingActionButton by lazy {findViewById(R.id.btnSaveAndReset)}

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient // 위칫값 사용
    private lateinit var locationCallback: LocationCallback // 위칫값 요청에 대한 갱신 정보를 받아옴



    private val polyLineOptions= PolylineOptions().width(5f).color(Color.RED)

    //오늘 날짜
    private var Formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riding)

        mLocationRequest = LocationRequest()


        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }

        btnStopRiding.isEnabled = false
        btnSaveAndReset.isEnabled = false
        if (checkPermissionForLocation(this))
        {
            btnStartRiding.setOnClickListener {


                startLocationUpdates()
                start()
                startProcess()
                btnStartRiding.isEnabled = false
                btnStopRiding.isEnabled = true
                btnSaveAndReset.isEnabled = false

                val start: String = "start"
                val apikey = MyApplication.prefs.getString("token", "")
                // 실제 반복하는 코드를 여기에 적는다
                Log.d("token", apikey)
                serviceStatus.locationStatusPost(apikey, LocationDataStatus(start))
                    .enqueue(object : Callback<LocationDataStatus> {
                        override fun onResponse(call: Call<LocationDataStatus>, response: Response<LocationDataStatus>) {
                            if (response.isSuccessful) {
                                if (response.code() == 200) {

                                    var result: LocationDataStatus? = response.body()
                                    Log.d("YMC", "onResponse 성공: " + result?.toString());

                                }
                            }
                        }

                        override fun onFailure(call: Call<LocationDataStatus>, t: Throwable) {
                            Log.e("RETRO_ERR", "Error")
                        }
                    })

            }

            btnStopRiding.setOnClickListener {
                stoplocationUpdates()
                pause()
                btnStartRiding.isEnabled = true
                btnSaveAndReset.isEnabled = true
                btnStopRiding.isEnabled = false
            }

            //room db
            val db = Room.databaseBuilder(
                this, RidingDatabase::class.java,
                "riding-db"
            ).allowMainThreadQueries().build()

            val Ddb = Room.databaseBuilder(
                this, DistanceDatabase::class.java,
                "distance-db"
            ).allowMainThreadQueries().build()

            //오늘 날짜
            var calendar: org.threeten.bp.LocalDate = org.threeten.bp.LocalDate.now()
            var riding_date: String = Formatter.format(calendar)

            btnSaveAndReset.setOnClickListener {
                //db에 넣기
                db.RidingDao().deleteRecord(riding_date)
                db.RidingDao().insert(RidingRecord(riding_date, riding_time(), riding_distance(), riding_speed(), riding_calorie()))

                //거리 누적
                Ddb.DistanceDao().insert(Distance(Ddb.DistanceDao().getDistance()+Integer.parseInt(removeLastChar(riding_distance()))))

                Log.d("RidingRecord", "총 거리 : " + Ddb.DistanceDao().getDistance())
                reset()
                lapTime()
                mMap.clear()
                btnStartRiding.isEnabled = false
                btnSaveAndReset.isEnabled = false
                btnStopRiding.isEnabled = false

                val end: String = "end"
                val apikey = MyApplication.prefs.getString("token", "")
                // 실제 반복하는 코드를 여기에 적는다
                Log.d("token", apikey)
                serviceStatus.locationStatusPost(apikey,LocationDataStatus(end))
                    .enqueue(object : Callback<LocationDataStatus> {
                        override fun onResponse(call: Call<LocationDataStatus>, response: Response<LocationDataStatus>) {
                            if (response.isSuccessful) {
                                if (response.code() == 200) {

                                    var result: LocationDataStatus? = response.body()
                                    Log.d("YMC", "onResponse 성공: " + result?.toString());

                                }
                            }
                        }

                        override fun onFailure(call: Call<LocationDataStatus>, t: Throwable) {
                            Log.e("RETRO_ERR", "Error")
                        }
                    })

               // Log.d("RidingRecord","달린시간 : " + db.RidingDao().getTime(riding_date))
                Toast.makeText(this@RidingActivity, "기록에 영상이 저장되었습니다. 라이딩을 종료 후 다시 이용해주세요", Toast.LENGTH_LONG).show()
            }
        }



    }




    private fun buildAlertMessageNoGps() {

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                startActivityForResult(
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    , 11)
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.cancel()
                finish()
            }
        val alert: AlertDialog = builder.create()
        alert.show()


    }


    protected fun startLocationUpdates() {

        // Create the location request to start receiving updates

        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest!!.setInterval(INTERVAL)
        mLocationRequest!!.setFastestInterval(FASTEST_INTERVAL)

        // Create LocationSettingsRequest object using location request
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        val locationSettingsRequest = builder.build()

        val settingsClient = LocationServices.getSettingsClient(this)
        settingsClient.checkLocationSettings(locationSettingsRequest)

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        mFusedLocationProviderClient!!.requestLocationUpdates(
            mLocationRequest!!, mLocationCallback,
            Looper.myLooper())
    }


    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            // do work here
            locationResult.lastLocation
            locationResult.lastLocation?.let { onLocationChanged(it) }
        }
    }


    @OptIn(ExperimentalTime::class)
    override fun onLocationChanged(location: Location) {
        val sdf = SimpleDateFormat("hh:mm:ss a")
        var deltaTime = 0.0
//        var getSpeed: String = String.format("%.3f", location.speed)
//
//        tvSpeed.setText(getSpeed)

        var speedTime = time
        var i = 0
        var j = 0
        if(time == 60){
            i++
            speedTime = i*60
        }else if (i == 60){
            j++
            speedTime = j*3600
        }



        if (mLastLocation != null){
            deltaTime = (location.time - mLastLocation!!.time) / 1000.0
            speed = mLastLocation!!.speed.toDouble()

            mLastLocation!!.distanceTo(location)
            sumTime = sumTime.plus(mLastLocation!!.distanceTo(location)).toInt()
//            sumTime = sumTime.plus(deltaTime*speed).toInt()



            tvDistance.text = sumTime.toString() + "m"

//            if (sumTime <= 60){
//                tvDistance.setText((sumTime*0.3).toInt().toString() + "m")
//            }else if (sumTime<=3600){
//                tvDistance.setText((sumTime.div(60)).toInt().toString() + (sumTime.rem(
//                    60)*0.3).toInt().toString()+ "m")
////                tvAverage.setText()
//            }
//            else{
//                tvDistance.setText((sumTime.div((60*60))).toInt().toString() + (sumTime.div(60)).toInt().toString() + (sumTime.rem(
//                    60)*0.3).toInt().toString()+ "m")
////                tvAverage.setText()
//            }


            tvSpeed.text = String.format("%.1f km", speed)

            sumKcal = sumKcal.plus(sumTime)
            var Kcal = 2.3*70.0*( sumKcal / 500.0);
            tvAverage.setText((Kcal/100).toInt().toString() + " kcal");








            val data : LoacationData = LoacationData(mLastLocation!!.latitude.toFloat(), mLastLocation!!.longitude.toFloat())
            val apikey = MyApplication.prefs.getString("token", "")
            // 실제 반복하는 코드를 여기에 적는다
            Log.d("token", apikey)
            service.locationPost(apikey,data)
                .enqueue(object : Callback<LoacationData> {
                    override fun onResponse(call: Call<LoacationData>, response: Response<LoacationData>) {
                        if (response.isSuccessful) {
                            if (response.code() == 200) {

                                var result: LoacationData? = response.body()
                                Log.d("YMC", "onResponse 성공: " + result?.toString());

                            }
                        }
                    }

                    override fun onFailure(call: Call<LoacationData>, t: Throwable) {
                        Log.e("RETRO_ERR", "Error")
                    }
                })


        }
        mLastLocation = location






    }

    private fun stoplocationUpdates() {
        mFusedLocationProviderClient!!.removeLocationUpdates(mLocationCallback)
        fusedLocationClient.removeLocationUpdates(locationCallback)

    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
                btnStartRiding.isEnabled = false
                btnStopRiding.isEnabled = true
            } else {
                Toast.makeText(this@RidingActivity, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }



    fun checkPermissionForLocation(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                // Show the permission request
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSION_LOCATION)
                false
            }
        } else {
            true
        }
    }

    private fun start() {

        timerTask = kotlin.concurrent.timer(period = 10) {
            time++
            var sec = time / 100
            var secString = String.format("%02d", sec)
            runOnUiThread {

                if (sec == 60){
                    sec = 0
                    time = 0
                    i++
                }
                val min = i
                var minString = String.format("%02d", min)
                val hour = 0
                tvRidingTime.text = "0$hour : $minString : $secString"
            }
        }
    }

    private fun pause() {
        timerTask?.cancel();
    }

    private fun reset() {
        timerTask?.cancel()

        time = 0
        i = 0
        isRunning = false

        tvRidingTime.text = "00 : 00 : 00"
        tvSpeed.text = "0km"
        tvDistance.text = "0km"
        tvAverage.text = "0km"

        index = 1
    }

    private fun lapTime() {
        val lapTime = time
        val textView = TextView(this).apply {
            setTextSize(20f)
        }
        textView.text = "${lapTime / 100}.${lapTime % 100}"

        index++
    }


    fun startProcess() {
        // SupportMapFragment를 가져와서 지도가 준비되면 알림을 받습니다.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // 서울을 기준으로 위도,경도 지정
        val SEOUL = LatLng(37.56, 126.97)
        // MarkerOptions에 위치 정보를 넣을 수 있음
        val markerOptions = MarkerOptions()
        markerOptions.position(SEOUL)
        markerOptions.title("서울")
        markerOptions.snippet("한국의 수도")
        // 마커 추가
        mMap.addMarker(markerOptions)
        // 카메라 서울로 이동
        mMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL))
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        updateLocation()


    }

    // 위치 정보를 받아오는 역할
    @SuppressLint("MissingPermission") //requestLocationUpdates는 권한 처리가 필요한데 현재 코드에서는 확인 할 수 없음. 따라서 해당 코드를 체크하지 않아도 됨.
    fun updateLocation() {
        val locationRequest = LocationRequest.create()
        locationRequest.run {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 1000
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult?.let {
                    for(location in it.locations) {
                        Log.d("Location", "${location.latitude} , ${location.longitude}")
                        setLastLocation(location)
                    }
                }
            }
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    fun setLastLocation(lastLocation: Location) {
        val LATLNG = LatLng(lastLocation.latitude, lastLocation.longitude)
        val markerOptions = MarkerOptions()
            .position(LATLNG)
            .title("Here!")

        val cameraPosition = CameraPosition.Builder()
            .target(LATLNG)
            .zoom(19.0f)
            .build()
        mMap.clear()
        mMap.addMarker(markerOptions)
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))


        val latLng=LatLng(lastLocation.latitude,lastLocation.longitude)
        // 카메라를 이동한다.(이동할 위치,줌 수치)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,19.0f))
        // 마커를 추가한다.
        mMap.addMarker(MarkerOptions().position(latLng).title("Changed Location"))
        // polyLine에 좌표 추가
        polyLineOptions.add(latLng)
        // 선 그리기
        mMap.addPolyline(polyLineOptions)


    }

    //텍스트뷰 저장함수?
    fun riding_time(): String = tvRidingTime.text.toString()
    fun riding_distance(): String = tvDistance.text.toString()
    fun riding_speed(): String = tvSpeed.text.toString()
    fun riding_calorie(): String = tvAverage.text.toString()

    fun removeLastChar(str: String?): String? {
        return str?.replaceFirst(".$".toRegex(), "")
    }

}





