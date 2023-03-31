package com.example.catch_mentor.search

/*
* 들어가야하는 것:
* TODO 그룹여부, 온라인여부, 성별, ★과목★(리스트), 지역(리스트)
* */

data class SearchInfo(
    var group: String = "",
    var online: String = "",
    var sex: String = "",
    var subject: MutableList<String>,
    var region: MutableList<String>) {
}