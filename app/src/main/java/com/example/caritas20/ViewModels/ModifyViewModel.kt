package com.example.caritas20.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caritas20.Data.Pedido
import com.example.caritas20.Data.PedidoConCliente
import com.example.caritas20.Data.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ModifyUiState(
    val pedidos: List<PedidoConCliente> = emptyList(),
    val selectedPedido: PedidoConCliente? = null,
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
        loadPedidos()
    }
    
    private fun loadPedidos() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                repository.getAllPedidos().collect { pedidos ->
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
                loadPedidos() // Refresh the list
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
                loadPedidos() // Refresh the list
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
        loadPedidos()
    }
} 