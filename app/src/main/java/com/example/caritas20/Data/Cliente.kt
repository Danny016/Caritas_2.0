package com.example.caritas20.Data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Cliente(
    @PrimaryKey(autoGenerate = true) val id_cliente: Int = 0,
    val nombre: String,
    val panaderia: String
)
