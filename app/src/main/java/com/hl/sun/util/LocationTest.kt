package com.hl.sun.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import java.io.Serializable
import java.util.*

/**
 * Function:
 * Date:2023/2/22
 * Author: sunHL
 */
object LocationTest {
    private const val TAG = "定位获取"
    fun fetchLocation() {
        //获取当前定位
        var app = Utils.getApp()
        //添加用户权限申请判断
        if (ActivityCompat.checkSelfPermission(
                app,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                app,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //先申请权限成功后再调用getLocation定位
            Toast.makeText(app, "未开启定位权限", Toast.LENGTH_SHORT).show()
            return
        } else {
            getLocation(app)
        }
    }

    private var gpsLocationListener: LocationListener? = null
    private var netLocationListener: LocationListener? = null

    @SuppressLint("MissingPermission")
    private fun getLocation(app: Application?) {
        //获取位置管理器
        val locationManager =
            app?.getSystemService(Context.LOCATION_SERVICE) as? LocationManager

        val gpsEnable = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true
        if (gpsEnable) {

            Log.i(TAG, "获取定位-从requestLocationUpdates - GPS_PROVIDER")
            createGpsLocationListener(locationManager, app)

            try {
                gpsLocationListener?.let {
                    locationManager?.requestLocationUpdates(//java.lang.reflect.UndeclaredThrowableException
                        LocationManager.GPS_PROVIDER, 1000, 50F, it
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "$e")
            }
        }
        val netEnable = locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true
        if (netEnable) {
            Log.i(TAG, "获取定位-从requestLocationUpdates - NETWORK_PROVIDER")
            createNetLocationListener(locationManager, app)

            try {
                netLocationListener?.let {
                    locationManager?.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, 1000, 50F, it
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }

        //超时定位获取不到则返回
        /*  if (gpsEnable || netEnable) {
              CoroutineScope(Dispatchers.IO).launch {
                  delay(4000)
                  val stopAlready = removeAllLocationUpdates(locationManager)
                  if (!stopAlready) {
                      result.success(mapOf<String, Serializable>("address" to "error"))
                      Log.i(TAG, "获取定位超时")
                  }
              }
          }*/

        //没有可用的定位服务从lastKnownLocation获取位置
        if (!gpsEnable && !netEnable) {
            Log.i(TAG, "获取定位-从lastKnownLocation")
            val gpsLastLocation =
                locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (gpsLastLocation != null) {
                analysisLocationResult(
                    Utils.getApp(),
                    gpsLastLocation
                )
                Log.i(TAG, "获取定位-从lastKnownLocation GPS_PROVIDER 获取成功")
                return
            }

            Log.i(TAG, "获取定位-尝试从lastKnownLocation NETWORK_PROVIDER 获取")
            val netLastLocation =
                locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            analysisLocationResult(
                Utils.getApp(),
                netLastLocation
            )
        }
    }

    private fun createGpsLocationListener(
        locationManager: LocationManager?,
        app: Application?
    ) {
        if (gpsLocationListener != null) {
            locationManager?.removeUpdates(gpsLocationListener!!)
            gpsLocationListener = null
        }
        gpsLocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                Log.i(TAG, "GPS_PROVIDER onLocationChanged执行，location：${location}")

                analysisLocationResult(app, location)

                removeAllLocationUpdates(locationManager)
            }

            override fun onProviderDisabled(provider: String) {
                super.onProviderDisabled(provider)
            }

            override fun onProviderEnabled(provider: String) {
                super.onProviderEnabled(provider)
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                super.onStatusChanged(provider, status, extras)
            }
        }
    }

    private fun createNetLocationListener(
        locationManager: LocationManager?,
        app: Application?
    ) {
        if (netLocationListener != null) {
            locationManager?.removeUpdates(netLocationListener!!)
            netLocationListener = null
        }
        netLocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                Log.i(TAG, "NETWORK_PROVIDER onLocationChanged执行，location：${location}")
                analysisLocationResult(app, location)

                removeAllLocationUpdates(locationManager)
            }

            override fun onProviderDisabled(provider: String) {
                super.onProviderDisabled(provider)
            }

            override fun onProviderEnabled(provider: String) {
                super.onProviderEnabled(provider)
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                super.onStatusChanged(provider, status, extras)
            }
        }
    }

    //Location经纬度解析 并回调结果
    private fun analysisLocationResult(
        app: Application?,
        location: Location?
    ) {
        if (location == null) {
            printLocation(mapOf<String, Serializable>("address" to "error"))
            Log.i(TAG, "要解析的Location为空")
            return
        }

        try {
            val gc = Geocoder(app, Locale.getDefault()).getFromLocation(
                location.latitude,
                location.longitude,
                1
            )
            if (gc?.isNotEmpty() == true) {
                val isOverseas = isOverseasFromCode(gc[0].countryCode)
                val map = mapOf<String, Serializable>(
                    "address" to if (isOverseas) gc[0].countryName else gc[0].adminArea,
                    "isOverseas" to isOverseas
                )
                printLocation(map)
            } else {
                printLocation(mapOf<String, Serializable>("address" to "error"))
                Log.e(TAG, "Geocoder 解析结果为空")
            }
        } catch (e: Exception) {
            printLocation(mapOf<String, Serializable>("address" to "error"))
            Log.e(TAG, e.toString())
        }
    }

    private fun removeAllLocationUpdates(locationManager: LocationManager?): Boolean {
        var stopAlready = true
        if (gpsLocationListener != null) {
            locationManager?.removeUpdates(gpsLocationListener!!)
            gpsLocationListener = null
            stopAlready = false
        }
        if (netLocationListener != null) {
            locationManager?.removeUpdates(netLocationListener!!)
            netLocationListener = null
            stopAlready = false
        }
        String
        return stopAlready
    }

    private fun isOverseasFromCode(countryCode: String?): Boolean {
        return when (countryCode?.toUpperCase()) {
            "CN", "TW", "HK", "MO" -> {
                false
            }
            else -> {
                true
            }
        }
    }

    private fun printLocation(location: Map<String, Serializable>) {
        Log.i(TAG, "定位结果：$location")
    }
}