package org.example.Characters.Subclasses

import org.example.Characters.Character
import org.example.Characters.Stats
import org.example.Combat.Attack
import org.example.Equipment.Armor
import org.example.Equipment.Heirloom
import org.example.Equipment.Weapon
import org.example.GameDataManagement.CombatLogManager
import java.util.ArrayList
import java.util.HashMap
import kotlin.math.roundToInt
import kotlin.random.Random

class Monster(
    name: String = "",
    race: String = "",
    lvl: Int = 0,
    stats: Stats = initializeStats(race),
    isDefending: Boolean = false,
    isCPU: Boolean = false,
    weapon: Weapon? = null,
    armor: HashMap<String, Armor> = HashMap(),
    heirlooms: ArrayList<Heirloom> = ArrayList()
) : Character(name, race, lvl, stats, isDefending, isCPU, weapon, armor, heirlooms) {

    companion object {
        fun initializeStats(race: String): Stats = when (race.lowercase()) {
            "beast" -> Stats(100, 12, 5, 12, 5)
            "undead" -> Stats(70, 10, 6, 2, 20)
            "giant" -> Stats(150, 10, 10, 2, 2)
            else -> throw java.lang.IllegalArgumentException("Race of the monster must be either Beast, Undead or Giant")
        }
    }

    constructor(other: Monster) : this(
        other.name,
        other.race,
        other.lvl,
        other.stats.copy(),
        other.isDefending,
        other.isCPU,
        other.weapon,
        HashMap(other.armor),
        ArrayList(other.heirlooms)
    )

    override fun attack(): Attack {
        CombatLogManager.out("\nThe $race $name attacks!")
        return super.attack()
    }

    override fun takeTurn(): Attack = attack()
    override fun getClassName(): String = "Monster"

    override fun onLevelUp() {
        val r = Random
        when (race.lowercase()) {
            "beast" -> {
                if (r.nextInt(0, 100) >= 50) stats.hp += (stats.hp * 0.1).roundToInt()
                if (r.nextInt(0, 100) >= 20) stats.atk += 1
                if (r.nextInt(0, 100) >= 65) stats.arm += 1
                if (r.nextInt(0, 100) >= 20) stats.spd += 1
                if (r.nextInt(0, 100) >= 85) stats.res += 1
            }
            "undead" -> {
                if (r.nextInt(0, 100) >= 70) stats.hp += (stats.hp * 0.1).roundToInt()
                if (r.nextInt(0, 100) >= 50) stats.atk += 1
                if (r.nextInt(0, 100) >= 70) stats.arm += 1
                if (r.nextInt(0, 100) >= 95) stats.spd += 1
                if (r.nextInt(0, 100) >= 30) stats.res += 4
            }
            "giant" -> {
                if (r.nextInt(0, 100) >= 50) stats.hp += (stats.hp * 0.2).roundToInt()
                else stats.hp += (stats.hp * 0.3).roundToInt()
                if (r.nextInt(0, 100) >= 50) stats.atk += 1
                if (r.nextInt(0, 100) >= 50) stats.arm += 1
                if (r.nextInt(0, 100) >= 90) stats.spd += 1
                if (r.nextInt(0, 100) >= 90) stats.res += 1
            } else -> throw IllegalArgumentException("Race of the monster must be either Beast, Undead or Giant")
        }
    }

    override fun displaySpecialAction(): String = ""
    override fun performSpecialAction(): Attack = Attack(0, "STA")
    override fun isWeaponValid(w: Weapon): Boolean = race.lowercase() == "undead"
    override fun isArmorValid(a: Armor): Boolean = race.lowercase() == "giant"
}