package com.ali77gh.lime.data

import android.content.Context
import com.example.easyrepolib.sqlite.GenericDAO
import com.example.easyrepolib.sqlite.Model

class Event(var name: String,
            var type: String,
            var goal: Double = -1.0,
            var tags: List<String> ) : Model {

    //name is primary key ;)
    private var id :String? = null;
    override fun setId(s: String) { name = s }
    override fun getId(): String { return name }

    fun haveGoal(): Boolean { return goal != -1.0; }


    class Repo(context: Context?) :
        GenericDAO<Event?>(context, Event::class.java, "events", true) {

        //custom queries here
        fun customQuerySample(): List<Event?> {
            return getWithCondition { p0 -> (p0 as Event).name == "felan"; }
        }
    }

    // repo singleton
    companion object{
        private var repo: Repo? = null
        fun getRepo(context:Context) :Repo{
            if (repo==null) repo = Repo(context)
            return repo!!
        }
    }

    object EventType{
        val COUNT_BASE = "cb"
        val TIME_BASE = "tb"
        val VALUE_BASE = "vb"
    }
}