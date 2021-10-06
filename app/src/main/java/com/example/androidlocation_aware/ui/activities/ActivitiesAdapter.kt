package com.example.androidlocation_aware.ui.activities

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.androidlocation_aware.R
import com.example.androidlocation_aware.data.Activity

class ActivitiesAdapter(private val onClickListener: OnClickListener) :
    RecyclerView.Adapter<ActivitiesAdapter.ActivityHolder>() {
    private var allActivities: List<Activity> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityHolder {

        return ActivityHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.activity_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return allActivities.size
    }

    fun setActivities(activities: List<Activity>) {
        allActivities = activities
        hasObservers()
    }

    override fun onBindViewHolder(holder: ActivityHolder, position: Int) {
        holder.bind(allActivities[position], onClickListener)
    }

    inner class ActivityHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        private val card: CardView = itemView.findViewById(R.id.card)
        private val geofence: ImageView = itemView.findViewById(R.id.geofence)
        val icon: ImageView = itemView.findViewById(R.id.icon)

        fun bind(activity: Activity, clickListener: OnClickListener) {
            with(itemView) {
                title.text = activity.title

                card.setOnClickListener {
                    clickListener.onClick(activity.activityId, activity.title)
                }
                geofence.setOnClickListener { clickListener.onGeofenceClick(activity.activityId) }

                var color = R.color.colorGray
                if (activity.geofenceEnabled) {
                    color = R.color.colorAccent
                }

                ImageViewCompat
                    .setImageTintList(
                        geofence,
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                geofence.context,
                                color
                            )
                        )
                    )

                val iconUri = "drawable/ic_${activity.icon}_black_24dp"
                val imageResource: Int =
                    context.resources.getIdentifier(
                        iconUri, null, context.packageName
                    )
                icon.setImageResource(imageResource)
                icon.contentDescription = activity.title
            }
        }
    }

    interface OnClickListener {
        fun onClick(id: Int, title: String)
        fun onGeofenceClick(id: Int)
    }
}