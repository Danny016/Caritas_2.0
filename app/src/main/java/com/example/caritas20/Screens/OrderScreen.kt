package com.example.caritas20.Screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
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
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.caritas20.Functions.ContentRow
import com.example.caritas20.R
import com.example.caritas20.ViewModels.OrderViewModel
import com.example.caritas20.ViewModels.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    navController: NavController,
    viewModelFactory: ViewModelFactory
){
    val orderViewModel: OrderViewModel = viewModel(factory = viewModelFactory)
    val uiState by orderViewModel.uiState.collectAsState()

    // State for AddPiecesDialog
    var selectedTipo by remember { mutableStateOf("Blanca") }
    var selectedNumber by remember { mutableStateOf(0) }
    var cantidad by remember { mutableStateOf("") }
    var numberExpanded by remember { mutableStateOf(false) }
    
    val optionsRadioB = listOf("Blanca", "Color")
    val optionsNumber = (0..7).toList()

    Column (modifier = Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF66BFFF)
            ),
            title = { Text(text = "Pedido", ) },
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
                Text("Procesando...", modifier = Modifier.padding(16.dp))
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
                item{
                    Text("Datos del Cliente:", fontSize = 24.sp)
                    OutlinedTextField(
                        value = uiState.clienteName,
                        onValueChange = { orderViewModel.updateClienteName(it) },
                        label = { Text("Nombre") },
                        modifier = Modifier.padding(8.dp)
                            .fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = uiState.panaderia,
                        onValueChange = { orderViewModel.updatePanaderia(it) },
                        label = { Text("Panadería") },
                        modifier = Modifier.padding(8.dp)
                            .fillMaxWidth()
                    )
                    
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
                    
                    // Blancas Table
                    if (uiState.tempBlancas.isNotEmpty()) {
                        Text("Piezas Blancas:", fontSize = 18.sp, modifier = Modifier.padding(top = 8.dp))
                        TempOrderTable(pieces = uiState.tempBlancas, tipo = "Blanca")
                    }
                    
                    // Colores Table
                    if (uiState.tempColores.isNotEmpty()) {
                        Text("Piezas Color:", fontSize = 18.sp, modifier = Modifier.padding(top = 8.dp))
                        TempOrderTable(pieces = uiState.tempColores, tipo = "Color")
                    }
                }
                item {
                    Button(
                        onClick = {
                            orderViewModel.showAddPiecesDialog()
                        },
                        modifier = Modifier.padding(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF66BFFF),
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp), // Bordes redondeados
                        border = BorderStroke(2.dp, Color.Black), // Borde negro de 2dp
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = null,
                            modifier = Modifier.padding(8.dp)
                                .size(24.dp),
                            Color.White
                        )
                        Text("Agregar Piezas")
                    }
                }
                item {
                    Button(
                        onClick = {
                            orderViewModel.showConfirmDialog()
                        },
                        modifier = Modifier.padding(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF66BFFF),
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp), // Bordes redondeados
                        border = BorderStroke(2.dp, Color.Black), // Borde negro de 2dp
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = null,
                            modifier = Modifier.padding(8.dp)
                                .size(24.dp),
                            Color.White
                        )
                        Text("Confirmar Pedido")
                    }
                }
            }
        }
        
        if (uiState.showConfirmDialog) {
            ConfirmDialog(
                onDismiss = { orderViewModel.hideConfirmDialog() },
                onConfirm = { orderViewModel.confirmOrder() }
            )
        }
        
        if (uiState.showAddPiecesDialog) {
            AddPiecesDialog(
                selectedTipo = selectedTipo,
                onTipoChange = { selectedTipo = it },
                selectedNumber = selectedNumber,
                onNumberChange = { selectedNumber = it },
                cantidad = cantidad,
                onCantidadChange = { cantidad = it },
                numberExpanded = numberExpanded,
                onNumberExpandedChange = { numberExpanded = it },
                optionsRadioB = optionsRadioB,
                optionsNumber = optionsNumber,
                onDismiss = { 
                    orderViewModel.hideAddPiecesDialog()
                    // Reset form
                    selectedTipo = "Blanca"
                    selectedNumber = 0
                    cantidad = ""
                },
                onConfirm = {
                    orderViewModel.addPiece(selectedTipo, selectedNumber, cantidad)
                    // Reset form
                    selectedTipo = "Blanca"
                    selectedNumber = 0
                    cantidad = ""
                }
            )
        }
    }
    
    // Handle success navigation
    LaunchedEffect(uiState.success) {
        if (uiState.success) {
            orderViewModel.clearSuccess()
            navController.navigate("Home")
        }
    }
}

@Composable
fun OrderTable(){
    Card (
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF8CE5CA))
    ){
        Column (
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ){
            ContentRow("No.","Cantidad", "Subtotal", backgroundColor = Color(0xFFB38BEE))
        }
    }
}

@Composable
fun TempOrderTable(pieces: List<com.example.caritas20.ViewModels.TempPiece>, tipo: String) {
    Card (
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (tipo == "Blanca") Color(0xFFF0F0F0) else Color(0xFFE8F4FD)
        )
    ){
        Column (
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ){
            ContentRow("No.", "Cantidad", "Tipo", backgroundColor = Color(0xFFB38BEE))
            pieces.forEach { piece ->
                ContentRow(
                    number = piece.numero.toString(),
                    amount = piece.cantidad.toString(),
                    price = piece.tipo,
                    backgroundColor = if (tipo == "Blanca") Color(0xFFF8F8F8) else Color(0xFFF0F8FF)
                )
            }
        }
    }
}

@Composable
fun ConfirmDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Confirmación")
        },
        text = {
            Text("¿Deseas continuar con esta acción?")
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPiecesDialog(
    selectedTipo: String,
    onTipoChange: (String) -> Unit,
    selectedNumber: Int,
    onNumberChange: (Int) -> Unit,
    cantidad: String,
    onCantidadChange: (String) -> Unit,
    numberExpanded: Boolean,
    onNumberExpandedChange: (Boolean) -> Unit,
    optionsRadioB: List<String>,
    optionsNumber: List<Int>,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Agregar Piezas")
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Tipo selection
                Text("Tipo:", fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))
                optionsRadioB.forEach { text ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        RadioButton(
                            selected = (text == selectedTipo),
                            onClick = { onTipoChange(text) }
                        )
                        Text(text = text)
                    }
                }
                
                // Number selection
                Text("Número:", fontSize = 18.sp, modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
                ExposedDropdownMenuBox(
                    expanded = numberExpanded,
                    onExpandedChange = { onNumberExpandedChange(it) }
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = selectedNumber.toString(),
                        onValueChange = {},
                        label = { Text("Selecciona un número") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(numberExpanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .width(200.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = numberExpanded,
                        onDismissRequest = { onNumberExpandedChange(false) }
                    ) {
                        optionsNumber.forEach { number ->
                            DropdownMenuItem(
                                text = { Text(
                                    text = number.toString(),
                                    modifier = Modifier.width(200.dp),
                                    textAlign = TextAlign.Center
                                ) },
                                onClick = {
                                    onNumberChange(number)
                                    onNumberExpandedChange(false)
                                }
                            )
                        }
                    }
                }
                
                // Cantidad input
                Text("Cantidad:", fontSize = 18.sp, modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
                OutlinedTextField(
                    value = cantidad,
                    onValueChange = { onCantidadChange(it) },
                    label = { Text("Cantidad") },
                    modifier = Modifier
                        .width(200.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF66BFFF),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(2.dp, Color.Black)
            ) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = null,
                    modifier = Modifier.padding(4.dp).size(20.dp),
                    Color.White
                )
                Text("Agregar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
