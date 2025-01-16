package utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class DistanceTracker(
    context: Context,
    private val onDistanceUpdated: (Double) -> Unit = {}
) {
    private val MIN_DISTANCE_THREADHOLD = 0.5f

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private var lastLocation: Location? = null
    private var totalDistance: Double = 0.0

    private val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        2500L
    ).build()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val newLocation = locationResult.lastLocation ?: return

            lastLocation?.let { previousLocation ->
                val distance = previousLocation.distanceTo(newLocation)
                if (distance > MIN_DISTANCE_THREADHOLD) {
                    totalDistance += distance
                    onDistanceUpdated(totalDistance)
                }
            }

            lastLocation = newLocation
        }
    }

    @SuppressLint("MissingPermission")
    fun startTracking() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    fun stopTracking() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}


