package com.example.caritas20.Data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(
    private val pedidoDao: PedidoDao,
    private val clienteDao: ClienteDao,
    private val colorDao: ColorDao,
    private val blancasDao: BlancasDao
) {
    
    // Pedido operations
    suspend fun insertPedido(pedido: Pedido) = withContext(Dispatchers.IO) {
        pedidoDao.insert(pedido)
    }
    
    suspend fun updatePedido(pedido: Pedido) = withContext(Dispatchers.IO) {
        pedidoDao.update(pedido)
    }
    
    suspend fun deletePedido(pedido: Pedido) = withContext(Dispatchers.IO) {
        pedidoDao.delete(pedido)
    }
    
    fun getAllPedidos(): Flow<List<PedidoConCliente>> = pedidoDao.getAllPedidos()
    
    fun getPedidosByCliente(idCliente: Int): Flow<List<PedidoConCliente>> = 
        pedidoDao.getPedidosByCliente(idCliente)
    
    suspend fun getPedidoById(id: Int): Pedido? = withContext(Dispatchers.IO) {
        pedidoDao.getById(id)
    }
    
    fun getClientesConPedidos(): Flow<List<com.example.caritas20.Data.ClienteConPedidos>> = 
        pedidoDao.getClientesConPedidos()
    
    // Cliente operations
    suspend fun insertCliente(cliente: Cliente) = withContext(Dispatchers.IO) {
        clienteDao.insert(cliente)
    }
    
    suspend fun insertClienteAndGetId(cliente: Cliente): Long = withContext(Dispatchers.IO) {
        clienteDao.insertAndGetId(cliente)
    }
    
    suspend fun updateCliente(cliente: Cliente) = withContext(Dispatchers.IO) {
        clienteDao.update(cliente)
    }
    
    suspend fun deleteCliente(cliente: Cliente) = withContext(Dispatchers.IO) {
        clienteDao.delete(cliente)
    }
    
    fun getAllClientes(): Flow<List<Cliente>> = clienteDao.getAllClientes()
    
    // Color operations
    suspend fun insertColor(color: ProductoColor) = withContext(Dispatchers.IO) {
        colorDao.insert(color)
    }
    
    suspend fun updateColor(color: ProductoColor) = withContext(Dispatchers.IO) {
        colorDao.update(color)
    }
    
    suspend fun deleteColor(color: ProductoColor) = withContext(Dispatchers.IO) {
        colorDao.delete(color)
    }
    
    fun getAllColores(): Flow<List<ProductoColor>> = colorDao.getAllColores()
    
    suspend fun getColorByNumero(numero: String): ProductoColor? = colorDao.getByNumero(numero)
    
    // Blancas operations
    suspend fun insertBlancas(blancas: ProductoBlanca) = withContext(Dispatchers.IO) {
        blancasDao.insert(blancas)
    }
    
    suspend fun updateBlancas(blancas: ProductoBlanca) = withContext(Dispatchers.IO) {
        blancasDao.update(blancas)
    }
    
    suspend fun deleteBlancas(blancas: ProductoBlanca) = withContext(Dispatchers.IO) {
        blancasDao.delete(blancas)
    }
    
    fun getAllBlancas(): Flow<List<ProductoBlanca>> = blancasDao.getAllBlancas()
    
    suspend fun getBlancasByNumero(numero: String): ProductoBlanca? = blancasDao.getByNumero(numero)
    
    suspend fun deleteAllBlancas() = withContext(Dispatchers.IO) {
        blancasDao.deleteAll()
    }

    suspend fun getAllColoresOnce(): List<ProductoColor> = withContext(Dispatchers.IO) {
        colorDao.getAllColoresOnce()
    }

    suspend fun getAllBlancasOnce(): List<ProductoBlanca> = withContext(Dispatchers.IO) {
        blancasDao.getAllBlancasOnce()
    }
    
    // Verification methods
    suspend fun productExists(idProducto: String): Boolean = withContext(Dispatchers.IO) {
        val color = getColorByNumero(idProducto)
        val blanca = getBlancasByNumero(idProducto)
        color != null || blanca != null
    }
    
    suspend fun getAllProductNumbers(): List<String> = withContext(Dispatchers.IO) {
        val colores = getAllColoresOnce()
        val blancas = getAllBlancasOnce()
        colores.map { it.numero } + blancas.map { it.numero }
    }
    
    // Cleanup methods
    suspend fun clearAllData() = withContext(Dispatchers.IO) {
        try {
            // Eliminar todos los pedidos primero (por las foreign keys)
            pedidoDao.deleteAll()
            // Eliminar todos los clientes
            clienteDao.deleteAll()
            // Eliminar todos los productos
            colorDao.deleteAll()
            blancasDao.deleteAll()
        } catch (e: Exception) {
            throw Exception("Error limpiando datos: ${e.message}")
        }
    }
    
    suspend fun reinitializeData() = withContext(Dispatchers.IO) {
        try {
            clearAllData()
            val dataInitializer = DataInitializer(this@Repository)
            dataInitializer.initializeSampleData()
        } catch (e: Exception) {
            throw Exception("Error reinicializando datos: ${e.message}")
        }
    }
} 