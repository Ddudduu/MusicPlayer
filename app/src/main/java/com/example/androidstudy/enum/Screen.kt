package com.example.androidstudy.enum

// navigate 에 사용할 인자
enum class Screen(val route: String) {
    // 음악 리스트 화면
    List("list_screen"),

    // 플레이어 상세 화면
    Player("player_screen"),

    Main("route_main")
}