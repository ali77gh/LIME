package com.ali77gh.lime.data.model

import android.content.Context
import com.example.easyrepolib.sqlite.GenericDAO
import com.example.easyrepolib.sqlite.Model

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
    ): Model {

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
}