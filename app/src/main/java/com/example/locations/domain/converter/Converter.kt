package com.example.locations.domain.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter

@ProvidedTypeConverter
object Converter {
    @TypeConverter
    @JvmStatic
    fun toList(strings: String?): List<String> {
        val list = mutableListOf<String>()
        val array = strings?.split(",")
        if (array != null) {
            for (s in array) {
                list.add(s)
            }
        }
        return list
    }

    @TypeConverter
    @JvmStatic
    fun toString(strings: List<String?>?): String {
        var result = ""
        strings?.forEachIndexed { index, element ->
            result += element
            if(index != (strings.size-1)){
                result += ","
            }
        }
        return result
    }

}