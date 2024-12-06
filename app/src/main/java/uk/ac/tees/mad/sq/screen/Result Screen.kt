package uk.ac.tees.mad.sq.screen

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import uk.ac.tees.mad.sq.QuizNavigation
import uk.ac.tees.mad.sq.R
import uk.ac.tees.mad.sq.data.remote.Result
import uk.ac.tees.mad.sq.ui.theme.permanentMarker
import uk.ac.tees.mad.sq.ui.theme.poppins
import uk.ac.tees.mad.sq.viewmodel.QuizViewModel

@Composable
fun ResultScreen(
    navController: NavHostController,
    viewModel: QuizViewModel,
    selectedOption: String?,
    quizData: Result,
) {
    val context = LocalContext.current
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 80.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    colorScheme.tertiary
                )
        ) {
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowLeft,
                contentDescription = "null",
                modifier = Modifier
                    .size(80.dp)
                    .padding(20.dp)
                    .clickable {
                        navController.popBackStack()
                    }
            )
            Spacer(modifier = Modifier.height(160.dp))
            if (selectedOption == quizData.correct_answer) {
                CorrectAnsTemplate(correctAns = quizData.correct_answer)
            } else {
                triggerVibration(context)
                WrongAnsTemplate(onRetryClick = {
                    navController.popBackStack()
                })
            }
        }
    }
}

@Composable
fun CorrectAnsTemplate(correctAns: String) {
    var isVisible by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,  // Animate to full size
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
    )

    LaunchedEffect(Unit) {
        isVisible = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.correct),
            contentDescription = "correct",
            modifier = Modifier
                .size(100.dp)
                .scale(scale)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Hurrah!! You got the correct Answer.",
            fontFamily = permanentMarker,
            fontSize = 22.sp,
            modifier = Modifier.padding(50.dp)
        )
        Text(
            text = "$correctAns is the correct answer",
            fontFamily = poppins,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 50.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = { /*TODO*/ }, shape = RoundedCornerShape(20.dp), modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(20.dp)
        ) {
            Text(text = "Save", fontFamily = permanentMarker, fontSize = 22.sp)
        }
    }
}


@Composable
fun WrongAnsTemplate(onRetryClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition()

    val offsetX by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(100, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.wrong),
            contentDescription = "wrong",
            modifier = Modifier
                .size(100.dp)
                .offset(x = offsetX.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Ahh!! You got the wrong Answer.",
            fontFamily = permanentMarker,
            fontSize = 22.sp,
            modifier = Modifier.padding(50.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = { onRetryClick() },
            colors = ButtonDefaults.buttonColors(Color.Red),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(20.dp)
        ) {
            Text(text = "Retry", fontFamily = permanentMarker, fontSize = 22.sp)
        }
    }
}


private fun triggerVibration(context: Context) {
    val vibrator = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
        try {
            val vibratorManager = context.getSystemService(Context::class.java.getDeclaredField("VIBRATOR_MANAGER_SERVICE").get(null) as String)
            (vibratorManager as VibratorManager).defaultVibrator
        } catch (e: Exception) {
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    } else {
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }
    vibrator?.let {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            it.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            it.vibrate(500)
        }
    }
}