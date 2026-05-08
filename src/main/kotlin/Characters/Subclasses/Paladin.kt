package org.example.Characters.Subclasses

import org.example.Characters.Stats
import org.example.Combat.Attack
import org.example.Equipment.Armor
import org.example.Equipment.Heirloom
import org.example.Equipment.Weapon
import java.util.*
import kotlin.math.roundToInt
import kotlin.random.Random

class Paladin(
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

    constructor(other: Paladin) : this(
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

    fun getClassName(): String = "Paladin"

    override fun onLevelUp() {
        val r = Random
        if (r.nextInt(0, 100) >= 50) stats.hp += (stats.hp * 0.15).roundToInt()
        if (r.nextInt(0, 100) >= 40) stats.atk += 1
        if (r.nextInt(0, 100) >= 30) stats.arm += 2
        if (r.nextInt(0, 100) >= 85) stats.spd += 1
        if (r.nextInt(0, 100) >= 50) stats.res += 1
        if (r.nextInt(0, 100) >= 70) stats.fth += 3
    }

    override fun initializeStats(): Stats = Stats(120, 10, 15, 5, 8, 0, 10)
    override fun isWeaponValid(w: Weapon): Boolean = w.type !in setOf("Bow", "Staff")
    override fun isArmorValid(a: Armor): Boolean = a.type == "Metal"

    override fun castMiracle(): Attack {
        val scan = Scanner(System.`in`)
        println("\n\t1. Imbue weapon - Coat your weapon with the holiness of your faith.")
        println("\t2. Bastion of faith - Protect yourself, faithfully.")
        println("\t3. Sacred blast - Blind your opponent with your god's splendor.")
        println("What do you want to cast?")
        return when (scan.nextLine()) {
            "1" -> {
                //
                Attack(0, "STA")
            }
            "2" -> {
                //
                Attack(0, "STA")
            }
            "3" -> {
                //
                Attack(0, "STA")
            } else -> throw IllegalArgumentException("Choose a valid miracle.")
        }
    }
}