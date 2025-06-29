package com.example.caritas20.Data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "producto_color")
data class ProductoColor(
    @PrimaryKey val numero: String,  // Formato: 0C, 1C, 2C, etc.
    val cantidad: Int,
    val precio: Double
)
