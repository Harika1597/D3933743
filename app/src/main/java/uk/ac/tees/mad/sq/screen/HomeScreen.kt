package uk.ac.tees.mad.sq.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import uk.ac.tees.mad.sq.ui.theme.poppins
import uk.ac.tees.mad.sq.viewmodel.QuizViewModel

@Composable
fun HomeScreen(navController: NavHostController, viewModel: QuizViewModel) {
    Scaffold {
        Column(modifier = Modifier.padding(it)) {
            Row {
                Text(text = "")
            }
        }
    }
}