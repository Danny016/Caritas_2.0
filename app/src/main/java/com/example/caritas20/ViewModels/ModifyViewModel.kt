package com.example.caritas20.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caritas20.Data.Pedido
import com.example.caritas20.Data.PedidoConCliente
import com.example.caritas20.Data.ProductoBlanca
import com.example.caritas20.Data.ProductoColor
import com.example.caritas20.Data.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ModifyUiState(
    val pedidos: List<PedidoConCliente> = emptyList(),
    val selectedPedido: PedidoConCliente? = null,
    val preciosBlancas: List<ProductoBlanca> = emptyList(),
    val preciosColores: List<ProductoColor> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

class ModifyViewModel(
    private val repository: Repository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ModifyUiState())
    val uiState: StateFlow<ModifyUiState> = _uiState.asStateFlow()
    
    init {
        loadPrecios()
    }
    
    private fun loadPrecios() {
        viewModelScope.launch {
            try {
                repository.getAllBlancas().collect { blancas ->
                    _uiState.value = _uiState.value.copy(preciosBlancas = blancas)
                }
            } catch (e: Exception) {}
        }
        viewModelScope.launch {
            try {
                repository.getAllColores().collect { colores ->
                    _uiState.value = _uiState.value.copy(preciosColores = colores)
                }
            } catch (e: Exception) {}
        }
    }
    
    fun loadPedidosByCliente(clienteId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                repository.getPedidosByCliente(clienteId).collect { pedidos ->
                    _uiState.value = _uiState.value.copy(
                        pedidos = pedidos,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al cargar pedidos"
                )
            }
        }
    }
    
    fun selectPedido(pedido: PedidoConCliente) {
        _uiState.value = _uiState.value.copy(selectedPedido = pedido)
    }
    
    fun updatePedido(pedido: Pedido) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                repository.updatePedido(pedido)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    success = true
                )
                _uiState.value.selectedPedido?.let { selected ->
                    loadPedidosByCliente(selected.pedido.id_cliente)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al actualizar pedido"
                )
            }
        }
    }
    
    fun deletePedido(pedido: Pedido) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                repository.deletePedido(pedido)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    success = true
                )
                _uiState.value.selectedPedido?.let { selected ->
                    loadPedidosByCliente(selected.pedido.id_cliente)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al eliminar pedido"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    fun clearSuccess() {
        _uiState.value = _uiState.value.copy(success = false)
    }
    
    fun refreshPedidos() {
        _uiState.value.selectedPedido?.let { selected ->
            loadPedidosByCliente(selected.pedido.id_cliente)
        }
    }

    fun getPrecioBlanca(numero: String): Double {
        val blanca = _uiState.value.preciosBlancas.find { it.numero == numero }
        return if (blanca != null && blanca.cantidad > 0) {
            blanca.precio / blanca.cantidad
        } else {
            0.0
        }
    }

    fun getPrecioColor(numero: String): Double {
        val color = _uiState.value.preciosColores.find { it.numero == numero }
        return if (color != null && color.cantidad > 0) {
            color.precio / color.cantidad
        } else {
            0.0
        }
    }

    fun calcularSubtotalBlancas(): Double {
        val blancas = _uiState.value.pedidos.filter { it.pedido.id_producto.endsWith("B") }
        return blancas.sumOf { pedidoConCliente ->
            val precioPorUnidad = getPrecioBlanca(pedidoConCliente.pedido.id_producto)
            pedidoConCliente.pedido.cantidad * precioPorUnidad
        }
    }

    fun calcularSubtotalColores(): Double {
        val colores = _uiState.value.pedidos.filter { it.pedido.id_producto.endsWith("C") }
        return colores.sumOf { pedidoConCliente ->
            val precioPorUnidad = getPrecioColor(pedidoConCliente.pedido.id_producto)
            pedidoConCliente.pedido.cantidad * precioPorUnidad
        }
    }

    fun calcularTotal(): Double {
        return calcularSubtotalBlancas() + calcularSubtotalColores()
    }
} 