package com.example.caritas20.Data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Cliente::class,
            parentColumns = ["id_cliente"],
            childColumns = ["id_cliente"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["id_producto"]),
        Index(value = ["id_cliente"])
    ]
)
data class Pedido(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val id_producto: String, // Puede ser numero de ProductoColor o ProductoBlanca (guardado como String)
    val cantidad: Int,
    val id_cliente: Int
)
