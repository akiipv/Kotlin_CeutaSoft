package org.example.Equipment

import org.example.Characters.Stats

abstract class Equipment(
    name: String = "",
    var equipmentStats: Stats = Stats(),
    rarity: String = "",
    value: Int = -1
) {
    constructor(other: Equipment) : this(
        other.name,
        other.equipmentStats.copy(),
        other.rarity,
        other.value
    )

    var name: String = name
    set(value) {
        require(value.length < 20 && value.isNotEmpty()) { "Name can't be any longer than 20 characters." }
        field = value
    }

    var rarity: String = rarity
    set(value) {
        require(value.lowercase() in setOf("common", "rare", "epic", "legendary")) { "Rarity must be one of the following: common, rare, epic or legendary." }
        field = value
    }

    var value: Int = value
    set(value) {
        require(value > 1) { "Equipment value must be higher than 1." }
        field = value
    }

    fun equals(other: Equipment): Boolean =
        this.name == other.name &&
                this.equipmentStats == other.equipmentStats &&
                this.rarity == other.rarity &&
                this.value == other.value

    override fun toString(): String = getEquipmentType() + "﹕" +
            "\n\t·Name﹕ $name" +
            getEquipmentSpecs() +
            "\n\t\t·Rarity﹕ $rarity" +
            "\n\t\t·Value﹕ $value" +
            equipmentStats.toString()

    protected abstract fun getEquipmentType(): String
    protected abstract fun getEquipmentSpecs(): String
}