package com.example.caritas20.Data

import androidx.room.Embedded

data class PedidoConCliente(
    @Embedded val pedido: Pedido,
    @Embedded(prefix = "cliente_") val cliente: Cliente
)
