package uk.ac.tees.mad.sq

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.sq.ui.theme.SmartQuizTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartQuizTheme {

            }
        }
    }
}

enum class QuizNavigation(val route : String){
    SplashScreen("splash_screen"),
    LoginScreen("login_screen"),
    SignupScreen("signup_screen"),
    HomeScreen("home_screen"),
    QuizScreen("quiz_screen"),
    ResultScreen("result_screen")
}

@Composable
fun QuizApp(){

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = QuizNavigation.SplashScreen.route) {
        composable(route = QuizNavigation.SplashScreen.route) {
            SplashScreen(navController)
        }
    }
}