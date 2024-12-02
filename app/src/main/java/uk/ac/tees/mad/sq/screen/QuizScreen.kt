package uk.ac.tees.mad.sq.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import uk.ac.tees.mad.sq.QuizNavigation
import uk.ac.tees.mad.sq.R
import uk.ac.tees.mad.sq.buildProjectDetailRoute
import uk.ac.tees.mad.sq.buildResultRoute
import uk.ac.tees.mad.sq.data.remote.Result
import uk.ac.tees.mad.sq.ui.theme.permanentMarker
import uk.ac.tees.mad.sq.ui.theme.poppins
import uk.ac.tees.mad.sq.viewmodel.QuizViewModel
import kotlin.random.Random

@Composable
fun QuizScreen(navController: NavHostController, viewModel: QuizViewModel, quizData: Result) {
    val backgroundColor = listOf(
        colorScheme.primary,
        colorScheme.tertiary
    )
    val selectedOption = rememberSaveable {
        mutableStateOf("")
    }
    val shuffledAnswers = remember { getShuffledAnswers(quizData) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.linearGradient(backgroundColor))
            .statusBarsPadding()
    ) {
        Row(Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(1.dp, Color.LightGray, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowLeft,
                    contentDescription = "",
                    modifier = Modifier.size(40.dp).clickable {
                        navController.navigate(QuizNavigation.HomeScreen.route){
                            popUpTo(0)
                        }
                    },
                    tint = Color.Black
                )
            }
            Spacer(modifier = Modifier.weight(0.3f))
            Text(
                text = "Answer the Quiz",
                fontFamily = poppins,
                fontSize = 20.sp,
                modifier = Modifier.align(Alignment.CenterVertically),
                color = Color.White
            )
            Spacer(modifier = Modifier.weight(0.5f))
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 40.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    colorScheme.tertiary
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(10.dp)
                    .clip(RoundedCornerShape(20.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.wireframe),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = quizData.question,
                    fontFamily = poppins,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(10.dp)
                )
            }
            Column(modifier = Modifier.padding(20.dp)) {
                for (answer in shuffledAnswers) {
                    optionChoose(
                        option = answer,
                        selectedOption = selectedOption.value,
                        onOptionClick = {
                            selectedOption.value = answer
                            Log.d("selectedOption", selectedOption.value)
                        }
                    )
                }
                Spacer(modifier = Modifier.height(30.dp))
                Button(onClick = {
                    if (selectedOption.value.isNotEmpty()) {
                        if (selectedOption.value == quizData.correct_answer){
                            Log.d("Correct","Updating ")
                            viewModel.addAnswer(quizData.question)
                        }
                        navigateToResultScreen(navController,quizData,selectedOption.value)
                    } }, shape = RoundedCornerShape(30.dp), modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Next", fontFamily = permanentMarker, color = Color.White, fontSize = 22.sp)
                }
            }
        }
    }
}

@Composable
fun optionChoose(option: String, selectedOption: String, onOptionClick: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .padding(4.dp)
        .border(
            1.dp,
            if (option == selectedOption) Color.Green else Color.LightGray,
            RoundedCornerShape(20.dp)
        )
        .clip(RoundedCornerShape(20.dp))
        .background(if (option == selectedOption) Color(0xFF90EE90) else Color.White)
        .clickable {
            onOptionClick()
        }) {
        Row(modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(20.dp))) {
            Text(
                text = option,
                fontFamily = permanentMarker,
                color = Color.Black,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            if (option == selectedOption) {
                Image(
                    painter = painterResource(id = R.drawable.checked),
                    contentDescription = "checked",
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(30.dp)
                        .align(Alignment.CenterVertically)

                )
            }
        }
    }
}

fun getShuffledAnswers(result: Result): List<String> {
    val allAnswers = mutableListOf(result.correct_answer)
    allAnswers.addAll(result.incorrect_answers)

    allAnswers.shuffle(Random(System.currentTimeMillis()))

    return allAnswers
}

fun navigateToResultScreen(navController: NavHostController, quiz : Result,selectedOption: String) {
    val route = buildResultRoute(selectedOption,quiz)
    navController.navigate(route)
}