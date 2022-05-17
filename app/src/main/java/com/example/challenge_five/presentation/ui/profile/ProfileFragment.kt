package com.example.challenge_five.presentation.ui.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.challenge_five.R
import com.example.challenge_five.data.local.entity.UserEntity
import com.example.challenge_five.databinding.FragmentProfileBinding
import com.example.challenge_five.presentation.ui.ViewModelFactory
import com.example.challenge_five.presentation.ui.login.utils.UserLoginPreferences

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var user: UserEntity? = null

    private lateinit var factory: ViewModelFactory
    private val viewModel: ProfileViewModel by viewModels {
        factory
    }

    private lateinit var userLoginPreferences: UserLoginPreferences


    private val gallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            loadImageUri(result)
        }

    private val camera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val bitmap = result.data?.extras?.get("data") as Bitmap
                loadImageBitmap(bitmap)
            }
        }

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
        userLoginPreferences = UserLoginPreferences(view.context)

        getData()
        update()
        moveToMovieList()
        addImage()
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

    private fun addImage() {
        binding.profileImage.setOnClickListener {
            checkPermission()
            Log.d("Image", "addImage clicked")
        }
    }

    private fun checkPermission() {
        Log.d("Image", "Checking Permission")
        if (permissionGranted(
                requireActivity(),
                Manifest.permission.CAMERA,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                REQUEST_CODE_PERMISSION
            )
        ) {
            chooseImage()
        } else {
            Log.d("Image", "Permission Denied")
            Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_LONG).show()
        }
    }

    private fun chooseImage() {
        Log.d("Image", "Showing Dialog")
        AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
            .setMessage("Choose an Image")
            .setPositiveButton("Gallery") { _, _ -> openGallery() }
            .setNegativeButton("Camera") { _, _ -> openCamera() }
            .show()
    }

    private fun openCamera() {
        Log.d("Image", "Opening Camera")
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { camera.launch(it) }
    }

    private fun openGallery() {
        Log.d("Image", "Opening Gallery")
        requireActivity().intent.type = "image/*"
        gallery.launch("image/*")
    }

    private fun permissionGranted(
        activity: Activity,
        permission: String,
        permissions: Array<String>,
        requestCode: Int
    ): Boolean {
        val permissionCheck = ActivityCompat.checkSelfPermission(activity, permission)
        return if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                Log.d("Image", "Permission Denied")
                permissionDenied()
            } else {
                Log.d("Image", "Permission Granted")
                ActivityCompat.requestPermissions(activity, permissions, requestCode)
            }
            false
        } else {
            true
        }

    }

    private fun permissionDenied() {
        AlertDialog.Builder(requireContext())
            .setTitle("Permission Denied")
            .setMessage("Permission is denied, please allow permissions from App Settings.")
            .setPositiveButton("App Settings") { _, _ -> openAppSettings() }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent()
        val uri = Uri.fromParts("package", requireActivity().packageName, null)

        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = uri

        startActivity(intent)
    }

    private fun loadImageUri(uri: Uri) {
        Glide.with(this)
            .load(uri)
            .apply(
                RequestOptions
                    .placeholderOf(R.drawable.ic_loading)
                    .error(R.drawable.ic_broken_image_black)
            )
            .into(binding.profileImage)
    }

    private fun loadImageBitmap(bitmap: Bitmap) {
        Glide.with(this)
            .load(bitmap)
            .apply(
                RequestOptions
                    .placeholderOf(R.drawable.ic_loading)
                    .error(R.drawable.ic_broken_image_black)
            )
            .into(binding.profileImage)
    }

    private fun moveToMovieList() {
        binding.toolbarId.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun logout() {
        binding.logoutButton.setOnClickListener {
            userLoginPreferences.logout()
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }
    }

    companion object {
        private const val REQUEST_CODE_PERMISSION = 100
    }
}