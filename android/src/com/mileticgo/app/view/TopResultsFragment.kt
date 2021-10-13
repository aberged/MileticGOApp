package com.mileticgo.app.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mileticgo.app.R
import com.mileticgo.app.databinding.FragmentTopResultsBinding
import com.mileticgo.app.utils.oneButtonDialog
import com.mileticgo.app.view_model.TopResultsViewModel

class TopResultsFragment : Fragment() {

    private lateinit var binding: FragmentTopResultsBinding
    private lateinit var adapter : TopResultAdapter
    private val topResultViewModel by viewModels<TopResultsViewModel>()
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_top_results, container, false)

        layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        binding.rvTopResultsList.layoutManager = layoutManager

        adapter = TopResultAdapter()
        binding.rvTopResultsList.adapter = adapter

        topResultViewModel.topScores.observe(viewLifecycleOwner, { topScores ->
            if (topScores.isNotEmpty()) {
                adapter.refreshList(topScores)
            } else {
                requireContext().oneButtonDialog(getString(R.string.info_dialog_title),
                    getString(R.string.empty_top_scores_list), getString(R.string.ok),
                    buttonCallback = {
                        findNavController().popBackStack()
                    })
            }
        })

        binding.myToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }
}