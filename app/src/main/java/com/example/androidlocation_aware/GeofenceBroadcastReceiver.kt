package com.example.androidlocation_aware

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.example.androidlocation_aware.data.OutdoorRoomDatabase
import com.example.androidlocation_aware.data.OutdoorRoomRepository
import com.example.androidlocation_aware.ui.location.LocationFragmentArgs
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent


class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val event = GeofencingEvent.fromIntent(intent)

        if (event.hasError()) {
            return
        }

        if (event.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            val geofence = event.triggeringGeofences[0]
            sendNotification(context, geofence.requestId.toInt())
        }
    }

    private fun sendNotification(context: Context, locationId: Int) {
        val outdoorDao = OutdoorRoomDatabase.getInstance(context).outdoorDao()
        val outdoorRepository = OutdoorRoomRepository(outdoorDao)
        val location = outdoorRepository.getLocationById(locationId)
        val message =
            "Visit ${location.title} to enjoy your favorite activities"
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "locations",
                "Nearby Locations",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val locationArgs =
            LocationFragmentArgs.fromBundle(bundle = Bundle(locationId)).toBundle()

        val intent = NavDeepLinkBuilder(context)
            .setGraph(R.navigation.mobile_navigation)
            .setDestination(R.id.navigation_location)
            .setArguments(locationArgs)
            .createPendingIntent()

        val notification = NotificationCompat.Builder(context, "locations")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Discover the outdoors near you now!")
            .setContentText(message)
            .setContentIntent(intent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}