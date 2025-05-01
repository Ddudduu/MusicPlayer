package com.example.androidstudy

import android.Manifest
import android.R
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.androidstudy.ui.theme.AndroidStudyTheme
import com.example.androidstudy.viewModel.MusicViewModel
import com.example.domain.entity.Music
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AndroidStudyTheme {
                Column(
                    modifier = Modifier,
//                    color = MaterialTheme.colorScheme.background
                ) {
                    AppTitle("Music Player🎧")
                    MusicScreen()
                }
            }
        }
    }
}

@Composable
fun RequestPermissionOnLaunch(
    // 권한 허용 시 호출할 콜백
    onPermissionGranted: () -> Unit
) {
    val context = LocalContext.current

    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(Manifest.permission.READ_MEDIA_AUDIO)
    } else {
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) { result ->
            val granted = result.values.all { it }
            if (granted) onPermissionGranted()
            else {
                val rationaleRequired = shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.READ_MEDIA_AUDIO
                )

                if (rationaleRequired) {
                    Toast.makeText(context, "Downloads 폴더에 접근하기 위해 권한이 필요합니다.", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(context, "설정에서 권한을 허용해주세요..", Toast.LENGTH_SHORT).show()
                }
            }
        }

    // 앱 시작 시 권한 확인
    // recomposition 시 반복 실행 방지
    LaunchedEffect(Unit) {
        val allGranted = permission.all {
            ContextCompat.checkSelfPermission(
                context,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }

        if (!allGranted) {
            permissionLauncher.launch(permission)
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

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MusicScreen() {
    // 현재 권한 상태
    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_AUDIO
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    var permissionState = rememberPermissionState(permission)
    RequestPermissionOnLaunch { }

    Log.i("=== current permission state ===", permissionState.status.toString())
    if (permissionState.status.isGranted) {
        MusicList()
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidStudyTheme {
        AppTitle("Music Player🎧")
    }
}