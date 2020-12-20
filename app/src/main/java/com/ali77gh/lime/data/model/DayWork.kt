package com.ali77gh.lime.data.model

interface DayWork {

    fun getStartTime():Int // milis from start of day
    fun getNeededTime():Int // needed time in milis
    fun getStartTimeString():String
    fun getEndTimeString():String
    fun getWorkName():String
}