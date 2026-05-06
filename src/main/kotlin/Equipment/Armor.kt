package org.example.Equipment

import org.example.Characters.Stats

class Armor(
    name: String = "",
    equipmentStats: Stats = Stats(),
    rarity: String = "",
    value: Int = -1,
    type: String = "",
    piece: String = ""
) : Equipment(name, equipmentStats, rarity, value) {

    constructor(other: Armor) : this(
        other.name,
        other.equipmentStats.copy(),
        other.rarity,
        other.value,
        other.type,
        other.piece
    )

    var type: String = type
        set(value) {
            require(value in setOf("cloth", "leather", "metal")) {"Armor can only be either cloth, leather or metal."}
            field = value
        }

    var piece: String = piece
        set(value) {
            require(value.lowercase() in setOf("helm", "pauldrons", "chestguard", "gauntlets", "greaves", "boots")) { "Armor pieces can only be helm, pauldrons, chestguard, gauntlets, greaves or boots." }
            field = value
        }

    fun equals(other: Armor): Boolean =
        super.equals(other) &&
        this.type == other.type &&
        this.piece == this.piece

    override fun toString(): String = super.toString() +
            "\n\t\t·Type﹕ $type" +
            "\n\t\t·Piece﹕ $piece"

    override fun getEquipmentType(): String = "Armor"
    override fun getEquipmentSpecs(): String = "Piece﹕ $piece\n" +
            "Armor type﹕ $type"
}