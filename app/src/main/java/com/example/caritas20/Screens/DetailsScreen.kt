package com.example.caritas20.Screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.caritas20.Functions.ContentRow
import com.example.caritas20.R
import com.example.caritas20.ViewModels.DetailsViewModel
import com.example.caritas20.ViewModels.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    navController: NavController,
    viewModelFactory: ViewModelFactory,
    clienteId: Int
) {
    val detailsViewModel: DetailsViewModel = viewModel(factory = viewModelFactory)
    val uiState by detailsViewModel.uiState.collectAsState()
    
    // State for delete confirmation dialog
    var showDeleteDialog by remember { mutableStateOf(false) }
    var pedidoToDelete by remember { mutableStateOf<Int?>(null) }
    
    // Load data when screen is created
    LaunchedEffect(clienteId) {
        detailsViewModel.loadPedidosByCliente(clienteId)
    }
    
    // Handle navigation to home when all orders are deleted
    LaunchedEffect(uiState.shouldNavigateToHome) {
        if (uiState.shouldNavigateToHome) {
            detailsViewModel.clearNavigationState()
            navController.navigate("Home") {
                popUpTo("Home") { inclusive = true }
            }
        }
    }
    
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF66BFFF)
            ),
            title = { Text(text = "Detalles del Pedido") },
            navigationIcon = {
                IconButton(onClick = {
                    navController.navigateUp()
                }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                }
            },
            actions = {
                Image(
                    painter = painterResource(id = R.drawable.logo),
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(color = Color.White),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Text("Datos del Cliente:", fontSize = 24.sp, modifier = Modifier.padding(top = 16.dp))
                    
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F8FF))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Nombre: ${uiState.clienteName}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = "Panadería: ${uiState.panaderia}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    
                    // Show error if exists
                    if (uiState.error != null) {
                        Text(
                            text = uiState.error!!,
                            color = Color.Red,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    
                    Text("Pedido:", fontSize = 24.sp, modifier = Modifier.padding(top = 16.dp))
                }
                
                // Group orders by type (Blanca/Color)
                val blancas = uiState.pedidosConCliente.filter { it.pedido.id_producto.endsWith("B") }
                val colores = uiState.pedidosConCliente.filter { it.pedido.id_producto.endsWith("C") }
                
                if (uiState.pedidosConCliente.isNotEmpty()) {
                    // Tabla Blancas
                    if (blancas.isNotEmpty()) {
                        item {
                            Text("Pedidos Blancas:", fontSize = 18.sp, modifier = Modifier.padding(top = 8.dp))
                            BlancasOrderTable(
                                blancas = blancas,
                                detailsViewModel = detailsViewModel
                            )
                        }
                    }
                    
                    // Tabla Color
                    if (colores.isNotEmpty()) {
                        item {
                            Text("Pedidos Color:", fontSize = 18.sp, modifier = Modifier.padding(top = 8.dp))
                            ColoresOrderTable(
                                colores = colores,
                                detailsViewModel = detailsViewModel
                            )
                        }
                    }
                }
                
                // Total General
                if (uiState.pedidosConCliente.isNotEmpty()) {
                    item {
                        Card(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF66BFFF))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "TOTAL A PAGAR",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "$${String.format("%.2f", detailsViewModel.calcularTotal())}",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                    }
                }
                
                // Action buttons
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                // Navigate to order screen to add pieces with client data
                                if (uiState.pedidosConCliente.isNotEmpty()) {
                                    val clienteId = uiState.pedidosConCliente.first().pedido.id_cliente
                                    val clienteName = uiState.clienteName
                                    val panaderia = uiState.panaderia
                                    navController.navigate("Order/$clienteId/$clienteName/$panaderia")
                                }
                            },
                            modifier = Modifier.wrapContentSize(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF66BFFF),
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(2.dp, Color.Black)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = null,
                                modifier = Modifier.padding(8.dp).size(24.dp),
                                Color.White
                            )
                            Text("Agregar Piezas")
                        }
                        
                        Button(
                            onClick = {
                                // Navigate to modify screen with client ID
                                if (uiState.pedidosConCliente.isNotEmpty()) {
                                    val clienteId = uiState.pedidosConCliente.first().pedido.id_cliente
                                    navController.navigate("Modify/$clienteId")
                                }
                            },
                            modifier = Modifier.wrapContentSize(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF66BFFF),
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(2.dp, Color.Black)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = null,
                                modifier = Modifier.padding(8.dp).size(24.dp),
                                Color.White
                            )
                            Text("Modificar Pedido")
                        }
                        
                        Button(
                            onClick = {
                                // Show delete confirmation for all orders
                                if (uiState.pedidosConCliente.isNotEmpty()) {
                                    pedidoToDelete = -1 // Special value for delete all
                                    showDeleteDialog = true
                                }
                            },
                            modifier = Modifier.wrapContentSize(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFF6B6B),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(2.dp, Color.Black)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = null,
                                modifier = Modifier.padding(8.dp).size(24.dp),
                                Color.White
                            )
                            Text("Eliminar Pedido")
                        }
                    }
                }
            }
        }
        
        // Delete confirmation dialog
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = {
                    Text(
                        text = if (pedidoToDelete == -1) "Eliminar todos los pedidos" else "Eliminar pedido",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = if (pedidoToDelete == -1) 
                            "¿Estás seguro de que quieres eliminar todos los pedidos de ${uiState.clienteName}?"
                        else 
                            "¿Estás seguro de que quieres eliminar este pedido?",
                        fontSize = 16.sp
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (pedidoToDelete == -1) {
                                // Delete all orders for this client
                                detailsViewModel.deleteAllPedidos()
                            } else {
                                // Delete specific order
                                pedidoToDelete?.let { detailsViewModel.deletePedido(it) }
                            }
                            showDeleteDialog = false
                            pedidoToDelete = null
                        }
                    ) {
                        Text("Eliminar", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                            pedidoToDelete = null
                        }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
fun BlancasOrderTable(
    blancas: List<com.example.caritas20.Data.PedidoConCliente>,
    detailsViewModel: DetailsViewModel
) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            ContentRow("No.", "Cant.", "Precio", "Subtotal", backgroundColor = Color(0xFFB38BEE))
            blancas.forEach { pedidoConCliente ->
                val precioPorUnidad = detailsViewModel.getPrecioBlanca(pedidoConCliente.pedido.id_producto)
                val subtotalFila = pedidoConCliente.pedido.cantidad * precioPorUnidad
                ContentRow(
                    number = pedidoConCliente.pedido.id_producto,
                    amount = pedidoConCliente.pedido.cantidad.toString(),
                    price = "$${String.format("%.2f", precioPorUnidad)}",
                    subtotal = "$${String.format("%.2f", subtotalFila)}",
                    backgroundColor = Color(0xFFF8F8F8)
                )
            }
            
            // Subtotal de la tabla
            val subtotal = detailsViewModel.calcularSubtotalBlancas()
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Subtotal Blancas:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
                Text(
                    text = "$${String.format("%.2f", subtotal)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF66BFFF)
                )
            }
        }
    }
}

@Composable
fun ColoresOrderTable(
    colores: List<com.example.caritas20.Data.PedidoConCliente>,
    detailsViewModel: DetailsViewModel
) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            ContentRow("No.", "Cant.", "Precio", "Subtotal", backgroundColor = Color(0xFFB38BEE))
            colores.forEach { pedidoConCliente ->
                val precioPorUnidad = detailsViewModel.getPrecioColor(pedidoConCliente.pedido.id_producto)
                val subtotalFila = pedidoConCliente.pedido.cantidad * precioPorUnidad
                ContentRow(
                    number = pedidoConCliente.pedido.id_producto,
                    amount = pedidoConCliente.pedido.cantidad.toString(),
                    price = "$${String.format("%.2f", precioPorUnidad)}",
                    subtotal = "$${String.format("%.2f", subtotalFila)}",
                    backgroundColor = Color(0xFFF0F8FF)
                )
            }
            
            // Subtotal de la tabla
            val subtotal = detailsViewModel.calcularSubtotalColores()
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Subtotal Color:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
                Text(
                    text = "$${String.format("%.2f", subtotal)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF66BFFF)
                )
            }
        }
    }
} 