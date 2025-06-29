package com.example.caritas20.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.caritas20.R
import com.example.caritas20.ViewModels.ModifyViewModel
import com.example.caritas20.ViewModels.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyScreen(
    navController: NavController,
    viewModelFactory: ViewModelFactory,
    clienteId: Int
){
    val modifyViewModel: ModifyViewModel = viewModel(factory = viewModelFactory)
    val uiState by modifyViewModel.uiState.collectAsState()

    // Estado para el AlertDialog de edición
    var showEditDialog by remember { mutableStateOf(false) }
    var pedidoToEdit by remember { mutableStateOf<com.example.caritas20.Data.PedidoConCliente?>(null) }
    var newCantidad by remember { mutableStateOf("") }

    // Cargar pedidos para el cliente específico
    LaunchedEffect(clienteId) {
        modifyViewModel.loadPedidosByCliente(clienteId)
    }

    Column (modifier = Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF66BFFF)
            ),
            title = { Text(text = "Modificar Pedidos", ) },
            navigationIcon = {
                IconButton(onClick = {
                    navController.navigateUp()
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
                Text("Cargando pedidos...", modifier = Modifier.padding(16.dp))
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
                        Text("No hay pedidos para modificar", fontSize = 24.sp, modifier = Modifier.padding(40.dp))
                    }
                } else {
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
                                    text = "Nombre: ${uiState.pedidos.first().cliente?.nombre ?: "N/A"}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                Text(
                                    text = "Panadería: ${uiState.pedidos.first().cliente?.panaderia ?: "N/A"}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                        
                        Text("Pedidos:", fontSize = 24.sp, modifier = Modifier.padding(top = 16.dp))
                    }
                    
                    items(uiState.pedidos) { pedido ->
                        Card(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                // Encabezado de la tabla
                                TableRow("Campo", "Valor", backgroundColor = Color(0xFFE8C6EC))
                                
                                // Tipo de producto
                                TableRow(
                                    field = "Tipo",
                                    value = if (pedido.pedido.id_producto.endsWith("B")) "Blanca" else "Color"
                                )
                                // Datos del pedido en formato tabla
                                TableRow(
                                    field = "Producto",
                                    value = pedido.pedido.id_producto
                                )
                                TableRow(
                                    field = "Cantidad",
                                    value = pedido.pedido.cantidad.toString()
                                )
                                
                                // Calcular precio y subtotal
                                val precioPorUnidad = if (pedido.pedido.id_producto.endsWith("B")) {
                                    modifyViewModel.getPrecioBlanca(pedido.pedido.id_producto)
                                } else {
                                    modifyViewModel.getPrecioColor(pedido.pedido.id_producto)
                                }
                                val subtotal = pedido.pedido.cantidad * precioPorUnidad
                                
                                TableRow(
                                    field = "Precio Unit.",
                                    value = "$${String.format("%.2f", precioPorUnidad)}"
                                )
                                TableRow(
                                    field = "Subtotal",
                                    value = "$${String.format("%.2f", subtotal)}"
                                )
                                
                                // Botones de acción
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Button(
                                        onClick = {
                                            pedidoToEdit = pedido
                                            newCantidad = pedido.pedido.cantidad.toString()
                                            showEditDialog = true
                                        },
                                        modifier = Modifier.wrapContentSize(),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF66BFFF)
                                        )
                                    ) {
                                        Icon(Icons.Filled.Edit, contentDescription = null)
                                        Text("Editar")
                                    }
                                    
                                    Button(
                                        onClick = { 
                                            modifyViewModel.deletePedido(pedido.pedido)
                                        },
                                        modifier = Modifier.wrapContentSize(),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.Red
                                        )
                                    ) {
                                        Icon(Icons.Filled.Delete, contentDescription = null)
                                        Text("Eliminar")
                                    }
                                }
                            }
                        }
                    }
                    
                    // Total General
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
                                    text = "$${String.format("%.2f", modifyViewModel.calcularTotal())}",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    
    // Manejar éxito
    LaunchedEffect(uiState.success) {
        if (uiState.success) {
            modifyViewModel.clearSuccess()
            // Podría mostrar un mensaje de éxito o navegar
        }
    }

    // AlertDialog para editar cantidad
    if (showEditDialog && pedidoToEdit != null) {
        AlertDialog(
            onDismissRequest = {
                showEditDialog = false
                pedidoToEdit = null
            },
            title = { Text("Editar Cantidad") },
            text = {
                Column {
                    Text("Producto: ${pedidoToEdit?.pedido?.id_producto}")
                    OutlinedTextField(
                        value = newCantidad,
                        onValueChange = { newCantidad = it.filter { c -> c.isDigit() } },
                        label = { Text("Cantidad") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val cantidadInt = newCantidad.toIntOrNull()
                        if (cantidadInt != null && cantidadInt > 0 && pedidoToEdit != null) {
                            val pedidoEditado = pedidoToEdit!!.pedido.copy(cantidad = cantidadInt)
                            modifyViewModel.updatePedido(pedidoEditado)
                            showEditDialog = false
                            pedidoToEdit = null
                        }
                    }
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showEditDialog = false
                        pedidoToEdit = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun TableRow(
    field: String,
    value: String,
    backgroundColor: Color = Color(0xFFF0F0F0)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 1.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .padding(1.dp)
                .weight(1f),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(6.dp),
            colors = CardDefaults.cardColors(containerColor = backgroundColor),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = field, 
                    modifier = Modifier.padding(8.dp),
                    fontSize = 14.sp
                )
            }
        }

        Card(
            modifier = Modifier
                .padding(1.dp)
                .weight(1f),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(6.dp),
            colors = CardDefaults.cardColors(containerColor = backgroundColor)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = value, 
                    modifier = Modifier.padding(8.dp),
                    fontSize = 14.sp
                )
            }
        }
    }
}