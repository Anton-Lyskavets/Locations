package com.example.locations.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.locations.R
import com.example.locations.data.header.NameSectionSharedPreferences
import com.example.locations.databinding.HeaderLocationBinding

class HeaderAdapter : RecyclerView.Adapter<HeaderAdapter.HeaderViewHolder>() {
    inner class HeaderViewHolder(view: View, context: Context) : RecyclerView.ViewHolder(view) {
        private val binding = HeaderLocationBinding.bind(view)
        private val nameHeader = NameSectionSharedPreferences(context).initSharedPref()
        fun bind() {
            binding.etHeader.apply {
                setText(nameHeader)
                addTextChangedListener {
                    val newNameHeader = it.toString()
                    NameSectionSharedPreferences(context).editSharedPref(newNameHeader)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.header_location, parent, false)
        return HeaderViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) = holder.bind()

    override fun getItemCount(): Int = 1
}