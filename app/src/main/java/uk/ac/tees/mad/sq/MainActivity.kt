package uk.ac.tees.mad.sq

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.json.Json
import uk.ac.tees.mad.sq.data.remote.Result
import uk.ac.tees.mad.sq.screen.HomeScreen
import uk.ac.tees.mad.sq.screen.LoginScreen
import uk.ac.tees.mad.sq.screen.QuizScreen
import uk.ac.tees.mad.sq.screen.RegistrationScreen
import uk.ac.tees.mad.sq.screen.SplashScreen
import uk.ac.tees.mad.sq.ui.theme.SmartQuizTheme
import uk.ac.tees.mad.sq.viewmodel.QuizViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartQuizTheme {
                    QuizApp()
            }
        }
    }
}

fun buildProjectDetailRoute(quiz : Result): String {
    val projectJson = Uri.encode(Gson().toJson(quiz))
    return "${QuizNavigation.QuizScreen.route}/$projectJson"
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
    Surface {
        val navController = rememberNavController()
        val viewModel : QuizViewModel = viewModel()
        NavHost(
            navController = navController,
            startDestination = QuizNavigation.SplashScreen.route
        ) {
            composable(route = QuizNavigation.SplashScreen.route) {
                SplashScreen(navController, viewModel)
            }
            composable(route = QuizNavigation.LoginScreen.route){
                LoginScreen(navController, viewModel)
            }
            composable(route = QuizNavigation.SignupScreen.route){
                RegistrationScreen(navController,viewModel)
            }
            composable(route = QuizNavigation.HomeScreen.route){
                HomeScreen(navController,viewModel)
            }
            composable(
                route = "${QuizNavigation.QuizScreen.route}/{result}",
                arguments = listOf(navArgument("result") {
                    type = NavType.StringType
                })
            ) { backStackEntry ->
                val resultJson = backStackEntry.arguments?.getString("result")
                val result = Gson().fromJson(resultJson, Result::class.java)
                QuizScreen(navController, viewModel, result)
            }
            composable(route = QuizNavigation.ResultScreen.route){
//                ResultScreen(navController,viewModel)
            }
        }
    }
}