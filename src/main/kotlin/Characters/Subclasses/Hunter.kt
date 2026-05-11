package org.example.Characters.Subclasses

import org.example.Characters.*
import org.example.Combat.Attack
import org.example.Equipment.*
import java.util.*
import kotlin.math.roundToInt
import kotlin.random.Random

class Hunter(
    name: String = "",
    race: String = "",
    lvl: Int = 0,
    stats: Stats = Stats(),
    isDefending: Boolean = false,
    isCPU: Boolean = false,
    weapon: Weapon? = null,
    armor: HashMap<String, Armor> = HashMap(),
    heirlooms: ArrayList<Heirloom> = ArrayList(),
    petName: String = "",
    petRace: String = "",
) : Character(name, race, lvl, stats, isDefending, isCPU, weapon, armor, heirlooms) {

    var pet: AnimalCompanion

    init {
        pet = AnimalCompanion(petName, petRace, isCPU = true)
    }

    constructor(other: Hunter) : this(
        other.name,
        other.race,
        other.lvl,
        other.stats.copy(),
        other.isDefending,
        other.isCPU,
        other.weapon,
        HashMap(other.armor),
        ArrayList(other.heirlooms),
        other.pet.name,
        other.pet.race,
    )

    override fun attack(): Attack {
        //
        return Attack(super.attack().dmgValue + pet.attack().dmgValue, "PHY")
    }

    override fun getClassName(): String = "Hunter"

    override fun equipHeirloom(heirloom: Heirloom): Boolean = if (super.equipHeirloom(heirloom))
        true
    else pet.equipHeirloom(heirloom)

    override fun onLevelUp() {
        val r = Random
        if (r.nextInt(0, 100) >= 60) stats.hp += (stats.hp * 0.1).roundToInt()
        if (r.nextInt(0, 100) >= 40) stats.atk += 1
        if (r.nextInt(0, 100) >= 60) stats.arm += 1
        if (r.nextInt(0, 100) >= 15) stats.spd += 2
        if (r.nextInt(0, 100) >= 60) stats.res += 1
    }

    override fun initializeStats(): Stats = Stats(100, 10, 10, 12, 10)
    override fun displaySpecialAction(): String = "2. Cheer on your pet."

    override fun performSpecialAction(): Attack {
        //
        return Attack(0, "STA")
    }

    override fun isWeaponValid(w: Weapon): Boolean = w.type in setOf("Sword", "Axe", "Dagger", "Bow")
    override fun isArmorValid(a: Armor): Boolean = a.type == "Leather"

    inner class AnimalCompanion(
        name: String = "",
        race: String = "",
        lvl: Int = 0,
        stats: Stats = Stats(),
        isCPU: Boolean = false
    ) : Character(name, race, lvl, stats, false, isCPU) {

        override fun equipHeirloom(heirloom: Heirloom): Boolean = if (heirloom.type != "Amulet")
            false
        else super.equipHeirloom(heirloom)

        override fun onLevelUp() {
            stats = updateStats()
        }

        override fun initializeStats(): Stats = updateStats()
        override fun displaySpecialAction(): String = ""
        override fun performSpecialAction(): Attack = Attack(0, "STA")
        override fun isWeaponValid(w: Weapon): Boolean = false
        override fun isArmorValid(a: Armor): Boolean = false

        fun updateStats(): Stats = when (race.lowercase()) {
            "canid" -> {
                var hunterStats = this@Hunter.stats
                Stats(
                    (hunterStats.hp * 0.2).roundToInt(),
                    (hunterStats.atk * 0.2).roundToInt(),
                    (hunterStats.arm * 0.2).roundToInt(),
                    (hunterStats.spd * 0.2).roundToInt(),
                    (hunterStats.res * 0.2).roundToInt()
                )
            }
            "feline" -> {
                var hunterStats = this@Hunter.stats
                Stats(
                    (hunterStats.hp * 0.15).roundToInt(),
                    (hunterStats.atk * 0.3).roundToInt(),
                    (hunterStats.arm * 0.15).roundToInt(),
                    (hunterStats.spd * 0.3).roundToInt(),
                    (hunterStats.res * 0.15).roundToInt()
                )
            }
            "raptor" -> {
                var hunterStats = this@Hunter.stats
                Stats(
                    (hunterStats.hp * 0.05).roundToInt(),
                    (hunterStats.atk * 0.15).roundToInt(),
                    (hunterStats.arm * 0.05).roundToInt(),
                    (hunterStats.spd * 0.35).roundToInt(),
                    (hunterStats.res * 0.25).roundToInt()
                )
            }
            else -> throw IllegalArgumentException("The animal companion species must be either Canid, Feline or Raptor.")
        }
        override fun getClassName(): String = "Pet"
    }
}
