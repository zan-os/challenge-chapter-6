package com.example.challenge_five.presentation.ui.movie_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.challenge_five.R
import com.example.challenge_five.common.Resource
import com.example.challenge_five.data.local.entity.UserEntity
import com.example.challenge_five.databinding.FragmentMainBinding
import com.example.challenge_five.domain.model.Result
import com.example.challenge_five.presentation.adapter.MovieAdapter
import com.example.challenge_five.presentation.ui.ViewModelFactory
import com.example.challenge_five.utils.UserPreferences

class MovieListFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var factory: ViewModelFactory
    private val viewModel: MovieListViewModel by viewModels {
        factory
    }

    private lateinit var adapter: MovieAdapter
    private lateinit var preferences: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        factory = ViewModelFactory.getInstance(requireActivity())
        _binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = UserPreferences(view.context)
        getMovies()
        setUserData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getMovies() {
        viewModel.getMovieList().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Resource.Loading -> {
                        setLoading(true)
                    }
                    is Resource.Success -> {
                        setLoading(false)
                        val movies = result.data
                        Log.d("TAG", "success ${movies.toString()}")
                        viewModel.getUser(preferences.getKey("email"))
                            .observe(viewLifecycleOwner) { result ->

                                showList(movies, result.id)
                            }
                    }
                    is Resource.Error -> {
                        setLoading(false)
                    }
                }
            }
        }
    }

    private fun showList(movie: List<Result>?, userId: Int) {
        adapter = MovieAdapter(movie, userId)
        binding.movieRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())
        binding.movieRecyclerView.adapter = adapter
    }

    private fun setLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
    }

    private fun setUserData() {
        viewModel.getUser(preferences.getKey("email")).observe(viewLifecycleOwner) { result ->
            binding.usernameTextView.text =
                getString(R.string.get_username, result.username)
            moveToFavorite(result.id)
            moveToProfile(result)
        }
    }

    private fun moveToFavorite(id: Int) {
        binding.favoriteButton.setOnClickListener {
            val direction = MovieListFragmentDirections.actionMainFragmentToFavoriteFragment(id)
            findNavController().navigate(direction)
        }
    }

    private fun moveToProfile(user: UserEntity) {
        binding.profileButton.setOnClickListener {
            val direction = MovieListFragmentDirections.actionMainFragmentToProfileFragment(user)
            findNavController().navigate(direction)
        }
    }
}
