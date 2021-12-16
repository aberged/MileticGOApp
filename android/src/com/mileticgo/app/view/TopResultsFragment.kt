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
import com.mileticgo.app.Repository
import com.mileticgo.app.TopScoreListItem
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

        showLoader()

        topResultViewModel.topScores.observe(viewLifecycleOwner, { topScores ->
            if (topScores.isNotEmpty()) {
                adapter.refreshList(topScores)
                hideLoader()
            } else {
                hideLoader()
                requireContext().oneButtonDialog(getString(R.string.info_dialog_title),
                    getString(R.string.empty_top_scores_list), getString(R.string.ok),
                    buttonCallback = {
                        findNavController().popBackStack()
                    })
            }
        })

        topResultViewModel.userResultFromList.observe(viewLifecycleOwner, { resultFromList ->
            setUserResultFromList(resultFromList)
        })

        topResultViewModel.userResultFromRepository.observe(viewLifecycleOwner, { resultFromRepository ->
            setUserResultFromRepository(resultFromRepository)
        })

        topResultViewModel.errorMessage.observe(viewLifecycleOwner, { message ->
            if (message != null && !message.isNullOrBlank()) {
                hideLoader()
                requireContext().oneButtonDialog(getString(R.string.info_dialog_title),
                    message, getString(R.string.ok),
                    buttonCallback = {
                        findNavController().popBackStack()
                    })
            }
        })

        return binding.root
    }

    private fun setUserResultFromList(lastTopResult: TopScoreListItem) {
        binding.clUserContainer.visibility = View.VISIBLE
        binding.tvUserName.text = lastTopResult.userName
        binding.tvPointsValue.text = lastTopResult.userPoints.toString()
        binding.tvPositionValue.text = lastTopResult.position.toString()
    }

    private fun setUserResultFromRepository(repositoryResult: TopResultsViewModel.RepositoryResult) {
        binding.clUserContainer.visibility = View.VISIBLE
        binding.tvUserName.text = repositoryResult.name
        binding.tvPointsValue.text = repositoryResult.score.toString()
        binding.tvPositionValue.text = repositoryResult.position.toString()
    }

    private fun showLoader() {
        this.activity?.runOnUiThread {
            binding.progressContainer.visibility = View.VISIBLE
        }
    }

    private fun hideLoader() {
        this.activity?.runOnUiThread {
            binding.progressContainer.visibility = View.GONE
        }
    }
}