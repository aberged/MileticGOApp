package com.mileticgo.app.view

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mileticgo.app.CityPin
import com.mileticgo.app.R
import com.mileticgo.app.databinding.LocationPagerLayoutBinding

class LocationViewPagerAdapter(private val activity: Activity) :
    RecyclerView.Adapter<LocationViewPagerAdapter.PagerViewHolder>() {

    private var data : List<List<CityPin>> = emptyList()
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapterCollection: CollectionAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewPagerAdapter.PagerViewHolder {
        val binding = LocationPagerLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationViewPagerAdapter.PagerViewHolder, position: Int) {

        adapterCollection = CollectionAdapter {
            //list item click listener
            val bundle = Bundle()
            bundle.putSerializable("details", it)
            Navigation.findNavController(holder.binding.root)
                .navigate(R.id.action_collectionFragment_to_placeDetailsFragment, bundle)
        }

        val item = data[position]
        layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        holder.binding.rvDiamondList.layoutManager = layoutManager
        holder.binding.rvDiamondList.adapter = adapterCollection
        holder.binding.tvCategoryTitle.text = item[0].category
        adapterCollection.refreshList(item)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun refresh(categoryList: List<List<CityPin>>) {
        data = categoryList
        notifyDataSetChanged()

    }

    inner class PagerViewHolder(val binding: LocationPagerLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}