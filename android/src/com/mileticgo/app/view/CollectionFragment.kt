package com.mileticgo.app.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.mileticgo.app.R
import com.mileticgo.app.databinding.FragmentCollectionBinding
import com.mileticgo.app.utils.oneButtonDialog
import com.mileticgo.app.view_model.CollectionViewModel

class CollectionFragment : Fragment() {

    private lateinit var binding: FragmentCollectionBinding
    private lateinit var viewPagerAdapter: LocationViewPagerAdapter
    private val collectionViewModel by viewModels<CollectionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_collection, container, false)

        collectionViewModel.cityPins.observe(viewLifecycleOwner, { cityPins ->
            if (cityPins.isNotEmpty()) {
                collectionViewModel.createCategorySubLists(cityPins)
            } else {
                requireContext().oneButtonDialog(getString(R.string.info_dialog_title),
                    getString(R.string.empty_city_pins_list), getString(R.string.ok),
                    buttonCallback = {
                        findNavController().popBackStack()
                    })
            }
        })

        viewPagerAdapter = LocationViewPagerAdapter(requireActivity())
        binding.pager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.pager.adapter = viewPagerAdapter

        //connect tab layout (with dots) with view pager
        TabLayoutMediator(binding.tabDots, binding.pager) { tab, position ->

        }.attach()

        collectionViewModel.categoryList.observe(viewLifecycleOwner, { categoryList ->
            viewPagerAdapter.refresh(categoryList, collectionViewModel)
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.tabDots.post {
            binding.pager.currentItem = collectionViewModel.viewPagerPosition
        }
    }
}