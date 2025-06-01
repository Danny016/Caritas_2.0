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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.caritas20.Functions.ContentRow
import com.example.caritas20.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceScreen(navController: NavController){
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
        LazyColumn (
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(color = Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            item {
                PriceTable()
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
                        imageVector = Icons.Filled.Settings,
                        contentDescription = null,
                        modifier = Modifier.padding(8.dp)
                            .size(24.dp),
                        Color.White
                    )
                    Text("Modificar Precios")
                }
            }
        }
    }
}

@Composable
fun PriceTable(){
    Column (
        modifier = Modifier
            .padding(16.dp)
            .border(2.dp, Color.Black, RoundedCornerShape(8.dp))
            .wrapContentSize()
            .fillMaxWidth()
            .background(Color(0xFFE8C6EC))
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, Color.Black)
                .background(Color(0xFF66BFFF)),
            horizontalArrangement = Arrangement.Center
        ){
            Text("Blanca", Modifier.padding(8.dp))
        }
        ContentRow("No.","Cantidad", "Precio")
        ContentRow("0","1000", "$100.00")
        ContentRow("1","1000", "$100.00")
        ContentRow("2","1000", "$100.00")
        ContentRow("3","1000", "$100.00")
        ContentRow("4","1000", "$100.00")
        ContentRow("4 1/2","100", "$100.00")
        ContentRow("5","100", "$100.00")
        ContentRow("6","1", "$100.00")
        ContentRow("7","1", "$100.00")
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, Color.Black)
                .background(Color(0xFF66BFFF)),
            horizontalArrangement = Arrangement.Center
        ){
            Text("Color", Modifier.padding(8.dp))
        }
        ContentRow("No.","Cantidad", "Precio")
        ContentRow("0","1000", "$100.00")
        ContentRow("1","1000", "$100.00")
        ContentRow("2","1000", "$100.00")
        ContentRow("3","1000", "$100.00")
        ContentRow("4","1000", "$100.00")
        ContentRow("4 1/2","100", "$100.00")
        ContentRow("5","100", "$100.00")
        ContentRow("6","1", "$100.00")
        ContentRow("7","1", "$100.00")
    }

}