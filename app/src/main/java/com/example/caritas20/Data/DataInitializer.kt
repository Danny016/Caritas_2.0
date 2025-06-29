package com.example.caritas20.Data

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DataInitializer(private val repository: Repository) {
    
    fun initializeSampleData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("DataInitializer", "Iniciando inicialización de datos...")
                
                val coloresExistentes = repository.getAllColoresOnce()
                val blancasExistentes = repository.getAllBlancasOnce()
                
                Log.d("DataInitializer", "Colores existentes: ${coloresExistentes.size}")
                Log.d("DataInitializer", "Blancas existentes: ${blancasExistentes.size}")
                
                if (coloresExistentes.isEmpty() && blancasExistentes.isEmpty()) {
                    Log.d("DataInitializer", "No hay datos existentes, insertando datos de muestra...")
                    
                    // Precios base para ambos
                    val precios = listOf(100.0, 120.0, 140.0, 160.0, 180.0, 200.0, 220.0, 240.0)
                    
                    // Insert sample colors
                    val sampleColors = precios.mapIndexed { idx, precio ->
                        ProductoColor("${idx}C", cantidad = if (idx < 5) 1000 else if (idx == 5) 100 else 1, precio = precio)
                    }
                    
                    sampleColors.forEach { color ->
                        try {
                            repository.insertColor(color)
                            Log.d("DataInitializer", "Color insertado: ${color.numero}")
                        } catch (e: Exception) {
                            Log.e("DataInitializer", "Error insertando color ${color.numero}: ${e.message}")
                        }
                    }
                    
                    // Insert sample blancas con los mismos precios
                    val sampleBlancas = precios.mapIndexed { idx, precio ->
                        ProductoBlanca("${idx}B", cantidad = if (idx < 5) 1000 else if (idx == 5) 100 else 1, precio = precio)
                    }
                    
                    sampleBlancas.forEach { blanca ->
                        try {
                            repository.insertBlancas(blanca)
                            Log.d("DataInitializer", "Blanca insertada: ${blanca.numero}")
                        } catch (e: Exception) {
                            Log.e("DataInitializer", "Error insertando blanca ${blanca.numero}: ${e.message}")
                        }
                    }
                    
                    // Insert sample client
                    val sampleCliente = Cliente(
                        nombre = "Cliente Ejemplo",
                        panaderia = "Panadería Central"
                    )
                    
                    try {
                        repository.insertCliente(sampleCliente)
                        Log.d("DataInitializer", "Cliente de ejemplo insertado")
                    } catch (e: Exception) {
                        Log.e("DataInitializer", "Error insertando cliente: ${e.message}")
                    }
                    
                    Log.d("DataInitializer", "Inicialización completada exitosamente")
                } else {
                    Log.d("DataInitializer", "Ya existen datos, saltando inicialización")
                }
            } catch (e: Exception) {
                Log.e("DataInitializer", "Error durante la inicialización: ${e.message}", e)
            }
        }
    }
} 