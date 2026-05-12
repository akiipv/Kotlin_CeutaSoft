package org.example.Characters

import org.example.Combat.Attack
import org.example.Equipment.Armor
import org.example.Equipment.Heirloom
import org.example.Equipment.Weapon
import org.example.GameDataManagement.CombatLogManager
import org.example.GameDataManagement.GameLogger
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

    constructor(characterSheet: File) : this(GameLogger.readCharacter(characterSheet))

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
            field = value.copy()
        }

    fun isDead(): Boolean = stats.hp <= 0

    fun levelUp() {
        lvl++
        onLevelUp()
    }

    open fun attack(): Attack {
        CombatLogManager.out("\n$name attacks!")
        return Attack(calculateTotalStat("ATK"), "PHY")
    }

    open fun defend(attack: Attack) {
        var totalDmg: Int = 0
        when (attack.dmgType) {
            "PHY" -> totalDmg = attack.dmgValue - calculateTotalStat("ARM")
            "MAG" -> totalDmg = attack.dmgValue - calculateTotalStat("RES")
            "STA" -> {}
            else -> throw IllegalArgumentException("Damage type must be PHY for physical, MAG for magical or STA for status changes.")
        }

        if (totalDmg <= 0 && !attack.dmgType.equals("STA")) CombatLogManager.out("\tIt doesn't even make a dent.")
        else if (!attack.dmgType.equals("STA")) {
            CombatLogManager.out("\t$name receives $totalDmg points of damage.")
            stats.receiveDmg(totalDmg)
        }
    }

    open fun takeTurn(): Attack {
        var attack: Attack
        if (!isCPU) {
            if (isDefending) {
                isDefending = false
                CombatLogManager.out("\n$name drops their guard and gets ready.")
                stats.dropGuard()
            }
            println("\n$name, what would you like to do? ੭")
            println("\t1. Attack.")
            println("\t" + displaySpecialAction())
            println("\t3. Defend.")
            println("\t4. Pass turn.")
            print("⟢ ")
            when (readLine()?.trim()) {
                "1" -> attack = attack()
                "2" -> attack = performSpecialAction()?: Attack(0, "STA")
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
        } else return attack()
    }

    fun compareToCharacterSheet(characterSheet: File) {
        var c: Character
        require(characterSheet.isFile && characterSheet.canRead()) { "Error. You must procure a valid character sheet." }
        c = GameLogger.readCharacter(characterSheet)
        if (c != this && c.name == this.name) {
            println("Character doesn't match with the character sheet provided. Updating character")
            updateCharacter(c)
        } else println("This character doesn't need updating.")
    }

    fun updateOnCombatWin(combatLog: File) {
        if (GameLogger.readCombatWin(this, combatLog)) levelUp()
    }

    fun equipWeapon(weapon: Weapon): Boolean {
        if (this.weapon != null) return false
        if (!isWeaponValid(weapon)) return false
        this.weapon = weapon
        return true
    }

    fun equipArmor(armor: Armor): Boolean {
        if (this.armor.size >= 6) return false
        if (this.armor.containsKey(armor.piece)) return false
        this.armor[armor.type] = armor
        return true
    }

    open fun equipHeirloom(heirloom: Heirloom): Boolean {
        if (!validateHeirlooms(heirloom)) return false
        heirlooms.add(heirloom)
        return true
    }

    fun calculateTotalStat(stat: String): Int {
        var totalStat: Int = stats.getStat(stat)
        if (stat in setOf("ARM", "RES", "HP")) {
            for (a in armor.values) {
                totalStat += a.equipmentStats.getStat(stat)
            }
        } else weapon?.let { totalStat += it.equipmentStats.getStat(stat) }
        for (h in heirlooms) totalStat += h.equipmentStats.getStat(stat)
        return totalStat
    }

    abstract fun getClassName(): String

    override fun toString(): String = "Character﹕" +
            "\n\t·Name﹕ $name" +
            "\n\t·Race﹕ $race" +
            "\n\t·Level﹕ $lvl" +
            "\n\t$stats" +
            printStatsIfValid() +
            "\n\t·Type﹕ ${isCPUString()}\n"

    fun equals(other: Character): Boolean = (
            this.name == other.name &&
                    this.race == other.race &&
                    this.lvl == other.lvl &&
                    this.stats.equals(stats) &&
                    this.isCPU == other.isCPU
            )

    override fun compareTo(other: Character): Int {
        var result = Integer.compare(other.stats.spd, this.stats.spd)
        if (result == 0) result = Integer.compare(other.lvl, this.lvl)
        return result
    }

    private fun isCPUString(): String = if (isCPU) "CPU" else "Player"

    protected abstract fun onLevelUp()
    protected abstract fun initializeStats(): Stats
    protected abstract fun displaySpecialAction(): String
    protected abstract fun performSpecialAction(): Attack?
    protected abstract fun isWeaponValid(w: Weapon): Boolean
    protected abstract fun isArmorValid(a: Armor): Boolean

    private fun printStatsIfValid(): String {
        var extraStats = ""
        if (stats.mag != 0) extraStats += "\n\t\t·Magic﹕ ${stats.mag}"
        if (stats.fth != 0) extraStats += "\n\t\t·Faith﹕ ${stats.fth}"
        return extraStats
    }

    private fun updateCharacter(update: Character) {
        this.race = update.race
        this.lvl = update.lvl
        this.stats = stats.copy()
        this.isCPU = update.isCPU
    }

    fun validateHeirlooms(heirloom: Heirloom): Boolean {
        var ringCount = 0
        var amuletFound = false
        heirlooms.forEach {
            if (it.type == "Ring") ringCount++
            if (it.type == "Amulet") amuletFound = true
        }
        if (heirloom.type == "Ring" && ringCount > 2) return false
        if (heirloom.type == "Amulet" && amuletFound) return false
        return true
    }
}