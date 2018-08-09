package sk.styk.martin.location.ui.main

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import sk.styk.martin.location.R
import sk.styk.martin.location.service.LocationUpdatesService
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions
open class LocationServiceBindableActivity : AppCompatActivity(), LocationTrackingController {

    var boundService: LocationUpdatesService? = null

    private var serviceBound = false

    private val serviceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as LocationUpdatesService.LocalBinder
            boundService =  binder.service
            serviceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            boundService = null
            serviceBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        bindService(Intent(this, LocationUpdatesService::class.java), serviceConnection,
                Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        if (serviceBound) {
            unbindService(serviceConnection)
            serviceBound = false
        }
        super.onStop()
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    override fun startLocationTracking() {
        boundService?.requestLocationUpdates()
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    fun onLocationPermissionDenied() {
        Snackbar.make(findViewById(android.R.id.content), getString(R.string.location_permission_denied),
                Snackbar.LENGTH_SHORT)
                .show()
    }

    override fun stopLocationTracking() {
        boundService?.removeLocationUpdates()
    }

    override fun updateLocationTrackingInterval(newInterval: Long) {
        boundService?.updateLocationInterval(newInterval)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // NOTE: delegate the permission handling to generated function
        onRequestPermissionsResult(requestCode, grantResults)
    }
}