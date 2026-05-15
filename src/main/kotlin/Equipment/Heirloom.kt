package org.example.Equipment

import org.example.Characters.Stats

class Heirloom(
    name: String = "Heirloom",
    equipmentStats: Stats = Stats(),
    rarity: String = "Common",
    value: Int = 1,
    type: String = "Ring"
) : Equipment(name, equipmentStats, rarity, value) {

    constructor(other: Heirloom) : this(
        other.name,
        other.equipmentStats.copy(),
        other.rarity,
        other.value,
        other.type
    )

    var type: String = type
        set(value) {
            require(value.lowercase() in setOf("ring", "amulet")) {"Heirlooms can only be rings or amulets."}
            field = value
        }

    fun equals(other: Heirloom): Boolean =
        super.equals(other) &&
                this.type == other.type

    override fun toString(): String = super.toString() +
            "\n\t\t·Type﹕ $type"

    override fun getEquipmentType(): String = "Armor"
    override fun getEquipmentSpecs(): String = "Type﹕ $type"
}