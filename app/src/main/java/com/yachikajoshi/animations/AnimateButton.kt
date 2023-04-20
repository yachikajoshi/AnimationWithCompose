package com.yachikajoshi.animations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yachikajoshi.animations.ui.theme.AnimationsTheme
import kotlinx.coroutines.delay

class AnimateButton : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimationsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    Button()
                }
            }
        }
    }
}

val HanBlue = Color(0xFF5972d9)
val PaoloVeroneseGreen = Color(0xFF099176)
val Turquoise = Color(0xFF24e0bb)

val poppinsFont = FontFamily(
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semi_bold, FontWeight.SemiBold),
    Font(R.font.poppins_bold, FontWeight.Bold)
)

enum class ButtonState {
    DEFAULT, LOADING, DONE
}

@Preview(showBackground = true)
@Composable
fun DefaultButton(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.White, RoundedCornerShape(20))
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = "Download",
            fontFamily = poppinsFont,
            color = Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingButton(modifier: Modifier = Modifier, onLoadingUpdate: () -> Unit = {}) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = PaoloVeroneseGreen, RoundedCornerShape(20))
            .padding(top = 10.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_download),
                    contentDescription = "",
                    tint = Color.White,
                    modifier = Modifier.height(22.dp).align(Alignment.Top)
                )
                Text(
                    modifier = Modifier
                        .height(24.dp)
                        .padding(start = 5.dp)
                        .align(Alignment.Bottom),
                    textAlign = TextAlign.Center,
                    style = TextStyle(lineHeight = 1.sp),
                    text = "Loading",
                    fontFamily = poppinsFont,
                    color = Color.White
                )
            }
            var animatedProgress by remember {
                mutableStateOf(0f)
            }
            LaunchedEffect(Unit) {
                repeat(100) {
                    delay(50)
                    animatedProgress = (it + 1) / 100f
                }
                if (animatedProgress == 1f) {
                    onLoadingUpdate()
                }
            }
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, start = 3.dp, end = 4.dp)
                    .size(4.dp),
                color = Turquoise,
                progress = animatedProgress
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DoneButton(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = HanBlue, RoundedCornerShape(20))
            .padding(10.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_check_circle),
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier.height(22.dp).align(Alignment.Top)
            )
            Text(
                modifier = Modifier
                    .padding(start = 5.dp)
                    .align(Alignment.Bottom),
                textAlign = TextAlign.Justify,
                text = "Done",
                fontFamily = poppinsFont,
                color = Color.White
            )
        }
    }
}

@Preview
@Composable
fun Button() {
    var clickedState by remember {
        mutableStateOf(ButtonState.DEFAULT)
    }
    Card(
        Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .clip(RoundedCornerShape(20))
    ) {
        AnimatedVisibility(
            visible = clickedState == ButtonState.DEFAULT,
            enter = slideInVertically(),
            exit = slideOutVertically()
        ) {
            DefaultButton(Modifier.clickable { clickedState = ButtonState.LOADING })
        }
        AnimatedVisibility(
            visible = clickedState == ButtonState.LOADING,
            enter = slideInVertically(),
            exit = slideOutVertically()
        ) {
            LoadingButton(onLoadingUpdate = { clickedState = ButtonState.DONE })
        }
        AnimatedVisibility(
            visible = clickedState == ButtonState.DONE,
            enter = slideInVertically(),
            exit = slideOutVertically()
        ) {
            DoneButton(Modifier.clickable { clickedState = ButtonState.DEFAULT })
        }
    }
}