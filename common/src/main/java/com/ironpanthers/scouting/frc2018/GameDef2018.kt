package com.ironpanthers.scouting.frc2018

import com.ironpanthers.scouting.common.ANY_GAME_PHASE
import com.ironpanthers.scouting.common.AUTO_ONLY
import com.ironpanthers.scouting.common.ENTIRE_TELEOP
import com.ironpanthers.scouting.common.GameDef
import com.ironpanthers.scouting.util.*

object GameDef2018 : GameDef("Power Up", "2018-power-up", 0) {

    init {
        addEventDef("baseline", "Passed Baseline", AUTO_ONLY, KeyCombo("B"))

        addEventDef("pick_up_floor", "Picked Up Cube from Floor", ANY_GAME_PHASE, KeyCombo("F"))
        addEventDef("pick_up_exchange", "Picked Up Cube from Exchange", ANY_GAME_PHASE, KeyCombo("E"))
        addEventDef("pick_up_portal", "Picked Up Cube from Portal", ANY_GAME_PHASE, KeyCombo("P"))

        addEventDef("drop", "Dropped Cube", ANY_GAME_PHASE, KeyCombo("P"))

        addEventDef("hit_vault", "Added Cube to Vault", ANY_GAME_PHASE, KeyCombo("V"), Color(0.0, 1.0, 0.0))
        addEventDef("hit_scale", "Added Cube to Scale", ANY_GAME_PHASE, KeyCombo("C"), Color(0.0, 1.0, 0.0))
        addEventDef("hit_switch", "Added Cube to Switch", ANY_GAME_PHASE, KeyCombo("W"), Color(0.0, 1.0, 0.0))

        addEventDef("miss_vault", "Missed Vault", ANY_GAME_PHASE, KeyCombo("V", SHIFT), Color(1.0, 1.0, 0.0))
        addEventDef("miss_scale", "Missed Scale", ANY_GAME_PHASE, KeyCombo("C", SHIFT))
        addEventDef("miss_switch", "Missed Switch", ANY_GAME_PHASE, KeyCombo("W", SHIFT))

        addEventDef("pushed_scale", "Pushed Cube Out of Scale", ANY_GAME_PHASE, KeyCombo("C", ALT))
        addEventDef("pushed_switch", "Pushed Cube Out of Switch", ANY_GAME_PHASE, KeyCombo("W", ALT))
        addEventDef("foul_scale", "Scale Foul", ANY_GAME_PHASE, KeyCombo("C", CTRL or SHIFT), Color(1.0, 0.0, 0.0))

        addEventDef("defense", "Defense", ENTIRE_TELEOP, KeyCombo("D"))

        addEndState("climb_fail", "Attempted and Failed to Climb")
        addEndState("park", "Parked")
        addEndState("climb_self", "Self Climb")
        addEndState("climb_assist_1", "Climb Assist 1")
        addEndState("climb_assist_2", "Climb Assist 2")
    }

}
