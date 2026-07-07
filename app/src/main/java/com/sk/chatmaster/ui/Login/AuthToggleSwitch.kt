package com.sk.chatmaster.ui.Login

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AuthToggleSwitch(
    isSignUpSelected: Boolean,
    onToggleChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    // Styling constants matching the image
    val containerColor = Color(0xFFF1F2F6) // Dark gray track
    val activeColor = Color(0xFFA5BFF3)
    val inactiveTextColor = Color(0xFF002C49)
    val activeTextColor = Color(0xFF020F5D)

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(containerColor, RoundedCornerShape(24.dp))
            .padding(4.dp) // Gap between background and active pill
    ) {
        val maxWidth = maxWidth
        val tabWidth = maxWidth / 2

        // Animate the horizontal offset position of the active white pill
        val indicatorOffset by animateDpAsState(
            targetValue = if (isSignUpSelected) tabWidth else 0.dp,
            label = "IndicatorOffset"
        )

        // Sliding Active Background Pill
        Box(
            modifier = Modifier
                .offset(x = indicatorOffset)
                .width(tabWidth)
                .fillMaxHeight()
                .background(activeColor, RoundedCornerShape(20.dp))
        )

        // Text Labels Row
        Row(modifier = Modifier.fillMaxSize()) {
            // Login Tab
            val loginTextColor by animateColorAsState(
                targetValue = if (!isSignUpSelected) activeTextColor else inactiveTextColor,
                label = "LoginTextColor"
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(20.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onToggleChanged(false) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Login",
                    color = loginTextColor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Sign up Tab
            val signUpTextColor by animateColorAsState(
                targetValue = if (isSignUpSelected) activeTextColor else inactiveTextColor,
                label = "SignUpTextColor"
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(15.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onToggleChanged(true) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Sign up",
                    color = signUpTextColor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}