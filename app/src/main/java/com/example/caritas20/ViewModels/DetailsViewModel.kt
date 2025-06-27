package com.example.caritas20.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caritas20.Data.PedidoConCliente
import com.example.caritas20.Data.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DetailsUiState(
    val pedidosConCliente: List<PedidoConCliente> = emptyList(),
    val clienteName: String = "",
    val panaderia: String = "",
    val preciosBlancas: List<com.example.caritas20.Data.Blancas> = emptyList(),
    val preciosColores: List<com.example.caritas20.Data.ProductoColor> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val shouldNavigateToHome: Boolean = false
)

class DetailsViewModel(
    private val repository: Repository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()
    
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
    
    fun loadPedidosByCliente(idCliente: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, shouldNavigateToHome = false)
            try {
                repository.getPedidosByCliente(idCliente).collect { pedidos ->
                    if (pedidos.isNotEmpty()) {
                        val cliente = pedidos.first().cliente
                        _uiState.value = _uiState.value.copy(
                            pedidosConCliente = pedidos,
                            clienteName = cliente.nombre,
                            panaderia = cliente.panaderia,
                            isLoading = false,
                            error = null
                        )
                    } else {
                        // If no orders found, navigate to home
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            shouldNavigateToHome = true,
                            error = null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error desconocido"
                )
            }
        }
    }
    
    fun deletePedido(pedidoId: Int) {
        viewModelScope.launch {
            try {
                val pedido = repository.getPedidoById(pedidoId)
                if (pedido != null) {
                    repository.deletePedido(pedido)
                    // Reload the data after deletion
                    val currentPedidos = _uiState.value.pedidosConCliente
                    if (currentPedidos.isNotEmpty()) {
                        loadPedidosByCliente(currentPedidos.first().pedido.id_cliente)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Error al eliminar el pedido"
                )
            }
        }
    }
    
    fun deleteAllPedidos() {
        viewModelScope.launch {
            try {
                val currentPedidos = _uiState.value.pedidosConCliente
                if (currentPedidos.isNotEmpty()) {
                    val clienteId = currentPedidos.first().pedido.id_cliente
                    // Delete all orders
                    currentPedidos.forEach { pedidoConCliente ->
                        repository.deletePedido(pedidoConCliente.pedido)
                    }
                    // Navigate to home since all orders are deleted
                    _uiState.value = _uiState.value.copy(shouldNavigateToHome = true)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Error al eliminar los pedidos"
                )
            }
        }
    }
    
    fun getPrecioBlanca(numero: Int): Double {
        val blanca = _uiState.value.preciosBlancas.find { it.numero == numero }
        return if (blanca != null && blanca.cantidad > 0) {
            blanca.precio / blanca.cantidad // Precio por unidad
        } else {
            0.0
        }
    }
    
    fun getPrecioColor(numero: Int): Double {
        val color = _uiState.value.preciosColores.find { it.numero == numero }
        return if (color != null && color.cantidad > 0) {
            color.precio / color.cantidad // Precio por unidad
        } else {
            0.0
        }
    }
    
    fun calcularSubtotalBlancas(): Double {
        val blancas = _uiState.value.pedidosConCliente.filter { 
            it.pedido.id_producto.toIntOrNull() != null 
        }
        return blancas.sumOf { pedidoConCliente ->
            val numero = pedidoConCliente.pedido.id_producto.toInt()
            val precioPorUnidad = getPrecioBlanca(numero)
            pedidoConCliente.pedido.cantidad * precioPorUnidad
        }
    }
    
    fun calcularSubtotalColores(): Double {
        val colores = _uiState.value.pedidosConCliente.filter { 
            it.pedido.id_producto.toIntOrNull() == null 
        }
        return colores.sumOf { pedidoConCliente ->
            val numero = pedidoConCliente.pedido.id_producto.toInt()
            val precioPorUnidad = getPrecioColor(numero)
            pedidoConCliente.pedido.cantidad * precioPorUnidad
        }
    }
    
    fun calcularTotal(): Double {
        return calcularSubtotalBlancas() + calcularSubtotalColores()
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    fun clearSuccess() {
        _uiState.value = _uiState.value.copy(success = false)
    }
    
    fun clearNavigationState() {
        _uiState.value = _uiState.value.copy(shouldNavigateToHome = false)
    }
} 