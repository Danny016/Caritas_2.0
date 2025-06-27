package com.example.caritas20.Screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.caritas20.R
import com.example.caritas20.ViewModels.HomeViewModel
import com.example.caritas20.ViewModels.ViewModelFactory
import kotlin.system.exitProcess

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun HomeScreen(
    navController: NavController,
    viewModelFactory: ViewModelFactory
){
    val homeViewModel: HomeViewModel = viewModel(factory = viewModelFactory)
    val uiState by homeViewModel.uiState.collectAsState()
    
    Column (modifier = Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF66BFFF)
            ),
            title = { Text(text = "Inicio", ) },
            navigationIcon = {
                IconButton(onClick = {
                    exitProcess(0) // Cierra la aplicación
                }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                }
            },
            actions = {
                Image(
                    painter = painterResource(id= R.drawable.logo),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .padding(4.dp),
                )
            }
        )
        
        if (uiState.isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
                Text("Cargando...", modifier = Modifier.padding(16.dp))
            }
        } else {
            LazyColumn (
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(color = Color.White),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                if (uiState.pedidos.isEmpty()) {
                    item {
                        Text("No Hay Pedidos", fontSize = 32.sp, modifier = Modifier.padding(40.dp))
                    }
                } else {
                    item {
                        Text("Pedidos: ${uiState.pedidos.size}", fontSize = 24.sp, modifier = Modifier.padding(16.dp))
                    }
                }
                
                item {
                    Button(onClick = {
                        navController.navigate("Order")
                    },
                        modifier = Modifier.padding(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF66BFFF),
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp), // Bordes redondeados
                        border = BorderStroke(2.dp, Color.Black), // Borde negro de 2dp
                    ) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = null,
                            modifier = Modifier.padding(8.dp),
                            Color.White)
                        Text("Agregar Pedido")
                    }
                }
                item {
                    Button(onClick = {
                        navController.navigate("Price")
                    },
                        modifier = Modifier.padding(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF66BFFF),
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp), // Bordes redondeados
                        border = BorderStroke(2.dp, Color.Black), // Borde negro de 2dp
                    ) {
                        Icon(painter = painterResource(id = R.drawable.lprecios,),
                            contentDescription = null,
                            modifier = Modifier.padding(8.dp)
                                .size(24.dp),
                            Color.White)
                        Text("Tabla de Precios")
                    }
                }
                item {
                    Button(onClick = {
                        navController.navigate("Galery")
                    },
                        modifier = Modifier.padding(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF66BFFF),
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp), // Bordes redondeados
                        border = BorderStroke(2.dp, Color.Black), // Borde negro de 2dp
                    ) {
                        Icon(painter = painterResource(id = R.drawable.galery,),
                            contentDescription = null,
                            modifier = Modifier.padding(8.dp)
                                .size(24.dp),
                            Color.White)
                        Text("Galería")
                    }
                }
            }
        }
    }
}