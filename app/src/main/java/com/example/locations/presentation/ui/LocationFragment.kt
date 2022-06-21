package com.example.locations.presentation.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.locations.presentation.viewmodel.LocationsViewModel
import com.example.locations.presentation.viewmodel.LocationsViewModelFactory
import com.example.locations.R
import com.example.locations.domain.model.Locations
import com.example.locations.databinding.FragmentLocationBinding
import com.example.locations.presentation.adapters.HeaderAdapter
import com.example.locations.presentation.adapters.LocationAdapter
import com.example.locations.presentation.app.LocationsApplication
import com.example.locations.presentation.interfaces.PhotoAdapterListener
import com.example.locations.presentation.interfaces.PhotoLongClick


class LocationFragment : Fragment(),
    LocationAdapter.Listener,
    LocationAdapter.MyEditable,
    PhotoAdapterListener,
    PhotoLongClick {
    private val viewModel: LocationsViewModel by activityViewModels {
        LocationsViewModelFactory(
            (activity?.application as LocationsApplication).database
                .locationsDao()
        )
    }
    private lateinit var binding: FragmentLocationBinding
    private var launcher: ActivityResultLauncher<Intent>? = null
    private val headerAdapter = HeaderAdapter()
    private val locationAdapter = LocationAdapter(this, this, this, this) {}
    private lateinit var needLocations: Locations
    private val concatAdapter = ConcatAdapter(headerAdapter, locationAdapter)
    private lateinit var locations: List<Locations>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLocationBinding.inflate(inflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("Recycle")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val permissionStatus =
            context?.let { it1 ->
                ContextCompat.checkSelfPermission(
                    it1,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }
        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PackageManager.PERMISSION_GRANTED
            )
        }
        binding.bnmPanel.selectedItemId = R.id.mi_location
        binding.rvLocations.adapter = concatAdapter

        viewModel.allLocations.observe(this.viewLifecycleOwner) { items ->
            items.let {
                locations = it
                locationAdapter.submitList(it)
            }
        }
        launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    if (result.data?.clipData != null) {
                        val count = result.data?.clipData?.itemCount
                        var currentItem = 0
                        while (currentItem < count!!) {
                            val imageUri = result.data?.clipData?.getItemAt(currentItem)?.uri
                            insertNewPhoto(imageUri)
                            currentItem += 1;
                        }
                    } else if (result.data?.data != null) {
                        val imageUri = result.data?.data as Uri
                        insertNewPhoto(imageUri)
                    }
                }
            }

        binding.ivDelete.setOnClickListener {
//            val checkBox: CheckBox = requireView().findViewById(R.id.cb_choice_photo)
            binding.ivAddLocation.setImageResource(R.drawable.ic_add_location_gold)
            viewModel.clearDB()
            binding.ivDelete.visibility = View.INVISIBLE
            Toast.makeText(
                context,
                "Sorry, please README.",
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.rvLocations.layoutManager = LinearLayoutManager(context)
        binding.ivAddLocation.setOnClickListener {
            val permissionStatusNow =
                context?.let { it1 ->
                    ContextCompat.checkSelfPermission(
                        it1,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                }
            if (permissionStatusNow == PackageManager.PERMISSION_GRANTED) {
                addNewLocation()
            } else {
                Toast.makeText(
                    context,
                    "Необходимо разрешение на доступ к фото",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun addNewLocation() {
        viewModel.addNewLocation(
            "", mutableListOf()
        )
        locations.map { Locations(it.id, it.nameLocation, it.photosLocation, false) }
            .forEach { locationsNew -> viewModel.insertLocation(locationsNew) }
        binding.ivDelete.visibility = View.INVISIBLE
        binding.ivAddLocation.setImageResource(R.drawable.ic_add_location_gold)
    }

    private fun updatePhotosLocation(locations: Locations) {
        viewModel.updatePhotos(locations)
    }

    private fun updateNameLocation(locations: Locations) {
        viewModel.updateName(locations)
    }

    private fun insertNewPhoto(imageUri: Uri?) {
        Log.d("TAG", "$imageUri")
        val projectionImage = MediaStore.Images.Media.DATA
        val contentResolver = activity?.contentResolver
        val cursor =
            imageUri.let { it1 ->
                it1?.let {
                    contentResolver?.query(it,
                        null,
                        null,
                        null,
                        null)
                }
            }
        cursor?.moveToFirst()
        val columnIndex: Int =
            cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val columnPhotos = cursor.getColumnIndex(projectionImage)
        val hisPhoto = columnPhotos.let { cursor.getString(it) }
        val newPhotosLocation = needLocations.photosLocation
        if (newPhotosLocation[0] == "") {
            newPhotosLocation.removeAt(0)
        }
        newPhotosLocation.add(hisPhoto)
        updatePhotosLocation(Locations(
            needLocations.id,
            needLocations.nameLocation,
            newPhotosLocation))
        cursor.close()
    }

    override fun onClick(locations: Locations) {
        needLocations = locations
        val imageIntent = Intent(Intent.ACTION_PICK)
        imageIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true).type = "image/*"
        launcher?.launch(imageIntent)


    }

    override fun onEdit(locations: Locations, nameLocation: String) {
        needLocations = locations
        updateNameLocation(
            Locations(
                needLocations.id,
                nameLocation,
                needLocations.photosLocation
            )
        )
    }

    override fun onClickItem(photo: String) {
        val activity = requireActivity() as MainActivity
        val photoFragment = PhotoFragment.newInstance(photo)
        activity.supportFragmentManager
            .beginTransaction()
            .replace(R.id.fl_place_holder, photoFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onClickLongItem() {
        locations.map {
            Locations(it.id,
                it.nameLocation,
                it.photosLocation,
                !it.visibleCheckBoxPhoto)
        }
            .forEach { locationsNew -> viewModel.insertLocation(locationsNew) }
        binding.ivDelete.visibility = View.VISIBLE
        binding.ivAddLocation.setImageResource(R.drawable.ic_add_location_silver)
    }

    override fun onStop() {
        super.onStop()
        locations.map { Locations(it.id, it.nameLocation, it.photosLocation, false) }
            .forEach { locationsNew -> viewModel.insertLocation(locationsNew) }

    }

    companion object {
        @JvmStatic
        fun newInstance() = LocationFragment()
    }
}