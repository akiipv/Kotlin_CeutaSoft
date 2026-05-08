package org.example.Characters.Subclasses

import org.example.Characters.Character
import org.example.Characters.Stats
import org.example.Combat.Attack
import org.example.Equipment.Armor
import org.example.Equipment.Heirloom
import org.example.Equipment.Weapon
import java.util.ArrayList
import java.util.HashMap
import kotlin.math.roundToInt
import kotlin.random.Random

class Thief(
    name: String = "",
    race: String = "",
    lvl: Int = 0,
    stats: Stats = Stats(),
    isDefending: Boolean = false,
    isCPU: Boolean = false,
    weapon: Weapon? = null,
    armor: HashMap<String, Armor> = HashMap(),
    heirlooms: ArrayList<Heirloom> = ArrayList()
) : Character(name, race, lvl, stats, isDefending, isCPU, weapon, armor, heirlooms) {

    constructor(other: Thief) : this(
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

    fun getClassName(): String = "Thief"

    override fun onLevelUp() {
        val r = Random
        if (r.nextInt(0, 100) >= 60) stats.hp += (stats.hp * 0.1).roundToInt()
        if (r.nextInt(0, 100) >= 40) stats.atk += 1
        if (r.nextInt(0, 100) >= 60) stats.arm += 1
        if (r.nextInt(0, 100) >= 15) stats.spd += 2
        if (r.nextInt(0, 100) >= 60) stats.res += 1
    }

    override fun initializeStats(): Stats = Stats(90, 12, 8, 15, 8)
    override fun displaySpecialAction(): String = "2. Steal."

    override fun performSpecialAction(): Attack {
        //
        return Attack(stats.spd, "MAG")
    }

    override fun isWeaponValid(w: Weapon): Boolean = w.type in setOf("Dagger", "Sword")
    override fun isArmorValid(a: Armor): Boolean = a.type == "Leather"
}