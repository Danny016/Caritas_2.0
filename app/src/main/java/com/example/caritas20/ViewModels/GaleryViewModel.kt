package com.example.caritas20.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caritas20.Data.PedidoConCliente
import com.example.caritas20.Data.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class GaleryUiState(
    val pedidos: List<PedidoConCliente> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class GaleryViewModel(
    private val repository: Repository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(GaleryUiState())
    val uiState: StateFlow<GaleryUiState> = _uiState.asStateFlow()
    
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
    
    fun refreshPedidos() {
        loadPedidos()
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
} 