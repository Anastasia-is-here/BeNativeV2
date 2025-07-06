package com.example.benative.presentation.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.benative.presentation.theme.ManropeBold

@Composable
fun ButtonCircle(
    text: String,
    iconId: Int,
    size: Dp,
    iconSize: Dp,
    fontSize: TextUnit,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(Color(0x99f5e2e9))
            .clickable { onClick() }
            .border(width = 2.dp, color = Color(0xFFD97BA4), shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = text,
                tint = Color(0xFFE91E63),
                modifier = Modifier.size(iconSize)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = text,
                fontSize = fontSize * 0.8,
                color = Color(0xFFE91E63),
                fontWeight = FontWeight.Bold,
                fontFamily = ManropeBold
            )
        }
    }
}