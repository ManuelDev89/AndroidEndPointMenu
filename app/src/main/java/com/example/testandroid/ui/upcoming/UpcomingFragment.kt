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
import androidx.recyclerview.widget.RecyclerView
import com.example.testandroid.R
import com.example.testandroid.data.entities.MovieEntity
import com.example.testandroid.data.model.Movie
import com.example.testandroid.data.model.ResourceStatus
import com.example.testandroid.databinding.FragmentUpcomingBinding
import com.example.testandroid.ui.upcoming.UpcomingViewModel
import com.example.testandroid.utils.Pages
import com.example.testandroid.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpcomingFragment : Fragment(), UpcomingMovieItemAdapter.OnMovieClickListener {

    private var _binding: FragmentUpcomingBinding? = null

    private val binding get() = _binding!!

    private val viewModel: UpcomingViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var upcomingMovieItemAdapter: UpcomingMovieItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        binding.upMovies.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleIteamCount: Int = (binding.upMovies.layoutManager as LinearLayoutManager).childCount
                val pastVisibleIteam: Int = (binding.upMovies.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                val total = binding.upMovies.adapter!!.itemCount
                if ((visibleIteamCount + pastVisibleIteam) >= total){
                    Log.i("progresiosds","${visibleIteamCount}, ${pastVisibleIteam}, as ${total}")
                    Pages.upcomingPage+=1
                    upPagination()
                    Log.i("progress","${Pages.upcomingPage}")
                }
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.upMovies.layoutManager = LinearLayoutManager(context)

        Pages.upcomingPage+=1
        upPagination()
    }

    private fun upPagination() {
        viewModel.fetchUpcomingMovies.observe(viewLifecycleOwner, Observer {
            when (it.resourceStatus) {
                ResourceStatus.LOADING -> {
                    Log.e("fetchUpcomingMovies", "Loading")
                }
                ResourceStatus.SUCCESS  -> {
                    Log.e("fetchUpcomingMovies", "Success")
                    upcomingMovieItemAdapter = UpcomingMovieItemAdapter(it.data!!,this@UpcomingFragment)
                    binding.upMovies.adapter =upcomingMovieItemAdapter
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