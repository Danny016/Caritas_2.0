package com.example.caritas20

import android.app.Application
import com.example.caritas20.Data.AppDatabase
import com.example.caritas20.Data.DataInitializer
import com.example.caritas20.Data.Repository

class CaritasApplication : Application() {
    
    // Instancia de la base de datos
    lateinit var database: AppDatabase
        private set
    
    // Instancia del repositorio
    lateinit var repository: Repository
        private set
    
    override fun onCreate() {
        super.onCreate()
        
        // Inicializar base de datos
        database = AppDatabase.getDatabase(this)
        
        // Inicializar repositorio
        repository = Repository(
            pedidoDao = database.pedidoDao(),
            clienteDao = database.clienteDao(),
            colorDao = database.colorDao(),
            blancasDao = database.blancasDao()
        )
        
        // Inicializar datos de muestra
        val dataInitializer = DataInitializer(repository)
        dataInitializer.initializeSampleData()
    }
} 