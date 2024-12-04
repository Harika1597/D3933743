package uk.ac.tees.mad.sq.screen

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.LockReset
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Restore
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import uk.ac.tees.mad.sq.QuizNavigation
import uk.ac.tees.mad.sq.R
import uk.ac.tees.mad.sq.ui.theme.permanentMarker
import uk.ac.tees.mad.sq.ui.theme.poppins
import uk.ac.tees.mad.sq.viewmodel.QuizViewModel

@Composable
fun ProfileScreen(navController: NavHostController, viewModel: QuizViewModel) {
    val userPoint = viewModel.points
    val userInfo = viewModel.userInformation
    val name = remember { mutableStateOf(userInfo.value.name) }
    val email = remember { mutableStateOf(userInfo.value.email) }
    val showAlertDialog = remember {
        mutableStateOf(false)
    }
    var actionType by remember {
        mutableStateOf("")
    }
    val backgroundColor = listOf(
        colorScheme.primary,
        colorScheme.tertiary
    )
    if (showAlertDialog.value) {
        when (actionType) {
            "Save" -> CommonAlertDialog(
                title = "Save Changes",
                message = "Are you sure you want to save changes",
                onConfirm = { /*TODO*/ },
                onDismiss = { showAlertDialog.value = false })

            "Reset" -> CommonAlertDialog(title = "Reset Points",
                message = "Are you sure you want to reset points",
                onConfirm = { /*TODO*/ },
                onDismiss = { showAlertDialog.value = false })

            "Logout" -> CommonAlertDialog(title = "Logout",
                message = "Are you sure you want to logout",
                onConfirm = { /*TODO*/ },
                onDismiss = { showAlertDialog.value = false})
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.linearGradient(colors = backgroundColor))
    ) {
        Spacer(modifier = Modifier.statusBarsPadding())
        Icon(imageVector = Icons.Rounded.ArrowBackIosNew, contentDescription = "back",
            modifier = Modifier
                .padding(20.dp)
                .size(20.dp)
                .clickable {
                    navController.navigate(QuizNavigation.HomeScreen.route) {
                        popUpTo(0)
                    }
                }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (userInfo.value.profilePictureUrl.isNotEmpty()) {
                AsyncImage(
                    model = userInfo.value.profilePictureUrl,
                    contentDescription = "profile picture",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.White, CircleShape)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.boy), contentDescription = null,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.White, CircleShape)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = userInfo.value.name, fontFamily = permanentMarker, fontSize = 26.sp)
            Spacer(modifier = Modifier.height(30.dp))
            Column {
                Text(
                    text = "Your Name",
                    fontFamily = permanentMarker,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 10.dp)
                )
                OutlinedTextField(
                    value = name.value,
                    onValueChange = { name.value = it },
                    trailingIcon = {
                        Icon(imageVector = Icons.Rounded.Person, contentDescription = null)
                    },
                    shape = RoundedCornerShape(30.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Column {
                Text(
                    text = "Your Email",
                    fontFamily = permanentMarker,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 10.dp)
                )
                OutlinedTextField(
                    value = email.value,
                    onValueChange = { email.value = it },
                    trailingIcon = {
                        Icon(imageVector = Icons.Rounded.Email, contentDescription = null)
                    },
                    shape = RoundedCornerShape(30.dp)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier.height(60.dp)) {
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
                                .background(Color(0xFFFFA800)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.flash),
                                contentDescription = null, modifier = Modifier
                                    .size(40.dp)
                                    .align(Alignment.Center),
                                tint = Color.White
                            )
                        }
                        Text(
                            text = userPoint.value.toString(),
                            fontFamily = poppins,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp,
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                }
                Button(onClick = {
                    actionType = "Reset"
                    showAlertDialog.value = true
                }, Modifier.height(60.dp)) {
                    Row {
                        Text(
                            text = "Reset",
                            fontFamily = permanentMarker,
                            color = Color.White,
                            fontSize = 20.sp,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Rounded.Restore,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    actionType = "Save"
                    showAlertDialog.value = true
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp)
            ) {
                Text(
                    text = "Save",
                    fontFamily = permanentMarker,
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    actionType = "Logout"
                    showAlertDialog.value = true
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp), colors = ButtonDefaults.buttonColors(Color.Red)
            ) {
                Text(
                    text = "Log out",
                    fontFamily = permanentMarker,
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun CommonAlertDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(text = title)
        },
        text = {
            Text(text = message)
        },
        confirmButton = {
            Button(onClick = {
                onConfirm()
            }) {
                Text(text = "Yes")
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismiss()
            }) {
                Text(text = "No")
            }
        }
    )
}
