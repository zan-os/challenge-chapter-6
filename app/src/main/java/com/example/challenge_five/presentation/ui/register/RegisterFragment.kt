package com.example.challenge_five.presentation.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.challenge_five.R
import com.example.challenge_five.common.Constants.MIN_PASSWORD_LENGTH
import com.example.challenge_five.databinding.FragmentRegisterBinding
import com.example.challenge_five.presentation.ui.ViewModelFactory

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        register()
        moveToLogin()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun register() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: RegisterViewModel by viewModels {
            factory
        }

        binding.apply {
            registerButton.setOnClickListener {
                val username = usernameEditText.text.toString().trim()
                val email = emailEditText.text.toString().trim()
                val password = passwordEditText.text.toString().trim()

                when {
                    username.isEmpty() -> {
                        binding.usernameEditText.error = "Tidak boleh kosong"
                    }
                    password.isEmpty() -> {
                        binding.passwordEditText.error = "Password minimal 6 Karakter"
                    }
                    password.length < MIN_PASSWORD_LENGTH -> {
                        binding.passwordEditText.error =
                            "Password harus lebih dari $MIN_PASSWORD_LENGTH karakter"
                        binding.passwordEditText.requestFocus()
                    }
                    email.isEmpty() -> {
                        binding.emailEditText.error = "Tidak boleh kosong"
                    }
                    else -> {
                        viewModel.register(username, email, password)
                            .observe(viewLifecycleOwner) { result ->
                                when (result) {
                                    0L -> Toast.makeText(
                                        context,
                                        "Something wrong",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    else -> {
                                        Toast.makeText(
                                            requireContext(),
                                            "Resgistrasi berhasil, silahkan login",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                                    }
                                }
                            }
                    }
                }
            }
        }
    }

    private fun moveToLogin() {
        binding.toLoginButton.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }
}