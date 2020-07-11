package com.sontran.sample.core.utils.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.tbruyelle.rxpermissions2.RxPermissions
import java.lang.ref.WeakReference


interface OnRequestLocationListener {
    fun onSuccess(result: LocationResult)
    fun onFail()
    fun onComplete()
}

/**
 * This class helps Activity/Fragment to get the last known location of the device
 * only one time to save battery when the method is called
 * @see com.sontran.sample.core.utils.location.OneShotLocationRequestHelper#requestCurrentLocation()
 * It also asks customer for the location permission if needed.
 */
class OneShotLocationRequestHelper(
        private val weakFragment: WeakReference<Fragment>,
        private val listener: OnRequestLocationListener?,
        private var requestPriority: Int = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
) {

    companion object {
        private const val LOCATION_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION
    }

    private lateinit var locationCallback: LocationCallback

    @SuppressLint("CheckResult")
    fun requestCurrentLocation() {
        val currentFragment = weakFragment.get()
        currentFragment?.let { fragment ->
            val rxPermissions = RxPermissions(fragment)
            rxPermissions.request(LOCATION_PERMISSION)
                    .subscribe {
                        handleRequestPermissionsResult(fragment)
                    }
        }
    }

    private fun handleRequestPermissionsResult(fragment: Fragment) {
        if (fragment.activity != null) {
            val isGranted = PackageManager.PERMISSION_GRANTED ==
                    ActivityCompat.checkSelfPermission(
                            fragment.requireActivity(),
                            LOCATION_PERMISSION)

            // Location permission is granted
            if (isGranted) {
                val mLocationRequest = LocationRequest.create()
                mLocationRequest.priority = requestPriority
                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(fragment.requireActivity())
                locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        super.onLocationResult(locationResult)
                        // Just get once
                        fusedLocationClient.removeLocationUpdates(locationCallback)

                        listener?.onSuccess(locationResult)
                        listener?.onComplete()
                    }
                }

                fusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallback, null);

            } else {
                listener?.onFail()
                listener?.onComplete()
            }
        }
    }
}