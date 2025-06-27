package com.example.caritas20.Data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Color(
    @PrimaryKey val id: String,  // Ejemplo: C1, C2...
    val cantidad: Int,
    val precio: Double
)
