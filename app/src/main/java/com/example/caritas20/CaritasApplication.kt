package com.example.caritas20

import android.app.Application
import com.example.caritas20.Data.AppDatabase
import com.example.caritas20.Data.DataInitializer
import com.example.caritas20.Data.Repository

class CaritasApplication : Application() {
    
    // Database instance
    lateinit var database: AppDatabase
        private set
    
    // Repository instance
    lateinit var repository: Repository
        private set
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize database
        database = AppDatabase.getDatabase(this)
        
        // Initialize repository
        repository = Repository(
            pedidoDao = database.pedidoDao(),
            clienteDao = database.clienteDao(),
            colorDao = database.colorDao(),
            blancasDao = database.blancasDao()
        )
        
        // Initialize sample data
        val dataInitializer = DataInitializer(repository)
        dataInitializer.initializeSampleData()
    }
} 