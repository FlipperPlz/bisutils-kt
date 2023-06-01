package com.flipperplz.bisutils.utils

enum class DayZScriptModule(val id: Int, val defsTitle: String) {
    ENGINE(1, "engineScriptModule"),
    GAME_LIB(2, "gameLibScriptModule"),
    GAME(3, "gameScriptModule"),
    WORLD(4, "worldScriptModule"),
    MISSION(5, "missionScriptModule"),
    WORKBENCH(6, "workbenchScriptModule");
}