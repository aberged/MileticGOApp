package com.mileticgo.app.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mileticgo.app.CityPin
import com.mileticgo.app.databinding.LocationPagerLayoutBinding

class LocationViewPagerAdapter(private val data : List<CityPin>) :
    RecyclerView.Adapter<LocationViewPagerAdapter.PagerViewHolder>() {

    private var locationsCategory : List<CityPin> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewPagerAdapter.PagerViewHolder {
        val binding = LocationPagerLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationViewPagerAdapter.PagerViewHolder, position: Int) {
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    inner class PagerViewHolder(val binding: LocationPagerLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: List<CityPin>) {

        }
    }
}