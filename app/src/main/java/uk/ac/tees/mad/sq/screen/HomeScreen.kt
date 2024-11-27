package uk.ac.tees.mad.sq.screen

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import uk.ac.tees.mad.sq.R
import uk.ac.tees.mad.sq.categoryList
import uk.ac.tees.mad.sq.data.Category
import uk.ac.tees.mad.sq.data.remote.Result
import uk.ac.tees.mad.sq.ui.theme.permanentMarker
import uk.ac.tees.mad.sq.ui.theme.poppins
import uk.ac.tees.mad.sq.viewmodel.QuizViewModel

@Composable
fun HomeScreen(navController: NavHostController, viewModel: QuizViewModel) {
    val backgroundColor = listOf(
        colorScheme.primary,
        colorScheme.tertiary
    )
    val userInfo = viewModel.userInformation
    val quizList = viewModel.quizData
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.linearGradient(colors = backgroundColor))
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(20.dp)
        ) {
            if (userInfo.value.profilePictureUrl.isNotEmpty()) {
                AsyncImage(
                    model = userInfo.value.profilePictureUrl,
                    contentDescription = "profile picture",
                    modifier = Modifier
                        .size(60.dp)
                        .align(Alignment.CenterVertically)
                        .clip(CircleShape)
                        .border(1.dp, Color.White, CircleShape)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.boy),
                    contentDescription = "profile picture",
                    modifier = Modifier
                        .size(60.dp)
                        .align(Alignment.CenterVertically)
                        .clip(CircleShape)
                        .border(1.dp, Color.White, CircleShape)
                )
            }
            Spacer(modifier = Modifier.width(5.dp))
            Column(modifier = Modifier.padding(5.dp)) {
                Text(
                    text = userInfo.value.name,
                    fontFamily = poppins,
                    fontSize = 20.sp,
                    color = Color.White
                )
                Text(text = "Expert", fontFamily = poppins, fontSize = 12.sp, color = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Column(modifier = Modifier.padding(4.dp)) {
            Text(
                text = "Quiz",
                fontFamily = poppins,
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            LazyRow {
                items(categoryList) { category ->
                    CategoryView(category = category)
                }
            }
        }
        Column(modifier = Modifier.padding(horizontal = 4.dp)) {
            Text(
                text = "Choose Quiz", fontFamily = poppins, fontSize = 20.sp, color = Color.White,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            if (quizList.value != null) {
                LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                    items(quizList.value!!.results) { quiz ->
                        QuizCard(quiz = quiz)
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun QuizCard(quiz: Result) {
    val optionList = remember {
        mutableListOf(quiz.incorrect_answers.random(), quiz.incorrect_answers.random(), quiz.correct_answer)
    }
    Log.d("QUIZCARD", "QuizCard: $optionList")
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .size(150.dp)
            .clip(RoundedCornerShape(16.dp))
            .padding(8.dp)
            .clickable {

            },
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.tertiary.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = quiz.question,
                fontFamily = poppins,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorScheme.onSurface
            )
        }
    }
}

@Composable
fun CategoryView(category: Category) {
    var isClicked by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isClicked) 0.95f else 1f,
        animationSpec = androidx.compose.animation.core.spring()
    )

    Column(
        modifier = Modifier
            .size(width = 100.dp, height = 150.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .size(80.dp)
                .scale(scale)
                .clip(RoundedCornerShape(16.dp))
                .clickable {

                    isClicked = !isClicked
                },
            colors = CardDefaults.cardColors(
                containerColor = colorScheme.tertiary.copy(alpha = 0.5f)
            )
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = category.image),
                    contentDescription = "category image",
                    modifier = Modifier.size(50.dp)
                )
            }
        }
        Text(
            text = category.name,
            fontSize = 10.sp,
            fontFamily = poppins,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}
