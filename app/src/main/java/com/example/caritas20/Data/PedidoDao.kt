package com.example.caritas20.Data

import androidx.room.*

@Dao
interface PedidoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pedido: Pedido)

    @Update
    suspend fun update(pedido: Pedido)

    @Delete
    suspend fun delete(pedido: Pedido)

    @Query("SELECT * FROM Pedido")
    suspend fun getAll(): List<Pedido>

    @Query("SELECT * FROM Pedido WHERE id = :id")
    suspend fun getById(id: Int): Pedido?

    @Query("""
        SELECT 
            Pedido.id AS id,
            Pedido.id_producto AS id_producto,
            Pedido.cantidad AS cantidad,
            Pedido.id_cliente AS id_cliente,
            Cliente.id_cliente AS cliente_id_cliente,
            Cliente.nombre AS cliente_nombre,
            Cliente.panaderia AS cliente_panaderia
        FROM Pedido
        INNER JOIN Cliente ON Pedido.id_cliente = Cliente.id_cliente
    """)
    suspend fun getPedidosConCliente(): List<PedidoConCliente>
}

