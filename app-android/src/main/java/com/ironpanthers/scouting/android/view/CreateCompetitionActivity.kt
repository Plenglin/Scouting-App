package com.ironpanthers.scouting.android.view

import android.os.Bundle
import android.app.Activity
import com.ironpanthers.scouting.android.R

import kotlinx.android.synthetic.main.activity_create_match.*

class CreateCompetitionActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_match)
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
