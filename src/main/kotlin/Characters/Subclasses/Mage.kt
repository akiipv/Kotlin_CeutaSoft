package org.example.Characters.Subclasses

import org.example.Characters.*
import org.example.Combat.Attack
import org.example.Equipment.*
import java.util.*
import kotlin.math.roundToInt
import kotlin.random.Random

class Mage(
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

    constructor(other: Mage) : this(
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

    override fun getClassName(): String = "Mage"

    private fun castSpell(spell: String): Attack = when (spell) {
        "1" -> {
            //
            Attack((stats.mag * 0.7).roundToInt(), "STA")
        }
        "2" -> {
            //
            Attack(0, "STA")
        }
        "3" -> {
            //
            Attack((stats.mag * 0.3).roundToInt(), "STA")
        }
        "4" -> {
            //
            Attack(0, "STA")
        } else -> throw IllegalArgumentException("Choose a valid spell.")
    }


    override fun onLevelUp() {
        val r = Random
        if (r.nextInt(0, 100) >= 65) stats.hp += (stats.hp * 0.1).roundToInt()
        if (r.nextInt(0, 100) >= 85) stats.atk += 1
        if (r.nextInt(0, 100) >= 65) stats.arm += 1
        if (r.nextInt(0, 100) >= 35) stats.spd += 1
        if (r.nextInt(0, 100) >= 20) stats.res += 1
        if (r.nextInt(0, 100) >= 15) stats.mag += 3
    }

    override fun initializeStats(): Stats = Stats(90, 5, 8, 10, 15, 30, 0)

    override fun displaySpecialAction(): String = "2. Cast a spell"

    override fun performSpecialAction(): Attack {
        val scan = Scanner(System.`in`)
        println("\n\t1. Fireball - Blast your opponent with a fiery incandescent orb.")
        println("\t2. Arcane shield - Protect yourself, magely.")
        println("\t3. Zephyr - Strong wind blasts your opponent away.")
        println("\t4. Mind surge - Your magic improves your reaction speed.")
        println("What do you want to cast?")
        return castSpell(scan.nextLine())
    }

    override fun isWeaponValid(w: Weapon): Boolean = w.type in setOf("Staff", "Scepter")

    override fun isArmorValid(a: Armor): Boolean = a.type == "Cloth"
}