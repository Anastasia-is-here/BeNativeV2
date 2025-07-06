package com.example.benative.presentation.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt

@Composable
fun StyledOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier,
    placeholder: @Composable () -> Unit,
){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = placeholder,
        shape = RoundedCornerShape(30.dp),
        colors = OutlinedTextFieldDefaults.colors(
            errorTextColor = Color.Red,
            focusedBorderColor = Color("#FFFFFF".toColorInt()),
            )
    )
}