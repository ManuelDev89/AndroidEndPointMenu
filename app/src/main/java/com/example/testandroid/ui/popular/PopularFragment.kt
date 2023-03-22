package com.example.testandroid.ui.popular

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
import com.example.testandroid.data.model.ResourceStatus
import com.example.testandroid.databinding.FragmentPopularBinding
import com.example.testandroid.utils.Pages
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PopularFragment : Fragment(), PopularMovieItemAdapter.OnMovieClickListener {

    private var _binding: FragmentPopularBinding? = null

    private var movieList: MutableList<MovieEntity> = ArrayList()

    private val binding get() = _binding!!

    private val viewModel: PopularViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var popularMovieItemAdapter: PopularMovieItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPopularBinding.inflate(inflater, container, false)
        popularMovieItemAdapter = PopularMovieItemAdapter(movieList,this@PopularFragment)
        binding.rvMovies.adapter = popularMovieItemAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Pages.popularPage+=1
        binding.rvMovies.layoutManager = LinearLayoutManager(context)
        pagination()
        binding.rvMovies.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val VisibleIteam: Int = (binding.rvMovies.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val total = binding.rvMovies.adapter!!.itemCount
                if (VisibleIteam == total-1){
                    Log.i("progresiosds"," ${VisibleIteam}, as ${total}")
                    Pages.popularPage+=1
                    pagination()
                    Log.i("progress","${Pages.popularPage}")
                }
            }
        })
        Log.i("ola","${Pages.popularPage}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun onRefresh() {
    }

    override fun onMovieClick(movieEntity: MovieEntity) {
        val action = PopularFragmentDirections.actionHomeFragmentToDetailFragment(movieEntity)
        findNavController().navigate(action)
    }

    private fun pagination() {
        viewModel.fetchPopularMovies.observe(viewLifecycleOwner, Observer {
            when (it.resourceStatus) {
                ResourceStatus.LOADING -> {
                    Log.e("fetchPopularMovies", "Loading")
                }
                ResourceStatus.SUCCESS  -> {
                    Log.e("fetchPopularMovies", "Success")
                    popularMovieItemAdapter.addItems(it.data!!)
                    popularMovieItemAdapter.notifyDataSetChanged()
                }
                ResourceStatus.ERROR -> {
                    Log.e("fetchPopularMovies", "Failure: ${it.message} ")
                    Toast.makeText(requireContext(), "Failure: ${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }
}