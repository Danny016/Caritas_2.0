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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.caritas20.R
import com.example.caritas20.ViewModels.AddViewModel
import com.example.caritas20.ViewModels.ViewModelFactory
import com.example.caritas20.ui.components.ErrorComponent
import com.example.caritas20.ui.components.LoadingComponent
import com.example.caritas20.ui.components.SuccessComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    navController: NavController,
    viewModelFactory: ViewModelFactory
){
    val addViewModel: AddViewModel = viewModel(factory = viewModelFactory)
    val uiState by addViewModel.uiState.collectAsState()
    
    val optionsRadioB = listOf("Blanca", "Color")
    val optionsNumber = (0..7).toList()
    var expanded by remember { mutableStateOf(false) }

    Column (modifier = Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF66BFFF)
            ),
            title = { Text(text = "Agregar piezas", ) },
            navigationIcon = {
                IconButton(onClick = {
                    navController.navigate("Order")
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
            LoadingComponent(message = "Agregando...")
        } else {
            LazyColumn (
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(color = Color.White),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                 item {
                     Text("Tipo:", fontSize = 24.sp)
                     optionsRadioB.forEach { text ->
                         Row(
                             verticalAlignment = Alignment.CenterVertically,
                             modifier = Modifier.padding(vertical = 4.dp)
                         ) {
                             RadioButton(
                                 selected = (text == uiState.selectedTipo),
                                 onClick = { addViewModel.updateTipo(text) }
                             )
                             Text(text = text)
                         }
                     }
                 }
                item {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            readOnly = true,
                            value = uiState.selectedNumber,
                            onValueChange = {},
                            label = { Text("Selecciona un número") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .width(250.dp)
                                .padding(8.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            val suffix = if (uiState.selectedTipo == "Blanca") "B" else "C"
                            optionsNumber.forEach { number ->
                                DropdownMenuItem(
                                    text = { Text(
                                        text = "$number$suffix",
                                        modifier = Modifier.width(250.dp),
                                        textAlign = TextAlign.Center
                                    ) },
                                    onClick = {
                                        addViewModel.updateNumber("$number$suffix")
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                item {
                    OutlinedTextField(
                        value = uiState.cantidad,
                        onValueChange = { addViewModel.updateCantidad(it) },
                        label = { Text("Cantidad") },
                        modifier = Modifier.padding(8.dp)
                            .width(250.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
                
                if (uiState.error != null) {
                    item {
                        ErrorComponent(error = uiState.error!!)
                    }
                }
                
                if (uiState.success) {
                    item {
                        SuccessComponent(message = "Pedido agregado exitosamente")
                    }
                }
                
                item {
                    Button(
                        onClick = {
                            addViewModel.addPedido()
                        },
                        modifier = Modifier.padding(8.dp).wrapContentSize(),
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
                        Text("Agregar")
                    }
                }
            }
        }
    }
    
    // Handle success navigation
    LaunchedEffect(uiState.success) {
        if (uiState.success) {
            addViewModel.clearSuccess()
            navController.navigate("Order")
        }
    }
    
    // Handle error clearing
    LaunchedEffect(uiState.error) {
        if (uiState.error != null) {
            // Auto-clear error after 3 seconds
            kotlinx.coroutines.delay(3000)
            addViewModel.clearError()
        }
    }
}
