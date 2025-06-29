package com.example.caritas20.Screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.caritas20.ViewModels.PriceViewModel
import com.example.caritas20.ViewModels.ViewModelFactory
import com.example.caritas20.Functions.ContentRow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceScreen(
    navController: NavController,
    viewModelFactory: ViewModelFactory
){
    val priceViewModel: PriceViewModel = viewModel(factory = viewModelFactory)
    val uiState by priceViewModel.uiState.collectAsState()

    val showDialog = remember { mutableStateOf(false) }
    var tipo by remember { mutableStateOf("Blancas") }
    var numero by remember { mutableStateOf("0B") }
    var nuevoPrecio by remember { mutableStateOf("") }
    var numeroExpanded by remember { mutableStateOf(false) }
    val scope = remember { CoroutineScope(Dispatchers.Main) }

    Column (modifier = Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF66BFFF)
            ),
            title = { Text(text = "Tabla de Precios", ) },
            navigationIcon = {
                IconButton(onClick = {
                    navController.navigate("Home")
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
                Text("Cargando precios...", modifier = Modifier.padding(16.dp))
            }
        } else {
            LazyColumn (
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(color = Color.White)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                // Botón para abrir el diálogo
                item {
                    Button(
                        onClick = { showDialog.value = true }, 
                        modifier = Modifier.padding(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF66BFFF),
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(2.dp, Color.Black)
                    ) {
                        Text("Modificar Precio")
                    }
                }
                // Tabla Blancas
                item {
                    Text("Precios Blancas:", fontSize = 24.sp, modifier = Modifier.padding(16.dp))
                    ContentRow("Número", "Cantidad", "Precio", backgroundColor = Color(0xFFB38BEE))
                }
                    items(uiState.blancas.size) { idx ->
                        val blanca = uiState.blancas[idx]
                        ContentRow(
                            number = blanca.numero.toString(),
                            amount = blanca.cantidad.toString(),
                            price = "$${blanca.precio}",
                            backgroundColor = Color(0xFFF0F0F0)
                        )
                    }

                // Tabla Color
                item {
                    Text("Precios Color:", fontSize = 24.sp, modifier = Modifier.padding(16.dp))
                    ContentRow("Número", "Cantidad", "Precio", backgroundColor = Color(0xFFB38BEE))
                }
                items(uiState.colores.size) { idx ->
                    val color = uiState.colores[idx]
                    ContentRow(
                        number = color.numero.toString(),
                        amount = color.cantidad.toString(),
                        price = "$${color.precio}",
                        backgroundColor = Color(0xFFE8F4FD)
                    )
                }
                
                // Padding al final
                item {
                    androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(bottom = 64.dp))
                }
            }
        }
        // AlertDialog para modificar precios
        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                title = { Text("Modificar Precio") },
                text = {
                    Column {
                        // Tipo
                        Text("Tipo:")
                        Row {
                            listOf("Blancas", "Color").forEach { t ->
                                Button(
                                    onClick = { tipo = t },
                                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                        containerColor = if (tipo == t) Color(0xFF66BFFF) else Color.LightGray
                                    ),
                                    modifier = Modifier.padding(4.dp)
                                ) { Text(t) }
                            }
                        }
                        // Número
                        Text("Número:")
                        ExposedDropdownMenuBox(
                            expanded = numeroExpanded,
                            onExpandedChange = { numeroExpanded = it },
                            modifier = Modifier.padding(4.dp)
                        ) {
                            OutlinedTextField(
                                value = numero,
                                onValueChange = { },
                                label = { Text("Número") },
                                singleLine = true,
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = numeroExpanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = numeroExpanded,
                                onDismissRequest = { numeroExpanded = false }
                            ) {
                                val suffix = if (tipo == "Blancas") "B" else "C"
                                listOf(0, 1, 2, 3, 4, 5, 6, 7).forEach { n ->
                                    DropdownMenuItem(
                                        text = { Text("$n$suffix") },
                                        onClick = { 
                                            numero = "$n$suffix"
                                            numeroExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                        // Nuevo Precio
                        Text("Nuevo Precio:")
                        OutlinedTextField(
                            value = nuevoPrecio,
                            onValueChange = { nuevoPrecio = it },
                            label = { Text("Nuevo Precio") },
                            singleLine = true
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        val precioDouble = nuevoPrecio.toDoubleOrNull()
                        if (precioDouble != null) {
                            scope.launch {
                                if (tipo == "Blancas") {
                                    val blanca = priceViewModel.uiState.value.blancas.find { it.numero == numero }
                                    if (blanca != null) {
                                        priceViewModel.updateBlancasPrice(blanca.copy(precio = precioDouble))
                                    }
                                } else {
                                    val color = priceViewModel.uiState.value.colores.find { it.numero == numero }
                                    if (color != null) {
                                        priceViewModel.updateColorPrice(color.copy(precio = precioDouble))
                                    }
                                }
                                showDialog.value = false
                                nuevoPrecio = ""
                            }
                        }
                    }) {
                        Text("Confirmar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog.value = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}