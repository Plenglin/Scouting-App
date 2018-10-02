package com.ironpanthers.scouting.common

import javafx.scene.input.KeyCode

object GameDef2018 : GameDef("Power Up", "2018", 0) {
    override fun upgrade(event: RobotEvent, from: Int, to: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    init {
        addEventDef("pick_up_floor", "Picked Up Cube from Floor", GameStage.ANY) {
            it.code == KeyCode.F
        }
        addEventDef("pick_up_exchange", "Picked Up Cube from Exchange", GameStage.ANY) {
            it.code == KeyCode.K
        }
        addEventDef("pick_up_portal", "Picked Up Cube from Portal", GameStage.ANY) {
            it.code == KeyCode.P
        }

        addEventDef("hit_vault", "Added Cube to Vault", GameStage.ANY) {
            it.code == KeyCode.V && !it.isShiftDown
        }
        addEventDef("hit_scale", "Added Cube to Scale", GameStage.ANY) {
            it.code == KeyCode.C && !it.isShiftDown
        }
        addEventDef("hit_switch", "Added Cube to Switch", GameStage.ANY){
            it.code == KeyCode.S && !it.isShiftDown
        }

        addEventDef("miss_vault", "Missed Cube at Vault", GameStage.ANY) {
            it.code == KeyCode.V && it.isShiftDown
        }
        addEventDef("miss_scale", "Missed Cube at Scale", GameStage.ANY) {
            it.code == KeyCode.C && it.isShiftDown
        }
        addEventDef("miss_switch", "Missed Cube at Switch", GameStage.ANY) {
            it.code == KeyCode.S && it.isShiftDown
        }

        addEventDef("baseline", "Passed Baseline", GameStage.AUTO, 1) {
            it.code == KeyCode.B
        }

        addEventDef("climb_fail", "Fail Climb", GameStage.ENDGAME, 1) {
            it.code == KeyCode.K && !it.isAltDown && it.isShiftDown
        }
        addEventDef("climb_self", "Self Climb", GameStage.ENDGAME, 1) {
            it.code == KeyCode.K && !it.isAltDown && !it.isShiftDown
        }
        addEventDef("climb_assist_1", "Climb Assist 1", GameStage.ENDGAME, 1) {
            it.code == KeyCode.K && it.isAltDown && !it.isShiftDown
        }
        addEventDef("climb_assist_2", "Climb Assist 2", GameStage.ENDGAME, 1) {
            it.code == KeyCode.K && it.isAltDown && it.isShiftDown
        }
        addEventDef("park", "Park", GameStage.ENDGAME, 1) {
            it.code == KeyCode.L
        }

        addEventDef("defense", "Defense", GameStage.ENDGAME) {
            it.code == KeyCode.D
        }
    }
}