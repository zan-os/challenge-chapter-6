package com.example.challenge_five.presentation.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.challenge_five.R
import com.example.challenge_five.common.Status
import com.example.challenge_five.databinding.FragmentLoginBinding
import com.example.challenge_five.presentation.ui.ViewModelFactory
import com.example.challenge_five.utils.UserPreferences

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var factory: ViewModelFactory
    private val viewModel: LoginViewModel by viewModels {
        factory
    }

    private var preferences: UserPreferences? = null

    private var isLogin: Boolean? = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        factory = ViewModelFactory.getInstance(requireActivity())
        _binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = UserPreferences(view.context)

        binding.loginButton.setOnClickListener {
            login()
        }
        moveToRegister()
        checkLoginState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun login() {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        viewModel.login(email, password)

        viewModel.result.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    setLoading(true)
                }
                Status.SUCCESS -> {
                    if (it.data != null) {
                        setLoading(false)
                        preferences?.getUser(it.data)
                        preferences?.saveLoginState(true)
                        moveToMovieList()
                    } else {
                        Toast.makeText(requireContext(), "Password Salah", Toast.LENGTH_LONG)
                            .show()
                    }
                }
                Status.ERROR -> {
                    setLoading(true)
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    private fun checkLoginState() {
        isLogin = preferences?.isLogin("login_state")

        Log.d("isLogin", isLogin.toString())

        if (isLogin == true) {
            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
        }
    }

    private fun moveToRegister() {
        binding.toRegisterButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun moveToMovieList() {
        findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
    }

    private fun setLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
    }
}