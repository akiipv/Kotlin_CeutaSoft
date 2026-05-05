package org.example.Characters

import kotlin.math.roundToInt

class Stats(
    var hp: Int = 0,
    var atk: Int = 0,
    var arm: Int = 0,
    var spd: Int = 0,
    var res: Int = 0,
    var mag: Int = 0,
    var fth: Int = 0
) {

    constructor(other: Stats) : this(
        other.hp,
        other.atk,
        other.arm,
        other.spd,
        other.res,
        other.mag,
        other.fth
    )

    fun raiseGuard() {
        this.arm = (this.arm * 1.2).roundToInt()
        this.res = (this.res * 1.2).roundToInt()
    }

    fun dropGuard() {
        this.arm = (this.arm * 0.8).roundToInt()
        this.res = (this.res * 0.8).roundToInt()
    }

    fun getStat(stat: String): Int {
        var statRequested: Int
        when (stat) {
            "hp" -> statRequested = hp
            "atk" -> statRequested = atk
            "arm" -> statRequested = arm
            "spd" -> statRequested = spd
            "res" -> statRequested = res
            "mag" -> statRequested = mag
            "fth" -> statRequested = fth
            else -> statRequested = 0
        }
        return statRequested
    }

    fun equals(other: Stats): Boolean {
        if (this.hp != other.hp) return false
        if (this.atk != other.atk) return false
        if (this.arm != other.arm) return false
        if (this.spd != other.spd) return false
        if (this.res != other.res) return false
        if (this.mag != other.mag) return false
        if (this.fth != other.fth) return false
        return true
    }

    override fun toString(): String {
        return "Stats﹕" +
                "\n\t·Health﹕ " + this.hp +
                "\n\t·Attack﹕ " + this.atk +
                "\n\t·Defense﹕ " + this.arm +
                "\n\t·Speed﹕ " + this.spd +
                "\n\t·Resistance﹕ " + this.res
    }
}