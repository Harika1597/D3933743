package uk.ac.tees.mad.sq.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import uk.ac.tees.mad.sq.R
import uk.ac.tees.mad.sq.categoryList
import uk.ac.tees.mad.sq.data.Category
import uk.ac.tees.mad.sq.ui.theme.poppins
import uk.ac.tees.mad.sq.viewmodel.QuizViewModel

@Composable
fun HomeScreen(navController: NavHostController, viewModel: QuizViewModel) {
    val backgroundColor = listOf(
        colorScheme.primary,
        colorScheme.tertiary
    )
    val userInfo = viewModel.userInformation
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
        Column(modifier = Modifier.padding(20.dp)) {
            Text(text = "Quiz", fontFamily = poppins, fontSize = 20.sp, color = Color.White)
            LazyRow {
                items(categoryList) { category ->
                    CategoryView(category = category)
                }
            }
        }
    }
}

@Composable
fun CategoryView(category: Category) {
    Column(modifier = Modifier
        .size(width = 100.dp, height = 150.dp)
        .padding(8.dp)
        ) {
        Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.size(80.dp),colors = CardDefaults.cardColors(
            containerColor = colorScheme.tertiary.copy(alpha = 0.5f)
        )) {
            Image(
                painter = painterResource(id = category.image),
                contentDescription = "category image",
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
        Text(text = category.name, fontSize = 12.sp, fontFamily = poppins, color = Color.White)
    }
}