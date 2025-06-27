package com.example.caritas20.Data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Blancas(
    @PrimaryKey val id: String,  // Ejemplo: B1, B2...
    val cantidad: Int,
    val precio: Double
)
