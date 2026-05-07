package org.example.Characters

import org.example.Combat.Attack
import org.example.Equipment.*
import java.io.File
import java.util.*

abstract class Character(
    name: String = "",
    race: String = "",
    lvl: Int = 0,
    stats: Stats = Stats(),
    var isDefending: Boolean = false,
    var isCPU: Boolean = false,
    var weapon: Weapon? = null,
    var armor: HashMap<String, Armor> = HashMap(),
    var heirlooms: ArrayList<Heirloom> = ArrayList()
) : Comparable<Character> {
    constructor(other: Character) : this(
        other.name,
        other.race,
        other.lvl,
        other.stats.copy(),
        other.isDefending,
        other.isCPU,
        other.weapon,
        other.armor,
        other.heirlooms
    )

    var name: String = name
        set(value) {
            require(value.isNotEmpty()) { "Name cannot be empty." }
            field = value.trim()
        }

    var race: String = race
        set(value) {
            require(value.isNotEmpty()) { "Race cannot be empty." }
            field = value.trim()
        }

    var lvl: Int = lvl
        set(value) {
            require(value > 0) { "Level must be greater than zero." }
            field = value
        }

    var stats: Stats = stats
        set(value) {
            field = stats.copy()
        }

    fun isDead(): Boolean = stats.hp < 0

    fun levelUp() {
        lvl++
        onLevelUp()
    }

    open fun attack(): Attack = Attack(calculateTotalStat("ATK"), "PHY")

    open fun defend(attack: Attack) {
        var totalDmg: Int = 0
        when (attack.dmgType) {
            "PHY" -> totalDmg = attack.dmgValue - calculateTotalStat("ARM")
            "MAG" -> totalDmg = attack.dmgValue + calculateTotalStat("RES")
            "STA" -> {}
            else -> throw IllegalArgumentException("Damage type must be PHY for physical, MAG for magical or STA for status changes.")
        }

        if (totalDmg <= 0 && !attack.dmgType.equals("STA"))
        // CombatLogManager. todo -> fokin vida
        else if (!attack.dmgType.equals("STA")) {
            // CombatLogManager
            stats.receiveDmg(totalDmg)
        }
    }

    open fun takeTurn(): Attack {
        var attack: Attack
        if (!isCPU) {
            val scan: Scanner = Scanner(System.`in`)
            if (isDefending) {
                isDefending = false
                // CombatLog
                stats.dropGuard()
            }
            println("\n\t1. Attack.")
            println("\t" + displaySpecialAction())
            println("\t3. Defend.")
            println("\t4. Pass turn.")
            print("What would you like to do? ")
            when (scan.nextLine()) {
                "1" -> attack = attack()
                "2" -> attack = performSpecialAction()!!
                "3" -> {
                    if (isDefending) {
                        isDefending = true
                        stats.raiseGuard()
                    }
                    println("$name raises its guard.\n")
                    attack = Attack(0, "STA")
                }
                "4" -> {
                    println("$name does nothing.\n")
                    attack = Attack(0, "STA")
                }
                else -> throw RuntimeException("Input a valid move")
            }
            return attack
        }
        else return Attack()
    }

    // todo -> Gamelogger
    fun compareToCharacterSheet(characterSheet: File) {
        var c: Character
        if (characterSheet.isFile && characterSheet.canRead()) {

        }
    }

    // todo -> para terminar estoy hay que tener algo hecho en armor :(
    fun calculateTotalStat(stat: String): Int {
        var totalStat: Int = stats.getStat(stat)
        if (stat in setOf("ARM", "RES", "HP")) {
            for (a in armor.values) {

            }
        }
        return TODO()
    }

    override fun compareTo(other: Character): Int {
        var result = Integer.compare(other.stats.spd, this.stats.spd)
        if (result == 0) result = Integer.compare(other.lvl, this.lvl)
        return result
    }

    open fun equipHeirloom(heirloom: Heirloom): Boolean {
        TODO()
    }

    protected abstract fun onLevelUp()
    protected abstract fun initializeStats(): Stats
    protected abstract fun displaySpecialAction(): String
    protected abstract fun performSpecialAction(): Attack?
    protected abstract fun isWeaponValid(w: Weapon): Boolean
    protected abstract fun isArmorValid(a: Armor): Boolean
}