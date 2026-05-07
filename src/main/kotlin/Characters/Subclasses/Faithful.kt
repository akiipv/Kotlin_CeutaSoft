package org.example.Characters.Subclasses

import org.example.Characters.Character
import org.example.Characters.Stats
import org.example.Combat.Attack
import org.example.Equipment.Armor
import org.example.Equipment.Heirloom
import org.example.Equipment.Weapon
import java.util.ArrayList
import java.util.HashMap

abstract class Faithful(
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

    constructor(other: Faithful) : this(
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

    override fun displaySpecialAction(): String = "2. Pray for a miracle."
    override fun performSpecialAction(): Attack {
        //
        return castMiracle()
    }
    protected abstract fun castMiracle(): Attack
}