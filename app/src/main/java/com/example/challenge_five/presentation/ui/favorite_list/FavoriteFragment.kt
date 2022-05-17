package com.example.challenge_five.presentation.ui.favorite_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.challenge_five.common.Resource
import com.example.challenge_five.databinding.FragmentFavoriteBinding
import com.example.challenge_five.domain.model.Favorite
import com.example.challenge_five.presentation.adapter.FavoriteAdapter
import com.example.challenge_five.presentation.ui.ViewModelFactory

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var factory: ViewModelFactory
    private val viewModel: FavoriteViewModel by viewModels {
        factory
    }

    private val args: FavoriteFragmentArgs by navArgs()
    private val userId: Int by lazy { args.id }

    private lateinit var adapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        factory = ViewModelFactory.getInstance(requireActivity())
        _binding = FragmentFavoriteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getFavoriteMovies()
        moveToMovieList()
        Log.d("Data", "id $userId")
    }

    private fun getFavoriteMovies() {
        viewModel.getFavorites(userId)
        viewModel.movies.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    val data = result.data
                    Log.d("Data", data.toString())
                    showList(data)
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun showList(favorite: List<Favorite>?) {
        adapter = FavoriteAdapter(favorite)
        binding.favoriteRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())
        binding.favoriteRecyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun moveToMovieList() {
        binding.toolbarId.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}