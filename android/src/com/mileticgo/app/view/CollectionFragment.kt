package com.mileticgo.app.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mileticgo.app.R
import com.mileticgo.app.databinding.FragmentCollectionBinding
import com.mileticgo.app.view_model.CollectionViewModel

class CollectionFragment : Fragment() {

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var binding : FragmentCollectionBinding
    private lateinit var adapter : CustomAdapter

    private val collectionViewModel by viewModels<CollectionViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_collection, container, false)

        layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        binding.rvDiamondList.layoutManager = layoutManager

        adapter = CustomAdapter()
        binding.rvDiamondList.adapter = adapter
        adapter.refreshList(collectionViewModel.getDummyItems())

        return binding.root
    }
}