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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.caritas20.Functions.ContentRow
import com.example.caritas20.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(navController: NavController){

    var name by remember { mutableStateOf("") }
    var bakery by remember { mutableStateOf("") }

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
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.padding(8.dp)
                        .fillMaxWidth()
                )
                OutlinedTextField(
                    value = bakery,
                    onValueChange = { bakery = it },
                    label = { Text("Panadería") },
                    modifier = Modifier.padding(8.dp)
                        .fillMaxWidth()
                )
                Text("Pedido:", fontSize = 24.sp)
                OrderTable()
            }
            item {
                Button(
                    onClick = {},
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
                    Text("Agregar Pedido")
                }
            }
            item {
                Button(
                    onClick = {},
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