package com.ali77gh.lime.data

import android.app.Activity
import com.ali.uneversaldatetools.date.JalaliDateTime
import com.ali77gh.lime.data.model.Routine
import com.ali77gh.lime.data.model.Task
import java.util.*
import kotlin.collections.ArrayList

class PlannerEngine(activity: Activity, private var notPlannedTasks: List<Task>) {

    companion object{
        private const val  CONST_15_MIN_MILIS = 15 * 60 * 1000
    }

    private val routines = Routine.getRepo(activity).getEnableRoutines()
    private val plannedTasks = Task.getRepo(activity).getFuturePlannedTasks()

    private val result = ArrayList<Task>()

    fun getPlannedTasks(): List<Task> {

        val sorted: ArrayList<Task> = notPlannedTasks as ArrayList<Task>
        sorted.sortBy { it.neededTimeInMilis } //SJF //TODO more complex


        var timeHead = System.currentTimeMillis()//TODO init value can be round :)  -> 00,15,30,45
        while (sorted.size!=0) {

            if (haveConflict(timeHead,sorted[0].neededTimeInMilis)){
                timeHead += CONST_15_MIN_MILIS// move head pointer 15 min forward
            }else{
                sorted[0].duoDate = timeHead // plan time for task
                timeHead += sorted[0].neededTimeInMilis// move head to end of planned task
                result.add(sorted[0])
                sorted.removeAt(0)
            }
        }

        return result
    }

    private fun haveConflict(start: Long, neededTime:Long):Boolean{

        if (haveRoutineConflict(start,neededTime)) return true
        if (haveTaskConflict(start,neededTime)) return true
        return false
    }

    //----------------------
    //routine conflict
    private fun haveRoutineConflict(start: Long, neededTime:Long):Boolean{

        val date= JalaliDateTime((start/1000).toInt(), TimeZone.getDefault())
        val milisecFromStartOfDay = (date.min*60*1000) + (date.hour*60*60*1000)

        for (i in getDayRoutines(start)){
            i!!
            if (    (i.routineTime>milisecFromStartOfDay)  &&  (milisecFromStartOfDay + neededTime > i.routineTime)  ) return true
            if (    (i.routineTime<milisecFromStartOfDay)  &&  (i.routineTime + i.neededTimeInMilis > milisecFromStartOfDay)) return true


        }
        return false
    }

    private fun getDayRoutines(milis: Long): List<Routine?> {
        val dayOfWeek = getDayOfWeek(milis)
        return routines.filter { it!!.routineDays.contains(dayOfWeek) }
    }

    private fun getDayOfWeek(milis: Long): Int {
        return JalaliDateTime((milis / 1000).toInt(), TimeZone.getDefault()).dayOfWeek.value
    }

    //----------------------
    //task conflict
    private fun haveTaskConflict(start: Long, neededTime:Long):Boolean{

        for (i in plannedTasks){

            i!!
            if (    (i.duoDate>start)  &&  (start + neededTime > i.duoDate)             ) return true
            if (    (i.duoDate<start)  &&  (i.duoDate + i.neededTimeInMilis > start)    ) return true

        }
        return false
    }
}