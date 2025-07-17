package com.example.androidstudy.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.androidstudy.R
import com.example.androidstudy.ui.theme.AndroidStudyTheme
import com.example.androidstudy.viewModel.MusicViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(title: String, viewModel: MusicViewModel = hiltViewModel()) {
    val musicPos by viewModel.curPos.collectAsState()
    val duration by viewModel.duration.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()

    var userSliderPos by remember { mutableStateOf<Float?>(null) }
    val curMusicPos by remember(duration, musicPos) {
        derivedStateOf {
            if (duration > 0) {
                (musicPos / duration.toFloat()).coerceIn(0f, 1f)
            } else 0f
        }
    }

    // display userSliderPos when user drags
    // display curMusicPos when user doesn't drag
    val sliderValue = userSliderPos ?: curMusicPos

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFccc5b9))
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_panda_playing_guitar),
            contentDescription = "Default Image"
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text("$title", fontWeight = FontWeight.Medium, fontSize = 18.sp)
        }

        // music duration
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(end = 10.dp),
            horizontalArrangement = Arrangement.End
        ) {
            val timeText by viewModel.curPosDurationFormatted.collectAsState()
            Text(timeText, fontWeight = FontWeight.Medium, fontSize = 15.sp, color = Color.White)
        }

        MusicSlider(
            sliderValue = sliderValue,
            onValueChanged = { changedValue ->
                userSliderPos = changedValue
            },
            onValueChangedFinished = {
                userSliderPos?.let {
                    val moveMusicPos = (it * duration).toLong()
                    viewModel.seek(moveMusicPos)
                }
                userSliderPos = null
            })

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp),
                onClick = {},
                content = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_prev),
                        contentDescription = null
                    )
                })

            IconButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    if (isPlaying) viewModel.pauseMusic()
                    else viewModel.resumeMusic()
                },
                content = {
                    Image(
                        painter = if (isPlaying)
                            painterResource(id = R.drawable.ic_pause)
                        else painterResource(R.drawable.ic_play),
                        contentDescription = null
                    )
                })

            IconButton(
                modifier = Modifier
                    .weight(1f),
                onClick = { },
                content = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_next),
                        contentDescription = null
                    )
                })
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicSlider(
    sliderValue: Float,
    onValueChanged: (Float) -> Unit,
    onValueChangedFinished: () -> Unit,
) {
    Slider(
        value = sliderValue,
        valueRange = 0f..1f,

        // set by user changed value
        onValueChange = onValueChanged,
        // called when user ended dragging
        onValueChangeFinished = onValueChangedFinished,

        track = {
            CustomTrack(it.value)
        },

        thumb = {
            Canvas(modifier = Modifier.size(18.dp)) {
                drawCircle(color = Color.White)
            }
        },
        modifier = Modifier.padding(horizontal = 30.dp)
    )
}

@Composable
fun CustomTrack(sliderValue: Float) {
    val activeColor = Color.White
    val inactiveColor = Color(0xCCa7a8aa)
    val activeFraction = sliderValue.coerceIn(0f, 1f)
    Box(
        Modifier
            .fillMaxWidth()
    ) {
        Box(
            Modifier
                .fillMaxWidth(activeFraction)
                .align(Alignment.CenterStart)
                .height(5.dp)
                .background(activeColor, CircleShape)
        )

        Box(
            Modifier
                .fillMaxWidth(1f - activeFraction)
                .align(Alignment.CenterEnd)
                .height(4.dp)
                .background(inactiveColor, CircleShape)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DetailPreview() {
    AndroidStudyTheme {
        DetailScreen("Hans Zimmer")
    }
}