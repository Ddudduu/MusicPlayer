# Music Player Android App

## 1. Overview
Android Music Player using ExoPlayer
<br/><br/><br/>

## 2. Tech Stack
 *Android, Jetpack Compose, ExoPlayer, Multi module, Hilt, Coroutines, Coil, MediaStore*
<br/><br/><br/>

## 3. Multi Module Architecture
<img src = "https://private-user-images.githubusercontent.com/50603005/480131216-3e6bc094-f2ba-469e-9844-100ea144cadd.png?jwt=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NTkwNDc3NTEsIm5iZiI6MTc1OTA0NzQ1MSwicGF0aCI6Ii81MDYwMzAwNS80ODAxMzEyMTYtM2U2YmMwOTQtZjJiYS00NjllLTk4NDQtMTAwZWExNDRjYWRkLnBuZz9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPUFLSUFWQ09EWUxTQTUzUFFLNFpBJTJGMjAyNTA5MjglMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjUwOTI4VDA4MTczMVomWC1BbXotRXhwaXJlcz0zMDAmWC1BbXotU2lnbmF0dXJlPTQ0OGQxZWIxYWIzZGRkNGNmZmExODgyOGI1OWNkZDAyMjMzMDA4YmZjMGIxZWMyYzg3NzVhYjViZDVhMzFkZGUmWC1BbXotU2lnbmVkSGVhZGVycz1ob3N0In0.zH6gPhfWfygLjpODvlZoLPjVGAfBGNlITR3Yk9u5lAA" width="70%" height="30%">
 <br/>
 
| Module | Description |
|-------|-------|
| App | Activity, Screens: Entry point of the application |
| Domain | Entity, Repository Interface: Encapsulates business login |
| Data | Repository Implementation: Access mp3 file via MediaStore  |

<br/><br/>

## 4. Feature & Screenshots
- Display music list
<img src = "https://private-user-images.githubusercontent.com/50603005/480124087-1b07df06-dce4-459f-970f-d7c5206dd892.gif?jwt=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NTYxMzYwNTMsIm5iZiI6MTc1NjEzNTc1MywicGF0aCI6Ii81MDYwMzAwNS80ODAxMjQwODctMWIwN2RmMDYtZGNlNC00NTlmLTk3MGYtZDdjNTIwNmRkODkyLmdpZj9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPUFLSUFWQ09EWUxTQTUzUFFLNFpBJTJGMjAyNTA4MjUlMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjUwODI1VDE1MjkxM1omWC1BbXotRXhwaXJlcz0zMDAmWC1BbXotU2lnbmF0dXJlPWJkZGQ1N2I5OWI5YWQwNDE1NjA1OTM3ZjkxNzFmMWFiOTI3ODA1NmE0ZDc5NTUwYjQwNTAxNTc2OWY5YmU3NzUmWC1BbXotU2lnbmVkSGVhZGVycz1ob3N0In0.fLK6lt_eDfcJokzRF6vOlDXlXO6waA-ODzI6DacJeQM" width="30%" height="50%">
<br/><br/>

- Play music and navigate to detail screen
  - Play & Pause music  
  - Set position by slider
<img src = "https://private-user-images.githubusercontent.com/50603005/480125479-bc66d261-3910-44a7-a76c-35165b0400b4.gif?jwt=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NTYxMzYwNTMsIm5iZiI6MTc1NjEzNTc1MywicGF0aCI6Ii81MDYwMzAwNS80ODAxMjU0NzktYmM2NmQyNjEtMzkxMC00NGE3LWE3NmMtMzUxNjViMDQwMGI0LmdpZj9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPUFLSUFWQ09EWUxTQTUzUFFLNFpBJTJGMjAyNTA4MjUlMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjUwODI1VDE1MjkxM1omWC1BbXotRXhwaXJlcz0zMDAmWC1BbXotU2lnbmF0dXJlPWExNTU5ZDEwZWM2ZTNjODhlOWUwYTBmMzgzMDkxN2M1ZTE0ZGZmNDU0N2M3YmM1M2YzNjgzZjk5NGNhYjJjYjUmWC1BbXotU2lnbmVkSGVhZGVycz1ob3N0In0.HU-2vs7Al5AhwSHWfY8-NEGYAjkUj4r5zXjK_Pk3Krs" width="30%" height="50%">
<br/><br/>

- Play next & prev music
<img src = "https://private-user-images.githubusercontent.com/50603005/480124197-d0746e4c-d3a0-4573-b8de-17b9c7af751e.gif?jwt=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NTU3MDg2OTIsIm5iZiI6MTc1NTcwODM5MiwicGF0aCI6Ii81MDYwMzAwNS80ODAxMjQxOTctZDA3NDZlNGMtZDNhMC00NTczLWI4ZGUtMTdiOWM3YWY3NTFlLmdpZj9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPUFLSUFWQ09EWUxTQTUzUFFLNFpBJTJGMjAyNTA4MjAlMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjUwODIwVDE2NDYzMlomWC1BbXotRXhwaXJlcz0zMDAmWC1BbXotU2lnbmF0dXJlPThhMmY4NWNiMmJlYTA3ODQ0NTc2ZDM5MzNlOTk5YjkxYjM0MjI5ZTA4NDkxNTgyNTk0NDRiMWVmZTIxY2I0ODUmWC1BbXotU2lnbmVkSGVhZGVycz1ob3N0In0.mRQQTzgYZVH6G8NKDjgTJcEUzN9Ut2mH3miQDzPfh4o" width="30%" height="50%">
<br/><br/><br/>

## 5. Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/Ddudduu/MusicPlayer.git
   ```
2. Open in Android Studio
3. Build and run the app on an emulator or physical device
- Minimum SDK: 24
- Compile SDK: 35
- Target SDK: 34
