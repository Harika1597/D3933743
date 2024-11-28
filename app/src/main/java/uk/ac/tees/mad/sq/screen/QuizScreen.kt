package uk.ac.tees.mad.sq.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import uk.ac.tees.mad.sq.R
import uk.ac.tees.mad.sq.data.remote.Result
import uk.ac.tees.mad.sq.ui.theme.poppins
import uk.ac.tees.mad.sq.viewmodel.QuizViewModel

@Composable
fun QuizScreen(navController: NavHostController, viewModel: QuizViewModel, quizData: Result) {
    val backgroundColor = listOf(
        colorScheme.primary,
        colorScheme.tertiary
    )
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
                    modifier = Modifier.size(40.dp),
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
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(10.dp)
                .clip(RoundedCornerShape(20.dp))){
                Image(painter = painterResource(id = R.drawable.wireframe), contentDescription = null, modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop)
                Text(text = quizData.question, fontFamily = poppins, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}