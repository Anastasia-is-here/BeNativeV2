package com.example.benative.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.benative.ui.theme.BeNativeTheme

@Composable
fun StyledButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit = {},
){
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = Color.Black
        ),
        shape = RoundedCornerShape(18.dp)
    ) {
        content()
    }
}

@Composable
fun StyledOutlinedButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit = {},
){
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = Color.White
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Color.Black
        ),
        shape = RoundedCornerShape(18.dp)
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun StyledButtonPreview(){
    BeNativeTheme {
        StyledOutlinedButton()
    }
}