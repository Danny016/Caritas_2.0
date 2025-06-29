package com.example.caritas20.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caritas20.Data.Cliente
import com.example.caritas20.Data.Pedido
import com.example.caritas20.Data.PedidoConCliente
import com.example.caritas20.Data.ProductoBlanca
import com.example.caritas20.Data.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

// Data class for temporary pieces
data class TempPiece(
    val numero: String,
    val cantidad: Int,
    val tipo: String
)

data class OrderUiState(
    val clienteName: String = "",
    val panaderia: String = "",
    val pedidos: List<PedidoConCliente> = emptyList(),
    val tempBlancas: List<TempPiece> = emptyList(),
    val tempColores: List<TempPiece> = emptyList(),
    val preciosBlancas: List<ProductoBlanca> = emptyList(),
    val preciosColores: List<com.example.caritas20.Data.ProductoColor> = emptyList(),
    val showConfirmDialog: Boolean = false,
    val showAddPiecesDialog: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val createdCliente: Cliente? = null
)

class OrderViewModel(
    private val repository: Repository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(OrderUiState())
    val uiState: StateFlow<OrderUiState> = _uiState.asStateFlow()
    
    init {
        loadPrecios()
    }
    
    private fun loadPrecios() {
        viewModelScope.launch {
            try {
                repository.getAllBlancas().collect { blancas ->
                    _uiState.value = _uiState.value.copy(preciosBlancas = blancas)
                }
            } catch (e: Exception) {
                // Handle error silently for now
            }
        }
        
        viewModelScope.launch {
            try {
                repository.getAllColores().collect { colores ->
                    _uiState.value = _uiState.value.copy(preciosColores = colores)
                }
            } catch (e: Exception) {
                // Handle error silently for now
            }
        }
    }
    
    fun updateClienteName(name: String) {
        _uiState.value = _uiState.value.copy(clienteName = name)
    }
    
    fun updatePanaderia(panaderia: String) {
        _uiState.value = _uiState.value.copy(panaderia = panaderia)
    }
    
    fun showConfirmDialog() {
        _uiState.value = _uiState.value.copy(showConfirmDialog = true)
    }
    
    fun hideConfirmDialog() {
        _uiState.value = _uiState.value.copy(showConfirmDialog = false)
    }
    
    fun showAddPiecesDialog() {
        _uiState.value = _uiState.value.copy(showAddPiecesDialog = true)
    }
    
    fun hideAddPiecesDialog() {
        _uiState.value = _uiState.value.copy(showAddPiecesDialog = false)
    }
    
    fun confirmOrder() {
        if (_uiState.value.clienteName.isBlank() || _uiState.value.panaderia.isBlank()) {
            _uiState.value = _uiState.value.copy(
                error = "Por favor complete todos los campos del cliente"
            )
            return
        }
        
        if (_uiState.value.tempBlancas.isEmpty() && _uiState.value.tempColores.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                error = "Por favor agregue al menos una pieza al pedido"
            )
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                // Verificar que todos los productos existan
                val allProducts = repository.getAllProductNumbers()
                val tempProducts = _uiState.value.tempBlancas.map { it.numero } + 
                                 _uiState.value.tempColores.map { it.numero }
                
                // Log de diagnóstico
                android.util.Log.d("OrderViewModel", "Productos en BD: $allProducts")
                android.util.Log.d("OrderViewModel", "Productos a insertar: $tempProducts")
                
                val missingProducts = tempProducts.filter { !allProducts.contains(it) }
                if (missingProducts.isNotEmpty()) {
                    android.util.Log.e("OrderViewModel", "Productos faltantes: $missingProducts")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Los siguientes productos no existen en la base de datos: ${missingProducts.joinToString(", ")}"
                    )
                    return@launch
                }
                
                // Create and insert cliente
                val cliente = Cliente(
                    nombre = _uiState.value.clienteName,
                    panaderia = _uiState.value.panaderia
                )
                
                val clienteId = repository.insertClienteAndGetId(cliente).toInt()
                android.util.Log.d("OrderViewModel", "Cliente insertado con ID: $clienteId")
                
                // Insert blancas pieces
                _uiState.value.tempBlancas.forEach { piece ->
                    val pedido = Pedido(
                        id_producto = piece.numero,
                        cantidad = piece.cantidad,
                        id_cliente = clienteId
                    )
                    android.util.Log.d("OrderViewModel", "Insertando pedido blanca: ${pedido.id_producto}, cantidad: ${pedido.cantidad}")
                    repository.insertPedido(pedido)
                }
                
                // Insert color pieces
                _uiState.value.tempColores.forEach { piece ->
                    val pedido = Pedido(
                        id_producto = piece.numero,
                        cantidad = piece.cantidad,
                        id_cliente = clienteId
                    )
                    android.util.Log.d("OrderViewModel", "Insertando pedido color: ${pedido.id_producto}, cantidad: ${pedido.cantidad}")
                    repository.insertPedido(pedido)
                }
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    success = true,
                    showConfirmDialog = false,
                    createdCliente = cliente
                )
            } catch (e: Exception) {
                android.util.Log.e("OrderViewModel", "Error al confirmar pedido", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al confirmar pedido: ${e.message}"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    fun clearSuccess() {
        _uiState.value = _uiState.value.copy(success = false, createdCliente = null)
    }
    
    fun resetState() {
        _uiState.value = OrderUiState()
    }
    
    fun addPiece(tipo: String, numero: String, cantidad: String) {
        if (cantidad.isBlank()) {
            _uiState.value = _uiState.value.copy(
                error = "Por favor ingrese la cantidad"
            )
            return
        }
        
        val cantidadInt = cantidad.toIntOrNull()
        if (cantidadInt == null || cantidadInt <= 0) {
            _uiState.value = _uiState.value.copy(
                error = "La cantidad debe ser un número positivo"
            )
            return
        }
        
        val newPiece = TempPiece(numero, cantidadInt, tipo)
        
        if (tipo == "Blanca") {
            val updatedBlancas = _uiState.value.tempBlancas.toMutableList()
            updatedBlancas.add(newPiece)
            _uiState.value = _uiState.value.copy(
                tempBlancas = updatedBlancas,
                showAddPiecesDialog = false,
                error = null
            )
        } else {
            val updatedColores = _uiState.value.tempColores.toMutableList()
            updatedColores.add(newPiece)
            _uiState.value = _uiState.value.copy(
                tempColores = updatedColores,
                showAddPiecesDialog = false,
                error = null
            )
        }
    }
    
    fun getPrecioBlanca(numero: String): Double {
        val blanca = _uiState.value.preciosBlancas.find { it.numero == numero }
        return if (blanca != null && blanca.cantidad > 0) {
            blanca.precio / blanca.cantidad // Precio por unidad
        } else {
            0.0
        }
    }
    
    fun getPrecioColor(numero: String): Double {
        val color = _uiState.value.preciosColores.find { it.numero == numero }
        return if (color != null && color.cantidad > 0) {
            color.precio / color.cantidad // Precio por unidad
        } else {
            0.0
        }
    }
    
    fun calcularSubtotalBlancas(): Double {
        return _uiState.value.tempBlancas.sumOf { piece ->
            val precioPorUnidad = getPrecioBlanca(piece.numero)
            piece.cantidad * precioPorUnidad
        }
    }
    
    fun calcularSubtotalColores(): Double {
        return _uiState.value.tempColores.sumOf { piece ->
            val precioPorUnidad = getPrecioColor(piece.numero)
            piece.cantidad * precioPorUnidad
        }
    }
    
    fun calcularTotal(): Double {
        return calcularSubtotalBlancas() + calcularSubtotalColores()
    }
} 