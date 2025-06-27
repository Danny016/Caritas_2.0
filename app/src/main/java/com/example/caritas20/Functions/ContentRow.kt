package com.example.caritas20.Functions

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



@Composable
fun ContentRow(
    number: String,
    amount: String,
    price: String,
    subtotal: String? = null,
    backgroundColor: Color = Color(0xFFE8C6EC) // Color por defecto si no se pasa uno
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
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
                    text = number, 
                    modifier = Modifier.padding(4.dp),
                    fontSize = 12.sp
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
                    text = amount, 
                    modifier = Modifier.padding(4.dp),
                    fontSize = 12.sp
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
                    text = price, 
                    modifier = Modifier.padding(4.dp),
                    fontSize = 12.sp
                )
            }
        }
        
        // Cuarta columna para subtotal (solo si se proporciona)
        if (subtotal != null) {
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
                        text = subtotal, 
                        modifier = Modifier.padding(4.dp),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
