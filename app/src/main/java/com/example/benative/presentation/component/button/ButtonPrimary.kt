package com.example.benative.presentation.component.button

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ButtonPrimary(
    enabled: Boolean = true,
    modifier: Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit
){
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        modifier = modifier,
        enabled = enabled
    ) {
        content()
    }
}