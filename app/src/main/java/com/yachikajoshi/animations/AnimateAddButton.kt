package com.yachikajoshi.animations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yachikajoshi.animations.ui.theme.AnimationsTheme

class AnimateAddButton : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimationsTheme {
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    AddButton()
                }
            }
        }
    }
}

@Composable
fun AddButton() {
    var count by remember {
        mutableStateOf(0)
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(
            count = count,
            onAdd = { count += 1 },
            onSubtract = { count -= 1 }
        )
    }
}

val Green1 = Color(0xFF1BA672)
val SlateA15 = Color(0x2602060C)
val SlateA45 = Color(0x7302060C)

val basisFontFamily = FontFamily(
    Font(R.font.basis_grotesque_pro_black, FontWeight.Black),
    Font(R.font.basis_grotesque_pro_medium, FontWeight.Medium),
    Font(R.font.basis_grotesque_pro_regular, FontWeight.Normal)
)

val buttonTextStyle = TextStyle(
    fontFamily = basisFontFamily,
    fontWeight = FontWeight.Black, fontSize = 18.sp,
    lineHeight = 24.sp
)
val buttonTextStyleSmall = TextStyle(
    fontFamily = basisFontFamily,
    fontWeight = FontWeight.Black, fontSize = 14.sp,
    lineHeight = 17.sp
)
val helperTextStyle = TextStyle(
    fontFamily = basisFontFamily,
    fontWeight = FontWeight.Normal, fontSize = 13.sp,
    lineHeight = 16.sp
)

data class ButtonSize(
    val textStyle: TextStyle = buttonTextStyle,
    val width: Dp = 120.dp,
    val height: Dp = 40.dp,
    val iconSize: Dp = 24.dp,
    val buttonHorizontalPadding: Dp = 16.dp
)

val buttonSize = ButtonSize()

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Button(
    modifier: Modifier = Modifier,
    count: Int,
    onAdd: () -> Unit,
    onSubtract: () -> Unit,
    helperText: String? = null
) {

    Column() {

        OutlinedButton(
            onClick = {
                if (count == 0) {
                    onAdd()
                }
            },
            modifier = Modifier
                .requiredSize(width = buttonSize.width, height = buttonSize.height)
                .then(modifier),
            border = BorderStroke(1.dp, SlateA15),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Green1),
            elevation = ButtonDefaults.elevation(defaultElevation = 4.dp),
            contentPadding = PaddingValues(
                vertical = 0.dp,
                horizontal = buttonSize.buttonHorizontalPadding
            )
        ) {
            SubtractIcon(onSubtract, buttonSize, count)
            AnimatedContent(
                modifier = Modifier.weight(1f),
                targetState = count,
                transitionSpec = {
                    // Compare the incoming number with the previous number.
                    if (targetState > initialState) {
                        // If the target number is larger, it slides up and fades in
                        // while the initial (smaller) number slides up and fades out.
                        slideInVertically { height -> height } + fadeIn() with
                                slideOutVertically { height -> -height } + fadeOut()
                    } else {
                        // If the target number is smaller, it slides down and fades in
                        // while the initial number slides down and fades out.
                        slideInVertically { height -> -height } + fadeIn() with
                                slideOutVertically { height -> height } + fadeOut()
                    }.using(
                        // Disable clipping since the faded slide-in/out should
                        // be displayed out of bounds.
                        SizeTransform(clip = false)
                    )
                }
            ) { targetCount ->
                val content = if (targetCount == 0) {
                    "Add"
                } else {
                    targetCount.toString()
                }
                Text(
                    text = content.uppercase(),
                    style = buttonSize.textStyle,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }

            AddIcon(onAdd, buttonSize, count)

        }
        if (helperText.isNullOrBlank().not()) {
            Text(
                text = helperText.toString(),
                style = helperTextStyle,
                color = SlateA45,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 2.dp)
            )
        }
    }
}

@Composable
private fun AddIcon(onAdd: () -> Unit, buttonSize: ButtonSize, count: Int) {
    AnimatedVisibility(
        visible = count != 0,
        enter = slideInHorizontally(
            initialOffsetX = { it }, // it == fullWidth
            animationSpec = tween(
                durationMillis = 200,
                easing = LinearEasing
            )
        ) + fadeIn(),
        exit = slideOutHorizontally(
            targetOffsetX = { it },
            animationSpec = tween(
                durationMillis = 200,
                easing = LinearEasing
            )
        ) + fadeOut()
    ) {

        Icon(
            painterResource(id = R.drawable.ic_add_large),
            contentDescription = "",
            modifier = Modifier
                .clickable { onAdd() }
                .size(buttonSize.iconSize)
                .fillMaxHeight()
        )
    }
}

@Composable
private fun SubtractIcon(
    onSubtract: () -> Unit,
    buttonSize: ButtonSize,
    count: Int
) {

    AnimatedVisibility(
        visible = count != 0,
        enter = slideInHorizontally() + fadeIn(),
        exit = slideOutHorizontally() + fadeOut()
    ) {
        Icon(
            painterResource(id = R.drawable.ic_remove_large),
            contentDescription = "",
            modifier = Modifier
                .clickable { onSubtract() }
                .size(buttonSize.iconSize)
        )
    }

}

@Preview(showBackground = true)
@Composable
fun AddButtonPreview() {
    Box(Modifier.padding(16.dp)) {
        var count by remember {
            mutableStateOf(0)
        }
        Button(count = count, onAdd = { count += 1 }, onSubtract = { count -= 1 })
    }
}

@Preview(showBackground = true)
@Composable
fun AddButtonPreview_WithContent() {
    Box(Modifier.padding(16.dp)) {

        Button(
            count = 1,
            onAdd = { },
            onSubtract = { }
        )

    }
}