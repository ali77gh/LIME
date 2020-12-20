package com.ali77gh.lime.data.model

import android.content.Context
import com.ali.uneversaldatetools.date.JalaliDateTime
import com.ali77gh.lime.ui.activity.ReportActivity
import com.example.easyrepolib.sqlite.GenericDAO
import com.example.easyrepolib.sqlite.Model
import java.util.*

class Task(
    var name: String,
    var note: String,
    var tags: List<String>,
    var neededTimeInMinute: Int,
    var eventId :String?=null,

    var priority: Int,
    var deadLine: Long,
    var mentalDifficulty: Int,
    var physicalDifficulty: Int,
    var enteredTime: Long,

    var duoDate :Long=0 // 0 means program should plan this task and other numbers are unix timestamp
    ): Model,DayWork,Comparable<Task> {

    private var id :String? = null;
    override fun setId(s: String) { id = s }
    override fun getId(): String { return this.id!! }

    val haveFixedTime :Boolean
        get(){return duoDate!=0L}

    class TaskRepo(context: Context?) :
        GenericDAO<Task?>(context, Task::class.java, "task", true) {

        //custom queries here
        fun customQuerySample(): List<Task?> {
            return getWithCondition { p0 -> (p0 as EventLog.TimeBaseEventLog).isEnd; }
        }

        fun getADayTasks(dayBeginTimeStamp:Long):List<Task?>{
            val dayEnd :Long= dayBeginTimeStamp + ReportActivity.DAY_IN_MILIS
            return getWithCondition { p0 ->
                val task = (p0 as Task)
                return@getWithCondition dayBeginTimeStamp < task.duoDate&&task.duoDate < dayEnd
            }
        }

        fun getUnPlannedTasks():List<Task?>{
            return getWithCondition { p0 ->
                (p0 as Task).duoDate == 0L
            }
        }

        fun getFuturePlannedTasks():List<Task?>{
            val now  = System.currentTimeMillis()
            return getWithCondition { p0 ->
                (p0 as Task).duoDate>now
            }
        }

        fun updateAll(tasks:List<Task>){
            for (i in tasks) Update(i)
        }
    }

    companion object{
        private var repo: TaskRepo? = null
        fun getRepo(context:Context) : TaskRepo {
            if (repo ==null) repo = TaskRepo(context)
            return repo!!
        }
        //easier to init :)
        fun newFixedTaskInstance(
            name: String,
            note: String,
            tags: List<String>,
            neededTimeInMinute: Int,
            duoDate: Long,
            eventId: String?=null
        ): Task {
            return Task(name,note,tags,neededTimeInMinute,eventId,0,0,0,0,0,duoDate)
        }
    }

    /**
     * a negative number if it's less than [other]
     * its based on SJF algorithm in process management in OS
     */
    override fun compareTo(other: Task): Int {
        return if (neededTimeInMinute > other.neededTimeInMinute)-1 else 1
        //TODO use other things too
    }

    //DayWork overrides

    override fun getStartTime(): Int {
        val date = JalaliDateTime((duoDate/1000).toInt(), TimeZone.getDefault())

        return (date.hour*60*60*1000)+(date.min*60*1000)+(date.sec*1000)
    }
    override fun getStartTimeString(): String {
        val date = JalaliDateTime((duoDate/1000).toInt(), TimeZone.getDefault())

        return "${date.hour}:${date.min}"
    }
    override fun getEndTimeString(): String {
        val date = JalaliDateTime(((duoDate+neededTimeInMinute)/1000).toInt(), TimeZone.getDefault())

        return "${date.hour}:${date.min}"
    }

    override fun getNeededTime(): Int = neededTimeInMinute

    override fun getWorkName(): String = name


}