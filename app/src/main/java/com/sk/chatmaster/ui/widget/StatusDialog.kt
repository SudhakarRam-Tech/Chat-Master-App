package com.sk.chatmaster.ui.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun StatusDialog(
    state: DialogState,
    onDismiss: (String?) -> Unit
) {
    when (state) {
        is DialogState.Hidden -> { /* Render nothing */ }

        is DialogState.Success -> {
            /*AlertDialog(
                onDismissRequest = { onDismiss(state.destinationRoute) },
                icon = {
                    Icon(
                        modifier = Modifier.size(70.dp),
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = Color(0xFF4CAF50) // Green
                    )
                },
                title = { Text(text = "Success!") },
                text = { Text(text = state.successMsg) },
                confirmButton = {
                    Button(
                        modifier = Modifier.size(70.dp),
                        onClick = { onDismiss(state.destinationRoute) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Text("OK")
                    }
                }
            )*/
            Dialog(onDismissRequest = { onDismiss(null) }) {
                // Custom container for custom arrangement
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally // Centers all children
                    ) {
                        // 1. Icon at the center
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = Color(0xFF4CAF50), // Red
                            modifier = Modifier.size(80.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Title (Optional, remove if you only want the message)
                        /*Text(
                            text = "Success",
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center
                        )*/

                        Spacer(modifier = Modifier.height(8.dp))

                        // 2. Message below the icon
                        Text(
                            text = state.successMsg,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // 3. Close button below the message
                        Button(
                            onClick = { onDismiss(state.destinationRoute) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                            modifier = Modifier.fillMaxWidth(0.7f) // Fits nicely at the center bottom
                        ) {
                            Text("Close")
                        }
                    }
                }
            }
        }
        is DialogState.Failure ->
        Dialog(onDismissRequest = { onDismiss(null) }) {
            // Custom container for custom arrangement
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally // Centers all children
                ) {
                    // 1. Icon at the center
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Error",
                        tint = Color(0xFFF44336), // Red
                        modifier = Modifier.size(80.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Title (Optional, remove if you only want the message)
                    Text(
                        text = "Error Occurred",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // 2. Message below the icon
                    Text(
                        text = state.errorMsg,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // 3. Close button below the message
                    Button(
                        onClick = { onDismiss(null) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
                        modifier = Modifier.fillMaxWidth(0.7f) // Fits nicely at the center bottom
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShowDialog() {
    StatusDialog(DialogState.Success("Account Created Successfully"),
        onDismiss = {

        })
}