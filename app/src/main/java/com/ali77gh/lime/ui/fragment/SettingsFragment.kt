package com.ali77gh.lime.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ali77gh.lime.BuildConfig
import com.ali77gh.lime.R
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment :Fragment(), Backable{

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setting_version.text = "version: " + BuildConfig.VERSION_NAME

//        setting_language.setOnClickListener{
//            Toast.makeText(activity,"coming soon...",Toast.LENGTH_SHORT).show()
//            //TODO
//        }
//        setting_date_system.setOnClickListener{
//            Toast.makeText(activity,"coming soon...",Toast.LENGTH_SHORT).show()
//            //TODO
//        }
        setting_about_developer.setOnClickListener{
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://ali77gh.ir")
                )
            )
        }

        setting_website.setOnClickListener {
            Toast.makeText(activity,"coming soon...",Toast.LENGTH_SHORT).show()
        }
    }


    override fun onBack(): Boolean {
        return false;
    }
}