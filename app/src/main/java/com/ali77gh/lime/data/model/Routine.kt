package com.ali77gh.lime.data.model

import android.content.Context
import com.ali.uneversaldatetools.date.JalaliDateTime
import com.example.easyrepolib.sqlite.GenericDAO
import com.example.easyrepolib.sqlite.Model
import java.util.*

class Routine(
    var name: String,
    var note: String,
    var tags: List<String>,
    var neededTimeInMilis: Long,//in milisec
    var eventId :String?=null,
    var enable :Boolean,

    var routineTime : Int,//milisec from start of day
    var routineDays: List<Int> // [0,1,2,3,4,5,6]

    ) :Model,DayWork {

    private var id: String? = null
    override fun setId(s: String) { id = s }
    override fun getId(): String { return this.id!! }

    class RoutineRepo(context: Context?) :
        GenericDAO<Routine?>(context, Routine::class.java, "routine", true) {

        //custom queries here

        fun getEnableRoutines(): List<Routine?> {
            return getWithCondition { p0 -> (p0 as Routine).enable; }
        }

//        fun getADayRoutines(dayOfWeek: Int):List<Routine?>{
//            return getWithCondition { p0 -> (p0 as Routine).routineDays.contains(dayOfWeek); }
//        }

        fun getADayEnableRoutines(dayOfWeek: Int):List<Routine?>{
            return getWithCondition { p0 -> (p0 as Routine).routineDays.contains(dayOfWeek) && p0.enable }
        }
    }

    companion object{
        private var repo: RoutineRepo? = null
        fun getRepo(context: Context) : RoutineRepo {
            if (repo ==null) repo = RoutineRepo(context)
            return repo!!
        }
    }


    override fun getStartTime(): Int = routineTime

    override fun getStartTimeString(): String {
        val date = JalaliDateTime(((routineTime+getFirstOfDayMilis())/1000).toInt(), TimeZone.getDefault())

        return "${date.hour}:${date.min}"
    }
    override fun getEndTimeString(): String {
        val date = JalaliDateTime((((routineTime+getFirstOfDayMilis())+neededTimeInMilis)/1000).toInt(), TimeZone.getDefault())

        return "${date.hour}:${date.min}"
    }

    override fun getNeededTime(): Int = neededTimeInMilis.toInt()

    override fun getWorkName(): String = name

    private fun getFirstOfDayMilis():Long{
        val now = JalaliDateTime.Now()
        return JalaliDateTime(now.year,now.month,now.day).toUnixTime().toLong() * 1000L
    }
}