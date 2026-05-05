package org.example.Characters

import org.example.Characters.Subclasses.Faithful
import org.example.Equipment.*

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
            field = Stats(value)
        }

    constructor(other: Character) : this(
        other.name,
        other.race,
        other.lvl,
        Stats(other.stats),
        other.isDefending,
        other.isCPU,
        other.weapon,
        other.armor,
        other.heirlooms
        )
    }

    fun isDead(): Boolean {
        return stats.hp < 0 // todo -> nose pq da errorrrr
    }
}