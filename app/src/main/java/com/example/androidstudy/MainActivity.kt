package com.example.androidstudy

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.androidstudy.enum.Screen
import com.example.androidstudy.ui.theme.AndroidStudyTheme
import com.example.androidstudy.viewModel.MusicViewModel
import com.example.domain.entity.Music
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AndroidStudyTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Navigation()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Main.route) {
        composable(route = Screen.Main.route) { MainScreen(navController) }
        composable(
            route = Screen.Player.route + "?title={title}",
            arguments = listOf(navArgument("title") {
                type = NavType.StringType
                nullable = true
            })
        ) { PlayerScreen(title = it.arguments?.getString("title").toString()) }
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
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
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

@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    // 현재 권한 상태
    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_AUDIO
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    var permissionState = rememberPermissionState(permission)
    RequestPermissionOnLaunch { }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "🎶Music Player🎧",
                        color = Color(0xff393E46),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 25.sp
                    )
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                if (permissionState.status.isGranted) {
                    MusicList(navController = navController)
                }
            }
        }
    )
    Log.i("=== current permission state ===", permissionState.status.toString())
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun MusicList(
    navController: NavController,
    viewModel: MusicViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) { viewModel.getMusicList() }
    val musicList = viewModel.musicList.collectAsState()
    LazyColumn {
        items(musicList.value.size) { index ->
            val music = musicList.value[index]
            MusicItem(music) {
                navController.navigate(Screen.Player.route + "?title=${music.title}")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun MusicItem(
    music: Music,
    onClick: (Music) -> Unit
) {
    val context = LocalContext.current
    val mediaMetaDataRetriever = MediaMetadataRetriever()
    mediaMetaDataRetriever.setDataSource(context, Uri.parse(music.musicUri))
    val picture = mediaMetaDataRetriever.embeddedPicture

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable { onClick(music) },
        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
    ) {
        Row(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier.size(50.dp),
                contentScale = ContentScale.Fit,
                model = picture?.let {
                    // ByteArray -> Bitmap
                    ImageRequest.Builder(context)
                        .data(BitmapFactory.decodeByteArray(it, 0, it.size)).crossfade(true)
                        .placeholder(R.drawable.img_music_default)
                        .error(R.drawable.img_music_default)
                        .listener(
                            onError = { _, result ->
                                Log.e("=== Coil loading error :", "${result.throwable}")
                            },
                            onSuccess = { _, result ->
                                Log.i("=== Coil loading success :", "===")
                            }
                        ).build()
                } ?: R.drawable.img_music_default,
                contentDescription = null,
                error = painterResource(R.drawable.img_music_default)
            )

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
    mediaMetaDataRetriever.release()
}

@Composable
fun PlayerScreen(title: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Title: $title")
//        Text("Artist: ${music.artist}")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidStudyTheme {
        AppTitle("Music Player🎧")
    }
}