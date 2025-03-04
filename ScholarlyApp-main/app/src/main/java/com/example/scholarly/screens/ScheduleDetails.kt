
package com.example.scholarly.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.scholarly.MainActivity
import com.example.scholarly.ui.theme.darkGrayCustom

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleDetailsScreen(
    meetingID: Int,
    navController: NavController
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = darkGrayCustom
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            TopAppBar(
                title = {
                    Text("Schedule Details", color = Color.White, fontSize = 24.sp)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Go back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MainActivity.backgroundPurple
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Display placeholder meeting details
            Text(
                text = "Meeting Details Unavailable",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
        }
    }
}
