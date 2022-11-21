package com.example.mylocation.ui.photo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import com.example.mylocation.databinding.FragmentPhotoBinding
import com.example.mylocation.domian.model.ConstantGeneral.Companion.DATA
import com.example.mylocation.domian.model.ConstantGeneral.Companion.ID_ACT_CAMERA
import com.example.mylocation.domian.model.ConstantGeneral.Companion.REQUEST_PERMISSION_CAMERA
import com.example.mylocation.domian.model.ConstantGeneral.Companion.REQUEST_PERMISSON_WRITE_EXTERNAL_STORAGE
import com.example.mylocation.domian.model.ConstantGeneral.Companion.ZERO
import com.example.mylocation.ui.MainActivity
import com.example.mylocation.ui.component.Screen
import com.example.mylocation.ui.component.saveMediaToStorage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoFragment : Fragment() {

    var binding: FragmentPhotoBinding? = null
    var permissionCount = ZERO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            (activity as MainActivity)
                .changeScreen(Screen.HomeFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhotoBinding.inflate(LayoutInflater.from(context),null,false )

        binding?.btnPhotographyEvent?.setOnClickListener {
            if (permissionCount > ZERO) {
                issueCameraIntent()
            }
        }

        iniCam()
        return binding?.root
    }

    private fun issueCameraIntent() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, ID_ACT_CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ID_ACT_CAMERA && resultCode == Activity.RESULT_OK) {
            val photoBitmap: Bitmap? = data?.extras?.getParcelable(DATA)
            binding?.ivPhotography?.setImageBitmap(photoBitmap)
            context?.let { photoBitmap?.saveMediaToStorage(it) }
        }
    }

    private fun iniCam() {

        permissionCount = ZERO

        if (PermissionChecker.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PermissionChecker.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_PERMISSION_CAMERA)
        } else {
            permissionCount++
        }

        if (PermissionChecker.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_PERMISSON_WRITE_EXTERNAL_STORAGE
            )
        } else {
            permissionCount++
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = PhotoFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}