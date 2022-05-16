package com.example.challenge_five.presentation.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.challenge_five.R
import com.example.challenge_five.data.local.entity.UserEntity
import com.example.challenge_five.databinding.FragmentProfileBinding
import com.example.challenge_five.presentation.ui.ViewModelFactory
import com.example.challenge_five.utils.UserPreferences

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var user: UserEntity? = null

    private lateinit var factory: ViewModelFactory
    private val viewModel: ProfileViewModel by viewModels {
        factory
    }

    private lateinit var preferences: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        factory = ViewModelFactory.getInstance(requireActivity())
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = UserPreferences(view.context)

        getData()
        update()
        moveToMovieList()
        logout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getData() {
        user = ProfileFragmentArgs.fromBundle(arguments as Bundle).user

        user?.let {
            binding.apply {
                usernameEditText.setText(it.username)
                fullNameEditText.setText(it.name)
                dateOfBirthEditText.setText(it.dateOfBirth)
                addressEditText.setText(it.address)
            }
        }
    }

    private fun update() {
        binding.updateButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString().trim()
            val name = binding.fullNameEditText.text.toString().trim()
            val date = binding.dateOfBirthEditText.text.toString().trim()
            val address = binding.addressEditText.text.toString().trim()

            user?.let {
                it.username = username
                it.name = name
                it.dateOfBirth = date
                it.address = address

                when {
                    username.isEmpty() -> {
                        binding.usernameEditText.error = "Tidak boleh kosong"
                    }
                    name.isEmpty() -> {
                        binding.fullNameEditText.error = "Tidak boleh kosong"
                    }
                    date.isEmpty() -> {
                        binding.dateOfBirthEditText.error = "Tidak boleh kosong"
                    }
                    address.isEmpty() -> {
                        binding.addressEditText.error = "Tidak boleh kosong"
                    }
                    else -> {
                        when (viewModel.update(it)) {
                            0 -> {
                                Toast.makeText(
                                    requireContext(),
                                    "Berhasil diupdate",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            else -> {
                                Toast.makeText(
                                    requireContext(),
                                    "Gagal diupdate",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
            }

        }
    }

    private fun logout() {
        binding.logoutButton.setOnClickListener {
            preferences.saveLoginState(false)
            preferences.logout()
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }
    }

    private fun moveToMovieList() {
        binding.toolbarId.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_mainFragment)
        }
    }
}