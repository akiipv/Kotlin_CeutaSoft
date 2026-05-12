package org.example.Combat

import org.example.Characters.*
import org.example.Characters.Subclasses.*
import org.example.Deco.CombatUI
import org.example.Equipment.*
import org.example.GameDataManagement.CombatLogManager
import java.io.*

class Combat() {
    companion object {
        val treasures: ArrayList<Equipment> = loadTreasures()

        fun combat(player: Character, CPU: Character): Equipment {
            println("₊˚ ‿︵‿︵‿︵୨୧ · · ♡ · · ୨୧‿︵‿︵‿︵ ˚₊")
            if (!CombatLogManager.hasLogStarted()) CombatLogManager.startLog("${player.name} vs ${CPU.name}")
            CombatLogManager.out("The showdown between ${player.name} and ${CPU.name}... BEGINS .ᐟ.ᐟ")
            var c1: Character
            var c2: Character

            if (checkFasterCharacter(player, CPU)) {
                c1 = player
                c2 = CPU
            }else {
                c2 = player
                c1 = CPU
            }

            while (!anybodyDead(c1, c2)) {
                if (doubleHit(c1, c2) && !anybodyDead(c1, c2)) {
                    c2.defend(c1.takeTurn())
                    visualizeHP(c1, c2)
                }
                if (!anybodyDead(c1, c2)) {
                    c2.defend(c1.takeTurn())
                    visualizeHP(c1, c2)
                }
                if (!anybodyDead(c1, c2)) {
                    c1.defend(c2.takeTurn())
                    visualizeHP(c1, c2)
                }
            }
            announceWinner(c1, c2)
            CombatLogManager.closeLog()
            return treasures.random()
        }

        fun groupCombat(players: ArrayList<Character>, CPU: ArrayList<Monster>): ArrayList<Equipment> {
            players.sortByDescending { it.lvl }
            CPU.sortByDescending { it.lvl }
            var playerLeader = players.first()
            var CPULeader = CPU.first()
            var prize = ArrayList<Equipment>()
            CombatLogManager.out("${playerLeader.name}'s party vs ${CPULeader.name}'s group.")
            while (players.isNotEmpty() && CPU.isNotEmpty()){
                var combatPrize: Equipment = combat(players.first(), CPU.first())
                if (CPU.first().isDead()) {
                    prize.add(combatPrize)
                    CPU.removeFirst()
                } else players.removeFirst()
            }
            if (players.isEmpty()) CombatLogManager.out("\n\t${playerLeader.getClassName()}'s party was wiped out from existence.")
            else CombatLogManager.out("\n\t${playerLeader.getClassName()}'s party was victorious!")
            return prize
        }

        fun visualizeHP(c1: Character, c2: Character) {
            println("\n" + CombatUI.hpDisplay(c1, c2))
        }

        private fun checkFasterCharacter(c1: Character, c2: Character): Boolean = c1.calculateTotalStat("SPD") >= c2.calculateTotalStat("SPD")
        private fun doubleHit(c1: Character, c2: Character): Boolean = c1.calculateTotalStat("SPD") >= (c2.calculateTotalStat("SPD") * 2)
        private fun anybodyDead(c1: Character, c2: Character): Boolean = c1.isDead() || c2.isDead()

        private fun announceWinner(c1: Character, c2: Character) {
            val winner = if (c1.isDead() && !c2.isDead()) c2 else c1
            val loser = if (c2.isDead() && !c1.isDead()) c2 else c1
            CombatLogManager.out("\n\t${winner.name} towers over ${loser.name} corpse.")
        }

        private fun loadTreasures(): ArrayList<Equipment> =
            arrayListOf<Equipment>().apply {
                addAll(loadWeapons())
                addAll(loadArmors())
                addAll(loadHeirlooms())
            }

        private fun loadWeapons(): ArrayList<Weapon> {
            val result = ArrayList<Weapon>()
            try {
                BufferedReader(FileReader("./DataFiles/Treasures/weapons.csv")).use { br ->
                    br.readLine()
                    br.forEachLine { line ->
                        val split = line.split(",")
                        var name = split[0].replace(",", "")

                        if (name.length > 20) name = name.take(20)
                        val w = Weapon(name, generateStatsFromString(split[3], 1), split[1], split[4].toInt(), split[2])
                        result.add(w)
                    }
                }
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
            return result
        }

        private fun loadArmors(): ArrayList<Armor> {
            val result = ArrayList<Armor>()
            try {
                BufferedReader(FileReader("./DataFiles/Treasures/armor.csv")).use { br ->
                    br.readLine()
                    br.forEachLine { line ->
                        val split = line.split(",")
                        var nombre = split[0]
                        if (nombre.length > 20) nombre = nombre.take(20)
                        val a = Armor(nombre, generateStatsFromString(split[4], 2), split[1], split[5].toInt(), split[3], split[2])
                        result.add(a)
                    }
                }
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
            return result
        }

        private fun loadHeirlooms(): ArrayList<Heirloom> {
            val result = ArrayList<Heirloom>()
            try {
                BufferedReader(FileReader("./DataFiles/Treasures/heirlooms.csv")).use { br ->
                    br.readLine()
                    br.forEachLine { line ->
                        val split = line.split(",")
                        var nombre = split[0]
                        if (nombre.length > 20) nombre = nombre.take(20)
                        val h = Heirloom(nombre, generateStatsFromString(split[3], 3), split[1], split[4].toInt(), split[2])
                        result.add(h)
                    }
                }
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
            return result
        }

        private fun generateStatsFromString(s: String, equipmentType: Int): Stats {
            val stats = Stats()
            val split = s.split("-")
            when (equipmentType) {
                1 -> {
                    stats.atk = split[0].toInt()
                    stats.spd = split[1].toInt()
                    stats.mag = split[2].toInt()
                    stats.fth = split[3].toInt()
                }
                2 -> {
                    stats.arm = split[0].toInt()
                    stats.res = split[1].toInt()
                    stats.hp = split[2].toInt()
                }
                3 -> {
                    stats.atk = split[0].toInt()
                    stats.spd = split[1].toInt()
                    stats.mag = split[2].toInt()
                    stats.fth = split[3].toInt()
                    stats.arm = split[4].toInt()
                    stats.res = split[5].toInt()
                    stats.hp = split[6].toInt()
                }
                else -> throw IllegalArgumentException("Invalid equipment type. Use 1 for Weapons, 2 for Armor and 3 for Heirlooms.")
            }
            return stats
        }
    }
}