package com.example.testandroid.ui.topRated

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testandroid.R
import com.example.testandroid.data.entities.MovieEntity
import com.example.testandroid.data.model.ResourceStatus
import com.example.testandroid.databinding.FragmentTopRatedMovieBinding
import com.example.testandroid.utils.Pages
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopRatedMovieFragment : Fragment(), TopRatedMovieItemAdapter.OnMovieClickListener {

    private var _binding: FragmentTopRatedMovieBinding? = null

    private val binding get() = _binding!!

    private val mViewModel: TopRatedMovieViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var topRatedMovieItemAdapter: TopRatedMovieItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTopRatedMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tpMovies.layoutManager = LinearLayoutManager(context)

        Pages.topRatedPage+=1
        mViewModel.fetchTopRatedMovies.observe(viewLifecycleOwner, Observer {
            when (it.resourceStatus) {
                ResourceStatus.LOADING -> {
                    Log.e("fetchTopRatedMovies", "Loading")
                }
                ResourceStatus.SUCCESS  -> {
                    Log.e("fetchTopRatedMovies", "Success")
                    topRatedMovieItemAdapter = TopRatedMovieItemAdapter(it.data!!, this@TopRatedMovieFragment)
                    binding.tpMovies.adapter = topRatedMovieItemAdapter
                }
                ResourceStatus.ERROR -> {
                    Log.e("fetchTopRatedMovies", "Failure: ${it.message} ")
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
        val action = TopRatedMovieFragmentDirections.actionTopRatedMovieFragmentToDetailFragment(movieEntity)
        findNavController().navigate(action)
    }


}