package com.example.benative.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.benative.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.benative.ui.component.StyledButton
import com.example.benative.ui.component.StyledOutlinedButton
import com.example.benative.ui.theme.BeNativeTheme

@Composable
fun  LoginScreen(){
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        StyledButton(
            modifier = Modifier.width(250.dp)
        ){
            Text(
                text = stringResource(R.string.text_1)
            )
        }

        Text(
            modifier = Modifier.padding(top = 10.dp),
            text = stringResource(R.string.text_2)
        )

        StyledOutlinedButton(
            modifier = Modifier.width(250.dp)
        ){
            Row {
                Image(
                    painter = painterResource(R.drawable.google_pic),
                    contentDescription = null,
                    modifier = Modifier.width(28.dp).height(28.dp)
                )
                Text(
                    modifier = Modifier.padding(start = 10.dp).align(Alignment.CenterVertically),
                    text = stringResource(R.string.google_login),
                    color = Color.Black,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenView(){
    BeNativeTheme {
        LoginScreen()
    }
}
