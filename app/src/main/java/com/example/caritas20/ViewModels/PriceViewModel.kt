package com.example.caritas20.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caritas20.Data.ProductoBlanca
import com.example.caritas20.Data.ProductoColor
import com.example.caritas20.Data.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class PriceUiState(
    val colores: List<ProductoColor> = emptyList(),
    val blancas: List<ProductoBlanca> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: String? = null
)

class PriceViewModel(
    private val repository: Repository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(PriceUiState())
    val uiState: StateFlow<PriceUiState> = _uiState.asStateFlow()
    
    init {
        loadPrecios()
    }
    
    private fun loadPrecios() {
        viewModelScope.launch {
            combine(
                repository.getAllColores(),
                repository.getAllBlancas()
            ) { colores, blancas ->
                PriceUiState(
                    colores = colores,
                    blancas = blancas,
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }
    
    fun updateColorPrice(color: ProductoColor) {
        viewModelScope.launch {
            try {
                repository.updateColor(color)
                _uiState.value = _uiState.value.copy(
                    success = "Precio de Color actualizado correctamente"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Error al actualizar precio de Color"
                )
            }
        }
    }
    
    fun updateBlancasPrice(blancas: ProductoBlanca) {
        viewModelScope.launch {
            try {
                repository.updateBlancas(blancas)
                _uiState.value = _uiState.value.copy(
                    success = "Precio de Blancas actualizado correctamente"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Error al actualizar precio de Blancas"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    fun clearSuccess() {
        _uiState.value = _uiState.value.copy(success = null)
    }
} 