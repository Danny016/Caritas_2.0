package com.example.caritas20

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.caritas20.Functions.Nav
import com.example.caritas20.ViewModels.ViewModelFactory
import com.example.caritas20.ui.theme.Caritas20Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Obtener repositorio desde la clase Application
        val repository = (application as CaritasApplication).repository
        val viewModelFactory = ViewModelFactory(repository)
        
        setContent {
            Caritas20Theme {
                Nav(viewModelFactory = viewModelFactory)
            }
        }
    }
}

