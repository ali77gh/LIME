package com.ali77gh.lime.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ali77gh.lime.R

class TaskFragment :Fragment(), Backable{

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val root :View = inflater.inflate(R.layout.fragment_task,container,false)

        return root;
    }


    override fun onBack(): Boolean {
        return false;
    }
}