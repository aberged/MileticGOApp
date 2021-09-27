package com.mileticgo.app.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mileticgo.app.R
import com.mileticgo.app.databinding.FragmentCollectionBinding
import com.mileticgo.app.view_model.CollectionViewModel

class CollectionFragment : Fragment() {

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var binding: FragmentCollectionBinding
    private lateinit var adapterCollection: CollectionAdapter

    private val collectionViewModel by viewModels<CollectionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_collection, container, false)

        layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        binding.rvDiamondList.layoutManager = layoutManager

        adapterCollection = CollectionAdapter {
            //list item click listener
            val bundle = Bundle()
            bundle.putString("details", it.name)
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_collectionFragment_to_placeDetailsFragment, bundle)
        }
        binding.rvDiamondList.adapter = adapterCollection
        adapterCollection.refreshList(collectionViewModel.getDummyItems())

        binding.myToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }
}