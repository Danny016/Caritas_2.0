package com.example.caritas20.ViewModels

import androidx.lifecycle.ViewModel
import com.example.caritas20.Data.Cliente
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SharedUiState(
    val currentCliente: Cliente? = null,
    val clienteId: Int = 0
)

class SharedViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(SharedUiState())
    val uiState: StateFlow<SharedUiState> = _uiState.asStateFlow()
    
    fun setCurrentCliente(cliente: Cliente) {
        _uiState.value = _uiState.value.copy(
            currentCliente = cliente,
            clienteId = cliente.id_cliente
        )
    }
    
    fun clearCurrentCliente() {
        _uiState.value = SharedUiState()
    }
} 