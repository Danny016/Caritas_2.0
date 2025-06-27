package com.example.caritas20.Data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DataInitializer(private val repository: Repository) {
    
    fun initializeSampleData() {
        CoroutineScope(Dispatchers.IO).launch {
            val coloresExistentes = repository.getAllColoresOnce()
            val blancasExistentes = repository.getAllBlancasOnce()
            if (coloresExistentes.isEmpty() && blancasExistentes.isEmpty()) {
                // Precios base para ambos
                val precios = listOf(100.0, 120.0, 140.0, 160.0, 180.0, 200.0, 220.0, 240.0)
                // Insert sample colors
                val sampleColors = precios.mapIndexed { idx, precio ->
                    ProductoColor(idx, cantidad = if (idx < 5) 1000 else if (idx == 5) 100 else 1, precio = precio)
                }
                sampleColors.forEach { color ->
                    repository.insertColor(color)
                }
                // Insert sample blancas con los mismos precios
                val sampleBlancas = precios.mapIndexed { idx, precio ->
                    Blancas(idx, cantidad = if (idx < 5) 1000 else if (idx == 5) 100 else 1, precio = precio)
                }
                sampleBlancas.forEach { blanca ->
                    repository.insertBlancas(blanca)
                }
                
                // Insert sample client
                val sampleCliente = Cliente(
                    nombre = "Cliente Ejemplo",
                    panaderia = "Panadería Central"
                )
                repository.insertCliente(sampleCliente)
            }
        }
    }
} 