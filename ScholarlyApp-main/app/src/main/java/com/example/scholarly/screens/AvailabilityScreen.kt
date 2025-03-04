import android.widget.Toast
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.scholarly.database.DatabaseHelper
import com.example.scholarly.ui.theme.Purple40
import com.example.scholarly.ui.theme.darkGrayCustom
import com.example.scholarly.viewmodel.UserViewModel
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvailabilityScreen(
    viewModel: UserViewModel,
    databaseHelper: DatabaseHelper
) {
    val daysOfWeek = listOf(
        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
    )
    val selectedDays = remember { mutableStateListOf<String>() }

    // Defines the time options and a state variable to hold the selected option
    val timeOptions = listOf("Morning", "Evening")
    val selectedTimeOption = remember { mutableStateOf(timeOptions[0]) }

    val context = LocalContext.current

    // Access the OnBackPressedDispatcher to handle back navigation
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = {
                        onBackPressedDispatcher?.onBackPressed()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = darkGrayCustom)
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(darkGrayCustom)
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Select Availability",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Days of the week checkboxes
                daysOfWeek.forEach { day ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Checkbox(
                            checked = selectedDays.contains(day),
                            onCheckedChange = {
                                if (it) selectedDays.add(day) else selectedDays.remove(day)
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color.White,
                                uncheckedColor = Color.White,
                                checkmarkColor = Color.Gray
                            )
                        )
                        Text(day, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Time availability radio buttons
                Text(
                    text = "Select Available Time",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                timeOptions.forEach { time ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = selectedTimeOption.value == time,
                            onClick = { selectedTimeOption.value = time },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color.White,
                                unselectedColor = Color.White
                            )
                        )
                        Text(time, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Save Availability Button
                Button(
                    onClick = {
                        val tutorID = viewModel.getCurrentUserId()
                        val morningAvailable = selectedTimeOption.value == "Morning"
                        val eveningAvailable = selectedTimeOption.value == "Evening"
                        selectedDays.forEach { day ->
                            databaseHelper.insertAvailability(
                                tutorID ?: 0,
                                day,
                                morningAvailable,
                                eveningAvailable
                            )
                        }

                        Toast.makeText(context, "Availability Updated", Toast.LENGTH_SHORT).show()

                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Purple40),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Availability", color = Color.White)
                }

                Button(
                    onClick = {
                        val tutorID = viewModel.getCurrentUserId() ?: 0
                        if (databaseHelper.clearAvailabilityForTutor(tutorID)) {
                            Toast.makeText(context, "Availability Cleared", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Failed to Clear Availability", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Clear Availability", color = Color.White)
                }

            }

        }
    )
}
