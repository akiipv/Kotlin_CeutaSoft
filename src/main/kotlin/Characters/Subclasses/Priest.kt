package org.example.Characters.Subclasses

import org.example.Characters.Stats
import org.example.Combat.Attack
import org.example.Equipment.Armor
import org.example.Equipment.Heirloom
import org.example.Equipment.Weapon
import org.example.GameDataManagement.CombatLogManager
import java.util.*
import kotlin.math.roundToInt
import kotlin.random.Random

class Priest(
    name: String = "",
    race: String = "",
    lvl: Int = 0,
    stats: Stats = Stats(),
    isDefending: Boolean = false,
    isCPU: Boolean = false,
    weapon: Weapon? = null,
    armor: HashMap<String, Armor> = HashMap(),
    heirlooms: ArrayList<Heirloom> = ArrayList()
) : Faithful(name, race, lvl, stats, isDefending, isCPU, weapon, armor, heirlooms) {

    constructor(other: Priest) : this(
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

    override fun getClassName(): String = "Priest"

    override fun onLevelUp() {
        val r = Random
        if (r.nextInt(0, 100) >= 80) stats.hp += (stats.hp * 0.15).roundToInt()
        if (r.nextInt(0, 100) >= 90) stats.atk += 1
        if (r.nextInt(0, 100) >= 80) stats.arm += 2
        if (r.nextInt(0, 100) >= 50) stats.spd += 1
        if (r.nextInt(0, 100) >= 20) stats.res += 2
        if (r.nextInt(0, 100) >= 20) stats.fth += 4
    }

    override fun initializeStats(): Stats = Stats(75, 5, 5, 10, 15, 0, 20)
    override fun isWeaponValid(w: Weapon): Boolean = w.type == "Staff"
    override fun isArmorValid(a: Armor): Boolean = a.type == "Cloth"

    override fun castMiracle(): Attack {
        println("\nWhat do you want to cast?")
        println("\t1. Heal - Cure some of your target's wounds.")
        println("\t2. Holy prayer - Partially restores all of your party members.")
        println("\t3. Divine punishment - Ask your god to smite your enemies in a blast of light.")
        print("⟢ ")
        return when (readLine()?.trim()) {
            "1" -> {
                CombatLogManager.out("\n$name prays: Heal!")
                Attack(0, "STA")
            }
            "2" -> {
                CombatLogManager.out("\n$name prays: Holy prayer!")
                Attack(0, "STA")
            }
            "3" -> {
                CombatLogManager.out("\n$name prays: Divine punishment!")
                Attack((stats.fth * 0.55).roundToInt(), "STA")
            } else -> throw IllegalArgumentException("Choose a valid miracle.")
        }
    }
}