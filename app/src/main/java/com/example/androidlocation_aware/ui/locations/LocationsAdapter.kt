package com.example.androidlocation_aware.ui.locations

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidlocation_aware.R
import com.example.androidlocation_aware.data.Location

class LocationsAdapter(private val onClickListener: OnClickListener) :
    RecyclerView.Adapter<LocationsAdapter.LocationHolder>() {
    private var allLocations: List<Location> = ArrayList()
    private var currentLocation: android.location.Location? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationHolder {

        return LocationHolder(LayoutInflater.from(parent.context).inflate(R.layout.location_item, parent, false))
    }

    override fun getItemCount(): Int {
        return allLocations.size
    }

    fun setLocations(locations: List<Location>) {
        allLocations = locations
        hasObservers()
    }

    fun setCurrentLocation(location: android.location.Location) {
        currentLocation = location
        allLocations = allLocations.sortedBy { it.getDistanceInMiles(location) }
        hasObservers()
    }

    override fun onBindViewHolder(holder: LocationHolder, position: Int) {
        holder.bind(allLocations[position], onClickListener)
    }

    inner class LocationHolder(itemview: View) :
        RecyclerView.ViewHolder(itemview) {
        val title = itemview.findViewById<TextView>(R.id.title)

        val card = itemview.findViewById<CardView>(R.id.card)
        val distance = itemview.findViewById<TextView>(R.id.distance)
        val distanceIcon = itemview.findViewById<ImageView>(R.id.distanceIcon)

        fun bind(location: Location, clickListener: OnClickListener) {
            with(itemView) {
                title.text = location.title
                itemView.setOnClickListener { clickListener.onClick(location.locationId) }

                if (currentLocation != null) {
                    distanceIcon.visibility = View.VISIBLE

                    distance.visibility = View.VISIBLE
                    distance.text = context.getString(
                        R.string.distance_value,
                        location.getDistanceInMiles(currentLocation!!)
                    )
                }
            }
        }
    }

    interface OnClickListener {
        fun onClick(id: Int)
    }
}