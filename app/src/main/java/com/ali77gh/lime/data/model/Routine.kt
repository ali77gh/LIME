package com.ali77gh.lime.data.model

import android.content.Context
import com.example.easyrepolib.sqlite.GenericDAO
import com.example.easyrepolib.sqlite.Model

class Routine(
    var name: String,
    var note: String,
    var tags: List<String>,
    var neededTimeInMinute: Int,//in milisec
    var eventId :String?=null,
    var enable :Boolean,

    var routineTime : Int,//milisec from start of day
    var routineDays: List<Int> // [0,1,2,3,4,5,6]

    ) :Model {

    private var id: String? = null
    override fun setId(s: String) { id = s }
    override fun getId(): String { return this.id!! }

    class RoutineRepo(context: Context?) :
        GenericDAO<Routine?>(context, Routine::class.java, "routine", true) {

        //custom queries here
        fun customQuerySample(): List<Routine?> {
            return getWithCondition { p0 -> (p0 as EventLog.TimeBaseEventLog).isEnd; }
        }
    }

    companion object{
        private var repo: RoutineRepo? = null
        fun getRepo(context: Context) : RoutineRepo {
            if (repo ==null) repo = RoutineRepo(context)
            return repo!!
        }
    }
}