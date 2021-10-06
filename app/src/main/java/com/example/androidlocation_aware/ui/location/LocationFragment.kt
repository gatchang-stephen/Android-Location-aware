package com.example.androidlocation_aware.ui.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidlocation_aware.databinding.FragmentLocationBinding

class LocationFragment : Fragment() {

    private lateinit var locationViewModel: LocationViewModel
    private var _binding: FragmentLocationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        locationViewModel =
            ViewModelProvider(this).get(LocationViewModel::class.java)

        _binding = FragmentLocationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { bundle ->
            val passedArguments = LocationFragmentArgs.fromBundle(bundle)
            locationViewModel.getLocation(passedArguments.locationId)
                .observe(viewLifecycleOwner, Observer { wrapper ->
                    val location = wrapper.location
                    binding.title.text = location.title
                    binding.hours.text = location.hours
                    binding.description.text = location.description

                    val adapter = ActivitiesAdapter()
                    binding.listActivities.layoutManager = GridLayoutManager(this.context, 2)
                    binding.listActivities.adapter = adapter
                    adapter.setActivities(wrapper.activities.sortedBy { a -> a.title })
                })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}