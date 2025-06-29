package com.example.caritas20.Data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "producto_blanca")
data class ProductoBlanca(
    @PrimaryKey val numero: String,  // Formato: 0B, 1B, 2B, etc.
    val cantidad: Int,
    val precio: Double
)
