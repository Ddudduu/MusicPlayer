# Music Player Android App

## 1. Overview
Android Music Player using ExoPlayer
<br/><br/><br/>

## 2. Tech Stack
 *Android, Jetpack Compose, ExoPlayer, Multi module, Hilt, Coroutines, Coil, MediaStore*
<br/><br/><br/>

## 3. Multi Module Architecture
<img src = "https://i.ibb.co/Txwycf0k/MP3-Player.png" width="70%" height="30%">
 <br/>
 
| Module | Description |
|-------|-------|
| App | Activity, Screens: Entry point of the application |
| Domain | Entity, Repository Interface: Encapsulates business login |
| Data | Repository Implementation: Access mp3 file via MediaStore  |

<br/><br/>

## 4. Feature & Screenshots
- Display music list
<img src = "https://private-user-images.githubusercontent.com/50603005/480125479-bc66d261-3910-44a7-a76c-35165b0400b4.gif?jwt=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NjEwNTg0OTksIm5iZiI6MTc2MTA1ODE5OSwicGF0aCI6Ii81MDYwMzAwNS80ODAxMjU0NzktYmM2NmQyNjEtMzkxMC00NGE3LWE3NmMtMzUxNjViMDQwMGI0LmdpZj9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPUFLSUFWQ09EWUxTQTUzUFFLNFpBJTJGMjAyNTEwMjElMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjUxMDIxVDE0NDk1OVomWC1BbXotRXhwaXJlcz0zMDAmWC1BbXotU2lnbmF0dXJlPTdhM2Q3YjU5YTlkNTg2Y2ExNWE3MzI4ZGVmM2VjYWJlMTI0MWI2NGUzZDAzNzE2NDBkZDA0NTRlYTI2NjE0YjEmWC1BbXotU2lnbmVkSGVhZGVycz1ob3N0In0.jVPHQnATPcuS7Eek6FVYKbjCypEUqaAhyb5VlOjKDGU" width="30%" height="50%">
<br/><br/>

- Play music and navigate to detail screen
  - Play & Pause music  
  - Set position by slider
<img src = "https://private-user-images.githubusercontent.com/50603005/480125479-bc66d261-3910-44a7-a76c-35165b0400b4.gif?jwt=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NjEwNTg1NjIsIm5iZiI6MTc2MTA1ODI2MiwicGF0aCI6Ii81MDYwMzAwNS80ODAxMjU0NzktYmM2NmQyNjEtMzkxMC00NGE3LWE3NmMtMzUxNjViMDQwMGI0LmdpZj9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPUFLSUFWQ09EWUxTQTUzUFFLNFpBJTJGMjAyNTEwMjElMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjUxMDIxVDE0NTEwMVomWC1BbXotRXhwaXJlcz0zMDAmWC1BbXotU2lnbmF0dXJlPTg3MDI2NzM3ZGUxNTI5NWJiZjk5NDVhNzUxOGY0OTRmMjAzYzAzMWQyMDQwYzRhYmQxODc3ZmJmOWFmMzczMzgmWC1BbXotU2lnbmVkSGVhZGVycz1ob3N0In0.QhGMKsYyCDCiLRd4qzh0R16Dbrj6c0Ur5OSrAA3iJHg" width="30%" height="50%">
<br/><br/>

- Play next & prev music
<img src = "https://private-user-images.githubusercontent.com/50603005/480124197-d0746e4c-d3a0-4573-b8de-17b9c7af751e.gif?jwt=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NjEwNTg1NjIsIm5iZiI6MTc2MTA1ODI2MiwicGF0aCI6Ii81MDYwMzAwNS80ODAxMjQxOTctZDA3NDZlNGMtZDNhMC00NTczLWI4ZGUtMTdiOWM3YWY3NTFlLmdpZj9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPUFLSUFWQ09EWUxTQTUzUFFLNFpBJTJGMjAyNTEwMjElMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjUxMDIxVDE0NTEwMlomWC1BbXotRXhwaXJlcz0zMDAmWC1BbXotU2lnbmF0dXJlPTRlMTdhMWM5MTg0YTM0ODYyYWU5NGJiYTM5ODViNzM0ZDE0NDkzYzE2ZGVjZjQ3Y2MzMzRhNWQ2N2M2YzhmMGMmWC1BbXotU2lnbmVkSGVhZGVycz1ob3N0In0.Q8hCRKUiTJJXb6wMyBOA_8QtgAcSDCL1HIuI46saFIY" width="30%" height="50%">
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
