package com.ironpanthers.scouting.android

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ironpanthers.scouting.android.competitionList.CompetitionListActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.slf4j.LoggerFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_browse.setOnTouchListener { _, _ ->
            logger.info("browse button clicked")
            startActivity(Intent(this, CompetitionListActivity::class.java))
            true
        }

        btn_connect.setOnTouchListener { _, _ ->
            logger.info("connect button clicked")
            logger.warn("connect WIP")
            true
        }
    }

    companion object {
        val logger = LoggerFactory.getLogger(MainActivity::class.java)
    }
}
