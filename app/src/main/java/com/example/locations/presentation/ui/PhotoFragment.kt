package com.example.locations.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import com.example.locations.databinding.FragmentPhotoBinding

class PhotoFragment : Fragment() {
    private lateinit var binding: FragmentPhotoBinding
    private var photo: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPhotoBinding.inflate(inflater)
        val arg = arguments
        if (arg != null) {
            photo = arg.getString("photo")
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        photo?.let { binding.ivNeedPhoto.setImageURI(it.toUri()) }
    }

    companion object {
        @JvmStatic
        fun newInstance(photo: String): PhotoFragment {
            val fragment = PhotoFragment()
            val args = Bundle()
            args.putString("photo", photo)
            fragment.arguments = args
            return fragment
        }
    }
}