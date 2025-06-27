package com.example.caritas20.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caritas20.Data.Blancas
import com.example.caritas20.Data.ProductoColor
import com.example.caritas20.Data.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class PriceUiState(
    val colores: List<ProductoColor> = emptyList(),
    val blancas: List<Blancas> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class PriceViewModel(
    private val repository: Repository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(PriceUiState())
    val uiState: StateFlow<PriceUiState> = _uiState.asStateFlow()
    
    init {
        loadPrices()
    }
    
    private fun loadPrices() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                repository.getAllColores()
                    .combine(repository.getAllBlancas()) { colores, blancas ->
                        PriceUiState(
                            colores = colores,
                            blancas = blancas,
                            isLoading = false,
                            error = null
                        )
                    }
                    .collect { state ->
                        _uiState.value = state
                    }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al cargar precios"
                )
            }
        }
    }
    
    fun refreshPrices() {
        loadPrices()
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    fun updateBlancasPrice(blancas: Blancas) {
        viewModelScope.launch {
            try {
                repository.updateBlancas(blancas)
                refreshPrices()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Error al actualizar precio de Blancas"
                )
            }
        }
    }
    
    fun updateColorPrice(color: ProductoColor) {
        viewModelScope.launch {
            try {
                repository.updateColor(color)
                refreshPrices()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Error al actualizar precio de Color"
                )
            }
        }
    }
} 