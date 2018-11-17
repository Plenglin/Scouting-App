package com.ironpanthers.scouting.android.view

import android.os.Bundle
import android.app.Activity
import com.ironpanthers.scouting.android.R

import kotlinx.android.synthetic.main.activity_open_match.*

class OpenCompetitionActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_match)
    }

}
