package com.example.caritas20.Data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// Data class for grouped orders by client
data class ClienteConPedidos(
    val id_cliente: Int,
    val nombre: String,
    val panaderia: String,
    val totalPedidos: Int
)

@Dao
interface PedidoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pedido: Pedido)

    @Update
    suspend fun update(pedido: Pedido)

    @Delete
    suspend fun delete(pedido: Pedido)

    @Query("SELECT * FROM Pedido")
    fun getAll(): Flow<List<Pedido>>

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
    fun getAllPedidos(): Flow<List<PedidoConCliente>>
    
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
        WHERE Pedido.id_cliente = :idCliente
    """)
    fun getPedidosByCliente(idCliente: Int): Flow<List<PedidoConCliente>>
    
    @Query("""
        SELECT 
            Cliente.id_cliente AS id_cliente,
            Cliente.nombre AS nombre,
            Cliente.panaderia AS panaderia,
            COUNT(Pedido.id) AS totalPedidos
        FROM Cliente
        LEFT JOIN Pedido ON Cliente.id_cliente = Pedido.id_cliente
        GROUP BY Cliente.id_cliente, Cliente.nombre, Cliente.panaderia
        HAVING COUNT(Pedido.id) > 0
        ORDER BY Cliente.nombre
    """)
    fun getClientesConPedidos(): Flow<List<ClienteConPedidos>>
    
    @Query("DELETE FROM Pedido")
    suspend fun deleteAll()
}

