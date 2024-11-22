package uk.ac.tees.mad.sq.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import uk.ac.tees.mad.sq.QuizApp
import uk.ac.tees.mad.sq.QuizNavigation
import uk.ac.tees.mad.sq.R
import uk.ac.tees.mad.sq.ui.theme.permanentMarker
import uk.ac.tees.mad.sq.viewmodel.QuizViewModel

@Composable
fun SplashScreen(navController: NavController, viewModel : QuizViewModel) {
    val logged = viewModel.loggedIn
    LaunchedEffect(key1 = true) {
        delay(2000L)
        if (logged.value) {
            navController.navigate(QuizNavigation.HomeScreen.route){
                popUpTo(0)
            }
        } else {
            navController.navigate(QuizNavigation.LoginScreen.route){
                popUpTo(0)
            }
        }
    }
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(id = R.drawable.designer), contentDescription = null,
            modifier = Modifier
                .size(180.dp)
                .clip(CircleShape)
                .border(1.dp, color = colorScheme.primary, CircleShape))
        Text(text = "Smart Quiz", fontFamily = permanentMarker, fontSize = 26.sp)
    }
}