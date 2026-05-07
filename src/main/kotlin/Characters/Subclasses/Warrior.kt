package org.example.Characters.Subclasses

import org.example.Characters.Character
import org.example.Characters.Stats
import org.example.Combat.Attack
import org.example.Equipment.Armor
import org.example.Equipment.Heirloom
import org.example.Equipment.Weapon
import kotlin.math.roundToInt
import kotlin.random.Random

class Warrior(
    name: String = "",
    race: String = "",
    lvl: Int = 0,
    stats: Stats = Stats(),
    isDefending: Boolean = false,
    isCPU: Boolean = false,
    weapon: Weapon? = null,
    var extraWeapon: Weapon? = null,
    var fury: Boolean = false,
    armor: HashMap<String, Armor> = HashMap(),
    heirlooms: ArrayList<Heirloom> = ArrayList()
) : Character(name, race, lvl, stats, isDefending, isCPU, weapon, armor, heirlooms) {

    constructor(other: Warrior) : this(
        other.name,
        other.race,
        other.lvl,
        other.stats.copy(),
        other.isDefending,
        other.isCPU,
        other.weapon,
        other.extraWeapon,
        other.fury,
        other.armor,
        other.heirlooms
    )

    fun toggleFury() {
        if (fury)
            println("\n\t $name calms down and face their enemy.")
        else println("\n\t $name feels the RAGE!")
        fury = !fury
    }

    override fun attack(): Attack = if (fury) {
        //
        Attack((stats.atk * 2), "PHY")
    } else super.attack()

    override fun defend(attack: Attack) {
        var totalDmg = 0
        when (attack.dmgType) {
            "PHY" -> totalDmg = attack.dmgValue - (stats.arm / 2)
            "MAG" -> totalDmg = attack.dmgValue - (stats.res / 2)
            else -> throw IllegalArgumentException("Damage type must be PHY for physical or MAG for magical.")
        }
        if (totalDmg <= 0 && attack.dmgType != "STA") TODO() // CombatLogManager.out("\nIt doesn't even make a dent.")
        else if (attack.dmgType != "STA") {
            // CombatLogManager.out("\t $name receives " + totalDmg + " points of damage.")
            stats.receiveDmg(totalDmg)
        }
    }

    fun getClassName(): String = "Warrior"

    fun equipExtraWeapon(w: Weapon): Boolean = if (weapon?.hands != 1 || !isWeaponValid(w))
        false
    else {
        extraWeapon = Weapon(w)
        true
    }

    override fun onLevelUp() {
        val r = Random
        if (r.nextInt(0, 100) >= 20) stats.hp += (stats.hp * 0.1).roundToInt()
        if (r.nextInt(0, 100) >= 25) stats.atk += 2
        if (r.nextInt(0, 100) >= 25) stats.arm += 1
        if (r.nextInt(0, 100) >= 50) stats.spd += 1
        if (r.nextInt(0, 100) >= 80) stats.res += 1
    }

    override fun initializeStats(): Stats = Stats(120, 15, 10, 10, 5)
    override fun displaySpecialAction(): String = "2. Toggle fury."

    override fun performSpecialAction(): Attack {
        toggleFury()
        return Attack(0, "STA")
    }

    override fun isWeaponValid(w: Weapon): Boolean = w.type !in setOf("Scepter", "Bow", "Staff")
    override fun isArmorValid(a: Armor): Boolean = a.type == "Metal"
}