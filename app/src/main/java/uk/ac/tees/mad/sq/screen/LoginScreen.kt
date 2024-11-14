package uk.ac.tees.mad.sq.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun LoginScreen(navController: NavController) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(colorScheme.primary)) {
        Box(
            modifier = Modifier
                .padding(top = 200.dp)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(colorScheme.secondary)
            ) {

            }
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(colorScheme.tertiary),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Welcome Back")
            }
        }
    }
}