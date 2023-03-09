package com.example.testandroid.ui.upcoming

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testandroid.R
import com.example.testandroid.data.entities.MovieEntity
import com.example.testandroid.data.model.Movie
import com.example.testandroid.data.model.ResourceStatus
import com.example.testandroid.databinding.FragmentUpcomingBinding
import com.example.testandroid.ui.popular.PopularMovieItemAdapter
import com.example.testandroid.ui.upcoming.UpcomingViewModel
import com.example.testandroid.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpcomingFragment : Fragment(), PopularMovieItemAdapter.OnMovieClickListener {

    private var _binding: FragmentUpcomingBinding? = null

    private val binding get() = _binding!!

    private val viewModel: UpcomingViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var upcomingMovieItemAdapter: PopularMovieItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvMovies.layoutManager = LinearLayoutManager(context)

        viewModel.fetchUpcomingMovies.observe(viewLifecycleOwner, Observer {
            when (it.resourceStatus) {
                ResourceStatus.LOADING -> {
                    Log.e("fetchUpcomingMovies", "Loading")
                }
                ResourceStatus.SUCCESS  -> {
                    Log.e("fetchUpcomingMovies", "Success")
                    upcomingMovieItemAdapter = PopularMovieItemAdapter(it.data!!, this@UpcomingFragment)
                    binding.rvMovies.adapter = upcomingMovieItemAdapter
                }
                ResourceStatus.ERROR -> {
                    Log.e("fetchUpcomingMovies", "Failure: ${it.message} ")
                    Toast.makeText(requireContext(), "Failure: ${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMovieClick(movieEntity: MovieEntity) {
        val action = UpcomingFragmentDirections.actionUpcomingFragmentToDetailFragment(movieEntity)
        findNavController().navigate(action)
    }
}