package org.example.Characters

import kotlin.math.roundToInt

data class Stats(
    var hp: Int = 0,
    var atk: Int = 0,
    var arm: Int = 0,
    var spd: Int = 0,
    var res: Int = 0,
    var mag: Int = 0,
    var fth: Int = 0
) {

    fun receiveDmg(dmg: Int) {
        this.hp -= dmg
        if (hp < 0) hp = 0
    }

    fun raiseGuard() {
        this.arm = (this.arm * 1.2).roundToInt()
        this.res = (this.res * 1.2).roundToInt()
    }

    fun dropGuard() {
        this.arm = (this.arm * 0.8).roundToInt()
        this.res = (this.res * 0.8).roundToInt()
    }

    fun getStat(stat: String): Int = when (stat.lowercase()) {
        "hp" -> hp
        "atk" -> atk
        "arm" -> arm
        "spd" -> spd
        "res" -> res
        "mag" -> mag
        "fth" -> fth
        else -> 0
    }

    override fun toString(): String = "·Stats﹕" +
                "\n\t\t·Health﹕ ${this.hp}" +
                "\n\t\t·Attack﹕ ${this.atk}" +
                "\n\t\t·Defense﹕ ${this.arm}" +
                "\n\t\t·Speed﹕ ${this.spd}" +
                "\n\t\t·Resistance﹕ ${this.res}"
}