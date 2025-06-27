package com.example.caritas20.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caritas20.Data.Blancas
import com.example.caritas20.Data.ProductoColor
import com.example.caritas20.Data.Pedido
import com.example.caritas20.Data.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AddUiState(
    val selectedTipo: String = "Blanca",
    val selectedNumber: Int = 0,
    val cantidad: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val currentClienteId: Int = 1 // Default to first client
)

class AddViewModel(
    private val repository: Repository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AddUiState())
    val uiState: StateFlow<AddUiState> = _uiState.asStateFlow()
    
    fun updateTipo(tipo: String) {
        _uiState.value = _uiState.value.copy(selectedTipo = tipo)
    }
    
    fun updateNumber(number: Int) {
        _uiState.value = _uiState.value.copy(selectedNumber = number)
    }
    
    fun updateCantidad(cantidad: String) {
        _uiState.value = _uiState.value.copy(cantidad = cantidad)
    }
    
    fun setClienteId(clienteId: Int) {
        _uiState.value = _uiState.value.copy(currentClienteId = clienteId)
    }
    
    fun addPedido() {
        val cantidad = _uiState.value.cantidad.toIntOrNull()
        if (cantidad == null || cantidad <= 0) {
            _uiState.value = _uiState.value.copy(
                error = "La cantidad debe ser un número válido mayor a 0"
            )
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val pedido = Pedido(
                    id_producto = _uiState.value.selectedNumber.toString(),
                    cantidad = cantidad,
                    id_cliente = _uiState.value.currentClienteId
                )
                
                repository.insertPedido(pedido)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    success = true,
                    cantidad = ""
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al agregar pedido"
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
    
    fun resetState() {
        _uiState.value = AddUiState()
    }
} 