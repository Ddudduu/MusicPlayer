package com.example.androidstudy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidstudy.data.Music
import com.example.androidstudy.ui.theme.AndroidStudyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val musicList = listOf(
            Music("Come Over", "LE SSERAFIM"),
            Music("DENIAL IS A RIVER", "Doechii"),
            Music("Grapejuice", "Harry Styles")
        )

        setContent {
            AndroidStudyTheme {
                // A surface container using the 'background' color from the theme
                Column(
                    modifier = Modifier,
//                    color = MaterialTheme.colorScheme.background
                ) {
                    AppTitle("Music Player🎧")
                    MusicList(musics = musicList)
                }
            }
        }
    }
}

@Composable
fun AppTitle(name: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(15.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$name",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }

}

@Composable
fun MusicList(musics: List<Music>) {
    Column {
        musics.forEach { music ->
            MusicItem(music)
        }
    }
}

@Composable
fun MusicItem(
    music: Music
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                music.title,
                color = Color.Black,
                fontWeight = FontWeight.W600
            )
            Text(
                music.artist,
                color = Color.DarkGray,
                fontWeight = FontWeight.W300
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidStudyTheme {
        AppTitle("Music Player🎧")

    }
}

@Preview
@Composable
fun MusicListPreview() {
    val musics = listOf(
        Music("Come Over", "LE SSERAFIM"),
        Music("DENIAL IS A RIVER", "Doechii"),
        Music("Grapejuice", "Harry Styles")
    )

    Column {
        musics.forEach { music ->
            MusicItem(music)
        }
    }
}