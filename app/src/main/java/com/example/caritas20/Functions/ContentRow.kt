package com.example.caritas20.Functions

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ContentRow(number: String, amount: String, price: String){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color.Black),
        horizontalArrangement = Arrangement.Center
    ){
        Column (
            modifier = Modifier
                .weight(1f)
                .border(2.dp, Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = number, Modifier.padding(8.dp))
        }
        Column (
            modifier = Modifier
                .weight(1f)
                .border(2.dp, Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = amount, Modifier.padding(8.dp))
        }
        Column (
            modifier = Modifier
                .weight(1f)
                .border(2.dp, Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = price, Modifier.padding(8.dp))
        }
    }
}