/*package com.example.scholarly.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.scholarly.models.Tutor
import com.example.scholarly.ui.theme.darkGrayCustom

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun ApprovalScreen(navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize(),
            color = darkGrayCustom){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Approval Page", style = MaterialTheme.typography.headlineLarge, color = Color.White)
            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {
                Log.d("ApprovalScreen", "Navigating to PendingTutorScreen")
                navController.navigate("PendingTutorScreen") // Navigate to Pending Tutors screen
            }) {
                Text("Approve Requests")
            }
        }
    }
}
*/