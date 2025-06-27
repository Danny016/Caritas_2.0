package com.example.caritas20.Data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Blancas(
    @PrimaryKey val numero: Int,  // 0 al 7
    val cantidad: Int,
    val precio: Double
)
