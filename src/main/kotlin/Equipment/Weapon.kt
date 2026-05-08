package org.example.Equipment

import org.example.Characters.Stats
import kotlin.collections.setOf

class Weapon(
    name: String = "",
    equipmentStats: Stats = Stats(),
    rarity: String = "",
    value: Int = -1,
    type: String = "",
    hands: Int = -1
) : Equipment(name, equipmentStats, rarity, value) {

    constructor(other: Weapon) : this(
        other.name,
        other.equipmentStats.copy(),
        other.rarity,
        other.value,
        other.type,
        other.hands
    )

    var type: String = type
        set(value) {
            require(
                value.lowercase() in setOf(
                    "sword", "mace", "axe", "scepter", "dagger", "greatsword", "hammer", "bow", "staff"
                )
            ) { "Weapon type not admited." }
            field = value
        }

    var hands: Int = hands
        get() = when (type) {
            "sword", "mace", "axe", "scepter", "dagger" -> 1
            "greatsword", "hammer", "bow", "staff" -> 2
            else -> 0
        }

    fun equals(other: Weapon): Boolean =
        super.equals(other) &&
                this.type == other.type &&
                this.hands == other.hands

    override fun toString(): String = super.toString() +
            "\n\t\t·Type﹕ $type" +
            "\n\t\t·Hands﹕ $hands"

    override fun getEquipmentType(): String = "Weapon"
    override fun getEquipmentSpecs(): String = "Type﹕ $type"
}