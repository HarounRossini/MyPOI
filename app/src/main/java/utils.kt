package utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class Utils {

    companion object {

        private lateinit var fusedLocationClient: FusedLocationProviderClient
        var currentLocation: Location? = null
        @SuppressLint("MissingPermission")
        fun getLocation(context: Context, activity: Activity): Location? {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

            if(isLocationPermissionGranted(context, activity)){
                fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener { location : Location? ->
                        currentLocation = location
                    }
            }

            return currentLocation

        }

        // method to check Location permissions
        private fun isLocationPermissionGranted(context: Context, activity: Activity): Boolean {
            return if (ActivityCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    1
                )
                false
            } else {
                true
            }
        }

    }

}