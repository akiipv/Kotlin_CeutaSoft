package org.example.GameMap

import org.example.Characters.Subclasses.Monster
import kotlin.random.Random

class Dungeon(
    var name: String = "",
    var level: Int = 0,
    var monsters: HashSet<Monster> = HashSet()
) {
    constructor(other: Dungeon) : this(
        other.name,
        other.level,
        HashSet<Monster>(other.monsters)
    )

    fun randomEncounter(): Monster {
        var ogEncounter: Monster = monsters.randomOrNull() ?: throw IllegalStateException("This dungeon has no monsters.")
        val encounter: Monster = Monster(ogEncounter)
        repeat(Random.nextInt(level - 3, level + 4)) {
            encounter.levelUp()
        }
        return encounter
    }

    fun randomFight(): ArrayList<Monster> {
        var encounters = ArrayList<Monster>()
        // TODO Investigar map, foreach, filter || funcion lambda
        (0..Random.nextInt(1, 4)).toList().forEach {
            encounters.add(randomEncounter())
        }
        return encounters
    }
}