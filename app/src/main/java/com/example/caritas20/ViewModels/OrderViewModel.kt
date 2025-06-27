package com.example.caritas20.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caritas20.Data.Cliente
import com.example.caritas20.Data.PedidoConCliente
import com.example.caritas20.Data.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class OrderUiState(
    val clienteName: String = "",
    val panaderia: String = "",
    val pedidos: List<PedidoConCliente> = emptyList(),
    val showConfirmDialog: Boolean = false,
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
    
    fun confirmOrder() {
        if (_uiState.value.clienteName.isBlank() || _uiState.value.panaderia.isBlank()) {
            _uiState.value = _uiState.value.copy(
                error = "Por favor complete todos los campos del cliente"
            )
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val cliente = Cliente(
                    nombre = _uiState.value.clienteName,
                    panaderia = _uiState.value.panaderia
                )
                
                repository.insertCliente(cliente)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    success = true,
                    showConfirmDialog = false,
                    createdCliente = cliente
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al confirmar pedido"
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
} 