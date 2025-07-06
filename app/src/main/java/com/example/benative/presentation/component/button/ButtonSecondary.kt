package com.example.benative.presentation.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ButtonSecondary(
    modifier: Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit
){
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFf5e2e9)),
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFFE91E63)
        ),
        modifier = modifier
    ) {
        content()
    }
}