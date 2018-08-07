package sk.styk.martin.location.service

import android.app.*
import android.content.Intent
import android.content.res.Configuration
import android.location.Location
import android.os.*
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.google.android.gms.location.*
import sk.styk.martin.location.R
import sk.styk.martin.location.db.LocationData
import sk.styk.martin.location.db.LocationDatabase
import sk.styk.martin.location.ui.main.MainActivity
import sk.styk.martin.location.util.Preferences
import sk.styk.martin.location.util.extensions.basicString
import sk.styk.martin.location.util.extensions.isRunningInForeground


/**
 * A bound and started service that is promoted to a foreground service when location updates have
 * been requested and all clients unbind.
 */
class LocationUpdatesService : Service() {

    private val binder = LocalBinder()

    private var changingConfiguration = false

    private lateinit var notificationManager: NotificationManager

    private lateinit var locationRequest: LocationRequest

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var serviceHandler: Handler

    private var location: Location? = null

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            onNewLocation(locationResult.lastLocation)
        }
    }

    override fun onCreate() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = createLocationRequest(Preferences.locationUpdateInterval(this))
        getLastLocation()

        serviceHandler = Handler(HandlerThread(TAG).apply { start() }.looper)
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(NotificationChannel(CHANNEL_ID,
                    getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT))
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.i(TAG, "Service started")
        val startedFromNotification = intent.getBooleanExtra(EXTRA_STARTED_FROM_NOTIFICATION, false)

        // We got here because the user decided to remove location updates from the notification.
        if (startedFromNotification) {
            removeLocationUpdates()
            stopSelf()
        }
        return START_NOT_STICKY
    }

    /**
     * Called when a client comes to the foreground and binds with this service.
     * The service should cease to be a foreground service when that happens.
     */
    override fun onBind(intent: Intent): IBinder {
        Log.i(TAG, "in onBind()")
        stopForeground(true)
        changingConfiguration = false
        return binder
    }

    /**
     * Called when a client returns to the foreground and binds with this service again.
     * The service should cease to be a foreground service when that happens.
     */
    override fun onRebind(intent: Intent) {
        Log.i(TAG, "in onRebind()")
        stopForeground(true)
        changingConfiguration = false
        super.onRebind(intent)
    }


    /**
     * Called when the last client unbinds from service. If this method is called due to a configuration change in Activity, we
     * do nothing. Otherwise, we make this service a foreground service.
     */
    override fun onUnbind(intent: Intent): Boolean {
        Log.i(TAG, "Last client unbound from service")

        if (!changingConfiguration && Preferences.requestingLocationUpdates(this)) {
            Log.i(TAG, "Starting foreground service")
            startForeground(NOTIFICATION_ID, notification)
        }
        return true
    }

    override fun onDestroy() {
        serviceHandler.removeCallbacksAndMessages(null)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        changingConfiguration = true
    }

    fun requestLocationUpdates() {
        Log.i(TAG, "Requesting location updates")
        Preferences.setRequestingLocationUpdates(this, true)
        startService(Intent(applicationContext, LocationUpdatesService::class.java))

        try {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
        } catch (unlikely: SecurityException) {
            Preferences.setRequestingLocationUpdates(this, false)
            Log.e(TAG, "Lost location permission. Could not request updates. $unlikely")
        }
    }

    fun removeLocationUpdates() {
        Log.i(TAG, "Removing location updates")

        try {
            fusedLocationClient.removeLocationUpdates(locationCallback)
            Preferences.setRequestingLocationUpdates(this, false)
            stopSelf()
        } catch (unlikely: SecurityException) {
            Preferences.setRequestingLocationUpdates(this, true)
            Log.e(TAG, "Lost location permission. Could not remove updates. $unlikely")
        }
    }

    fun updateLocationInterval(newInterval: Long) {
        locationRequest = createLocationRequest(newInterval)

        if (Preferences.requestingLocationUpdates(applicationContext)) {
            try {
                fusedLocationClient.removeLocationUpdates(locationCallback).addOnCompleteListener {
                    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
                }
            } catch (unlikely: SecurityException) {
                Preferences.setRequestingLocationUpdates(this, false)
                Log.e(TAG, "Lost location permission. Could not request updates. $unlikely")
            }
        }
    }

    private fun getLastLocation() = try {
        fusedLocationClient.lastLocation
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        location = task.result
                    } else {
                        Log.w(TAG, "Failed to get location.")
                    }
                }
    } catch (exception: SecurityException) {
        Log.e(TAG, "Lost location permission.$exception")
    }


    private fun onNewLocation(location: Location) {
        Log.i(TAG, "New location: $location")

        this.location = location

        Thread {
            LocationDatabase.getInstance(this).locationDataDao().insert(LocationData(location))
        }.start()

        if (isRunningInForeground()) {
            notificationManager.notify(NOTIFICATION_ID, notification)
        }
    }

    private fun createLocationRequest(updateInterval: Long) = LocationRequest().apply {
        interval = updateInterval
        fastestInterval = updateInterval / 2
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    inner class LocalBinder : Binder() {
        val service: LocationUpdatesService = this@LocationUpdatesService
    }

    private val notification: Notification by lazy {
        val intent = Intent(this, LocationUpdatesService::class.java)
                .apply { putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true) }

        val servicePendingIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT)
        val activityPendingIntent = PendingIntent.getActivity(this, 0,
                Intent(this, MainActivity::class.java), 0)

        val builder = NotificationCompat.Builder(this)
                .addAction(R.drawable.ic_launcher_background, getString(R.string.notification_open_app), activityPendingIntent)
                .addAction(R.drawable.ic_launcher_background, getString(R.string.notification_stop_tracking), servicePendingIntent)
                .setContentText(location?.basicString() ?: getString(R.string.location_unknown))
                .setContentTitle(getString(R.string.location_tracking_active))
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(location?.basicString() ?: getString(R.string.location_unknown))
                .setWhen(System.currentTimeMillis())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            builder.setChannelId(CHANNEL_ID)

        builder.build()
    }

    companion object {

        private val TAG = LocationUpdatesService::class.java.simpleName

        private const val CHANNEL_ID = "channel_01"

        private const val EXTRA_STARTED_FROM_NOTIFICATION = "sk.styk.martin.location.started_from_notification"

        private const val NOTIFICATION_ID = 12345678
    }
}