package uk.ac.tees.mad.sq.screen

import android.util.Log
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import uk.ac.tees.mad.sq.ui.theme.poppins
import uk.ac.tees.mad.sq.viewmodel.QuizViewModel

@Composable
fun HomeScreen(navController: NavHostController, viewModel: QuizViewModel) {
    LaunchedEffect(key1 = true) {
        Log.d("HomeScreen", "LaunchedEffect triggered")
    }
    Text(text = "Home Screen", fontFamily = poppins, fontSize = 40.sp, color = colorScheme.primary)
}