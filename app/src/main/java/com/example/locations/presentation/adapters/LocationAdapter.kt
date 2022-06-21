package com.example.locations.presentation.adapters


import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.example.locations.presentation.interfaces.PhotoAdapterListener
import com.example.locations.presentation.interfaces.PhotoLongClick
import com.example.locations.domain.model.Locations
import com.example.locations.databinding.ItemLocationBinding


class LocationAdapter(
    private val listener: Listener,
    private val myEditable: MyEditable,
    private val photoAdapterListener: PhotoAdapterListener,
    private val photoLongClick: PhotoLongClick,
    private val onItemClicked: (
        Locations,
    ) -> Unit,
) :
    ListAdapter<Locations, LocationAdapter.ItemViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemLocationBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current, listener, myEditable, photoAdapterListener, photoLongClick)
    }

    class ItemViewHolder(private var binding: ItemLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            locations: Locations,
            listener: Listener,
            myEditable: MyEditable,
            photoAdapterListener: PhotoAdapterListener,
            photoLongClick: PhotoLongClick,
        ) {
            binding.apply {
//                val photoAdapter = PhotoAdapter(locations.photosLocation)
                val photoAdapter = PhotoAdapter(locations)
                photoAdapter.setOnClickListener(photoAdapterListener)
                photoAdapter.setOnLongClickListener(photoLongClick)
                rvPhotos.layoutManager = GridLayoutManager(itemView.context, 3)
                if (locations.photosLocation[0] == "") {
                    rvPhotos.adapter = null
                    etNameLocation.setText(locations.nameLocation)
                } else {
                    rvPhotos.adapter = photoAdapter
                    etNameLocation.setText(locations.nameLocation)
                }
                ibAddPhoto.setOnClickListener {
                    listener.onClick(locations)
                }
                etNameLocation.addTextChangedListener(
                    object : TextWatcher {
                        override fun beforeTextChanged(
                            p0: CharSequence?,
                            p1: Int,
                            p2: Int,
                            p3: Int,
                        ) {
                        }

                        override fun afterTextChanged(p0: Editable?) {}
                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                            etNameLocation.setOnFocusChangeListener { view, b ->
                                etNameLocation.setText(p0.toString())
                                if (!b && p0.toString() != locations.nameLocation) {
                                    try {
                                        myEditable.onEdit(locations, p0.toString())
                                    } catch (e: Exception) {
                                        e.stackTrace
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Locations>() {
            override fun areItemsTheSame(oldItem: Locations, newItem: Locations): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Locations, newItem: Locations): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    interface Listener {
        fun onClick(locations: Locations)
    }

    interface MyEditable {
        fun onEdit(locations: Locations, nameLocation: String)
    }
}
