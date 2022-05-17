package com.example.challenge_five.presentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.challenge_five.R
import com.example.challenge_five.databinding.FragmentSplashScreenBinding
import com.example.challenge_five.presentation.ui.login.utils.LoginViewModelFactory
import com.example.challenge_five.presentation.ui.login.utils.SaveLoginViewModel
import com.example.challenge_five.presentation.ui.login.utils.UserLoginPreferences

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment() {
    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var saveLoginViewModelFactory: LoginViewModelFactory
    private val saveLoginViewModel: SaveLoginViewModel by viewModels {
        saveLoginViewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSplashScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelInitialization()
        startSplashScreen()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun viewModelInitialization() {
        val userLoginPreferences = UserLoginPreferences(requireContext())
        saveLoginViewModelFactory = LoginViewModelFactory.getInstance(userLoginPreferences)
    }

    private fun startSplashScreen() {
        binding.logo.alpha = 0f
        binding.logo.animate().setDuration(2000).alpha(1f).withEndAction {
            moveToLogin()
        }
    }

    private fun moveToLogin() {
        findNavController().navigate(R.id.action_splashScreenFragment_to_loginFragment)
    }
}