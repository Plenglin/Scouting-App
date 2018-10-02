package com.ironpanthers.scouting.common

import com.ironpanthers.scouting.util.ALT
import com.ironpanthers.scouting.util.CTRL
import com.ironpanthers.scouting.util.KeyCombo
import com.ironpanthers.scouting.util.SHIFT
import javafx.scene.input.KeyCode

object GameDef2018 : GameDef("Power Up", "2018", 0) {
    override fun upgrade(event: RobotEvent, from: Int, to: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    init {
        addEventDef("baseline", "Passed Baseline", AUTO, KeyCombo(KeyCode.B))

        addEventDef("pick_up_floor", "Picked Up Cube from Floor", ANY, KeyCombo(KeyCode.F))
        addEventDef("pick_up_exchange", "Picked Up Cube from Exchange", ANY, KeyCombo(KeyCode.E))
        addEventDef("pick_up_portal", "Picked Up Cube from Portal", ANY, KeyCombo(KeyCode.P))

        addEventDef("hit_vault", "Added Cube to Vault", ANY, KeyCombo(KeyCode.V))
        addEventDef("hit_scale", "Added Cube to Scale", ANY, KeyCombo(KeyCode.C))
        addEventDef("hit_switch", "Added Cube to Switch", ANY, KeyCombo(KeyCode.W))

        addEventDef("miss_vault", "Missed Vault", ANY, KeyCombo(KeyCode.V, SHIFT))
        addEventDef("miss_scale", "Missed Scale", ANY, KeyCombo(KeyCode.C, SHIFT))
        addEventDef("miss_switch", "Missed Switch", ANY, KeyCombo(KeyCode.W, SHIFT))

        addEventDef("pushed_scale", "Pushed Cube Out of Scale", ANY, KeyCombo(KeyCode.C, ALT))
        addEventDef("pushed_switch", "Pushed Cube Out of Switch", ANY, KeyCombo(KeyCode.W, ALT))
        addEventDef("foul_scale", "Scale Foul", ANY, KeyCombo(KeyCode.C, CTRL or SHIFT))

        addEventDef("defense", "Defense", ENDGAME, KeyCombo(KeyCode.D))

        addEndState("climb_fail", "Attempted and Failed to Climb")
        addEndState("park", "Parked")
        addEndState("climb_self", "Self Climb")
        addEndState("climb_assist_1", "Climb Assist 1")
        addEndState("climb_assist_2", "Climb Assist 2")
    }
}