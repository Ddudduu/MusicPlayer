package com.example.androidstudy

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.androidstudy.ui.theme.AndroidStudyTheme
import com.example.androidstudy.viewModel.MusicViewModel
import com.example.domain.entity.Music
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel = hiltViewModel<MusicViewModel>()

            AndroidStudyTheme {
                Column(
                    modifier = Modifier,
//                    color = MaterialTheme.colorScheme.background
                ) {
                    AppTitle("Music Player🎧")
//                    permissionButton(requestPermissionLauncher)
                    MusicList(viewModel)
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
fun MusicList(viewModel: MusicViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) { viewModel.getMusicList() }
    val musicList = viewModel.musicList.observeAsState(emptyList())
    LazyColumn {
        items(musicList.value.size) { index ->
            MusicItem(musicList.value[index])

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
                "${music.title}",
                color = Color.Black,
                fontWeight = FontWeight.W600
            )
            Text(
                "${music.artist}",
                color = Color.DarkGray,
                fontWeight = FontWeight.W300
            )
        }
    }
}

@Composable
fun permissionButton(permissionLauncher: ActivityResultLauncher<String>) {
    Column {
        Text("MP3 파일에 접근하려면 권한을 허용해주세요.")
        Button(onClick = {
            val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_AUDIO
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }
            permissionLauncher.launch(permission)
        }) {
            Text("권한 요청")
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