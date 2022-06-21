package com.example.locations.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.locations.presentation.interfaces.PhotoAdapterListener
import com.example.locations.presentation.interfaces.PhotoLongClick
import com.example.locations.R
import com.example.locations.domain.model.Locations
import com.example.locations.databinding.ItemPhotoBinding

class PhotoAdapter(
//    private val dataset: MutableList<String>,
    private val locations: Locations,
) : RecyclerView.Adapter<PhotoAdapter.PhotoHolder>() {
    private var photoAdapterListener: PhotoAdapterListener? = null
    fun setOnClickListener(onClickListener: PhotoAdapterListener) {
        this.photoAdapterListener = onClickListener
    }

    private var photoLongAdapterListener: PhotoLongClick? = null
    fun setOnLongClickListener(onLongClickListener: PhotoLongClick) {
        this.photoLongAdapterListener = onLongClickListener
    }

    inner class PhotoHolder(photo: View, context: Context) : RecyclerView.ViewHolder(photo) {
        private val binding = ItemPhotoBinding.bind(photo)
        private val myContext = context

        fun bind(needPhoto: String, isVisible: Boolean) {
            Glide
                .with(myContext)
                .load(needPhoto)
                .into(binding.ivItemPhoto)
            initPressListeners(needPhoto)
            initLongPressListeners()
            binding.cbChoicePhoto.isVisible = isVisible
//            if (binding.cbChoicePhoto.isChecked){}
        }

        private fun initPressListeners(photo: String) {
            binding.ivItemPhoto.setOnClickListener {
                photoAdapterListener?.onClickItem(photo)
            }
        }

        private fun initLongPressListeners() {
            binding.ivItemPhoto.setOnLongClickListener {
                photoLongAdapterListener?.onClickLongItem()
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return PhotoHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        val photo = locations.photosLocation[position]
        holder.bind(photo, locations.visibleCheckBoxPhoto)
    }

    override fun getItemCount(): Int = locations.photosLocation.size
}
