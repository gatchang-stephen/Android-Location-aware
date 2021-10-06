package com.example.androidlocation_aware.ui.location

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidlocation_aware.data.Activity
import com.example.androidlocation_aware.databinding.LocationActivityItemBinding

class ActivitiesAdapter :
    RecyclerView.Adapter<ActivitiesAdapter.ActivityHolder>() {
    private var allActivities: List<Activity> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityHolder {
        val itemView = LocationActivityItemBinding.inflate(LayoutInflater.from(parent.context))
        return ActivityHolder(itemView)
    }

    override fun getItemCount(): Int {
        return allActivities.size
    }

    fun setActivities(activities: List<Activity>) {
        allActivities = activities
        hasObservers()
    }

    override fun onBindViewHolder(holder: ActivityHolder, position: Int) {
        holder.bind(allActivities[position])
    }

    inner class ActivityHolder(itemView: LocationActivityItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val title = itemView.title
        val icon = itemView.icon

        fun bind(activity: Activity) {
            with(itemView) {
                title.text = activity.title

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
}