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
import androidx.compose.material3.Icon
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
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import uk.ac.tees.mad.sq.QuizNavigation
import uk.ac.tees.mad.sq.R
import uk.ac.tees.mad.sq.buildProjectDetailRoute
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
    val userPoints = viewModel.points
    var isLoading by remember { mutableStateOf(false) }
    val isClicked = remember {
        mutableStateOf(false)
    }
    val isClickable = remember {
        mutableStateOf(true)
    }
    LaunchedEffect(key1 = isClicked.value) {
        if (isClickable.value) {
            isClickable.value = false
            delay(10000L)
            isClickable.value = true
        }
    }
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
                        .clickable {
                            navController.navigate(QuizNavigation.ProfileScreen.route)
                        }
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
                        .clickable {
                            navController.navigate(QuizNavigation.ProfileScreen.route)
                        }
                )
            }
            Spacer(modifier = Modifier.width(5.dp))
            Column(modifier = Modifier.padding(5.dp)) {
                Text(
                    text = userInfo.value.name,
                    fontFamily = poppins,
                    fontSize = 20.sp,
                    color = Color.White,
                    modifier = Modifier.clickable {
                        navController.navigate(QuizNavigation.ProfileScreen.route)
                    }
                )
                Text(text = if (userPoints.value > 100) "Expert" else "Beginner", fontFamily = poppins, fontSize = 12.sp, color = Color.White)
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .height(100.dp)
                    .width(125.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color.White)
                    .clickable {

                    }
            ) {
                Row {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(4.dp)
                            .size(55.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFA800))
                            ,
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.flash),
                            contentDescription = null, modifier = Modifier.size(40.dp).align(Alignment.Center),
                            tint = Color.White
                        )
                    }
                    Text(
                        text = userPoints.value.toString(),
                        fontFamily = poppins,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        color = Color.Black,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(1.dp))
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
                    CategoryView(category = category, onCategoryClick = {
                        if (!isLoading) {
                            isLoading = true
                            isClicked.value = !isClicked.value
                            viewModel.fetchFromApi(category.id) {
                                isLoading = false
                            }
                        }
                    }, isClickale = isClickable.value)
                }
            }
        }
        Column(modifier = Modifier.padding(horizontal = 4.dp)) {
            Text(
                text = "Choose Quiz", fontFamily = poppins, fontSize = 20.sp, color = Color.White,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                if (quizList.value != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(12.dp)
                    ) {
                        Text(
                            text = quizList.value!!.results.get(0).category,
                            fontFamily = permanentMarker,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "Difficulty: " + quizList.value!!.results.get(0).difficulty,
                            fontFamily = permanentMarker,
                            fontSize = 12.sp
                        )
                    }
                    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                        items(quizList.value!!.results) { quiz ->
                            QuizCard(quiz = quiz, onQuizClicked = {
                                navigateToQuizScreen(navController, quiz)
                            })
                        }
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "No Data")
                    }
                }
            }
        }
    }
}

@Composable
fun QuizCard(quiz: Result, onQuizClicked: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .size(150.dp)
            .clip(RoundedCornerShape(16.dp))
            .padding(8.dp)
            .clickable {
                onQuizClicked()
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
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorScheme.onSurface
            )
        }
    }
}

@Composable
fun CategoryView(category: Category, onCategoryClick: () -> Unit, isClickale: Boolean) {
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
                .clickable(enabled = isClickale) {
                    isClicked = !isClicked
                    onCategoryClick()
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

fun navigateToQuizScreen(navController: NavHostController, quiz: Result) {
    val route = buildProjectDetailRoute(quiz)
    navController.navigate(route)
}