package com.example.locations.domain.model

import androidx.room.*
import com.example.locations.domain.converter.Converter

@Entity(tableName = "locations")
data class Locations(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val nameLocation: String,
    @TypeConverters(Converter::class)
    @ColumnInfo val photosLocation: MutableList<String> = mutableListOf(),
    val visibleCheckBoxPhoto: Boolean = false
//@ColumnInfo val photosLocation: MutableList<ModelPhoto> = mutableListOf(),

)

//data class ModelPhoto(
//    val name: String,
//    val isVisible: Boolean,
//    val isChecking: Boolean
//)