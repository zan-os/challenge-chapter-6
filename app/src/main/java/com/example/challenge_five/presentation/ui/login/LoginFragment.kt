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
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.challenge_five.R
import com.example.challenge_five.common.Status
import com.example.challenge_five.databinding.FragmentLoginBinding
import com.example.challenge_five.presentation.ui.ViewModelFactory
import com.example.challenge_five.presentation.ui.login.utils.LoginViewModelFactory
import com.example.challenge_five.presentation.ui.login.utils.SaveLoginViewModel
import com.example.challenge_five.presentation.ui.login.utils.UserLoginPreferences

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var factory: ViewModelFactory
    private val viewModel: LoginViewModel by viewModels {
        factory
    }

    private lateinit var saveLoginViewModelFactory: LoginViewModelFactory
    private val saveLoginViewModel: SaveLoginViewModel by viewModels {
        saveLoginViewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userLoginPreferences = UserLoginPreferences(view.context)

        saveLoginViewModelFactory = LoginViewModelFactory.getInstance(userLoginPreferences)
        factory = ViewModelFactory.getInstance(requireActivity())

        binding.loginButton.setOnClickListener {
            login()
        }
        moveToRegister()
        checkLogin()
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
                        saveLoginViewModel.setLogin(it.data.email!!)
                        moveToMovieList(it.data.email!!)
                        Log.d("Email", it.data.email.toString())
                    } else {
                        Toast.makeText(requireContext(), "Password Salah", Toast.LENGTH_LONG)
                            .show()
                    }
                }
                Status.ERROR -> {
                    setLoading(false)
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    private fun moveToRegister() {
        binding.toRegisterButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun moveToMovieList(email: String) {
        val direction = LoginFragmentDirections.actionLoginFragmentToMovieListFragment(email)
        view?.findNavController()?.navigate(direction)
    }

    private fun setLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
    }

    private fun checkLogin() {
        saveLoginViewModel.email.observe(requireActivity()) { email ->
            if (email.isNotEmpty()) {
                val directions =
                    LoginFragmentDirections.actionLoginFragmentToMovieListFragment(email)
                view?.findNavController()?.navigate(directions)
            }
        }
    }
}