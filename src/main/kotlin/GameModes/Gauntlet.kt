package org.example.GameModes

import org.example.Characters.Character
import org.example.Characters.Subclasses.Monster
import org.example.Combat.Combat
import org.example.Equipment.Armor
import org.example.Equipment.Equipment
import org.example.Equipment.Heirloom
import org.example.Equipment.Weapon
import org.example.GameDataManagement.CombatLogManager
import org.example.GameMap.Dungeon
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

class Gauntlet {
    var dungeon = Dungeon()
    var playerParty = ArrayList<Character>()

    fun initGauntlet(dungeonFile: File, players: ArrayList<Character>) {
        loadDungeon(dungeonFile)
        playerParty = ArrayList(players)
    }

    fun playGauntlet() {
        var remainingCombats = 10
        while (remainingCombats > 0 && playerParty.isNotEmpty()) {
            var combatPrize = Combat.groupCombat(playerParty, dungeon.randomFight())
            combatPrize.forEach {
                var indexOfPlayer = 0
                while (indexOfPlayer < playerParty.size && !equipPrize(playerParty[indexOfPlayer], it)) {
                    indexOfPlayer++
                }
            }
            remainingCombats--
        }
        if (playerParty.isNotEmpty()) CombatLogManager.out("Congratulations! You defeated the gauntlet and triumphed over your quest!")
        else CombatLogManager.out("The adventurers died an ignominious death...")
    }

    private fun loadDungeon(dungeonFile: File) {
        var dungeonName: String
        var dungeonLevel: Int
        require(dungeonFile.exists() && dungeonFile.canRead()) { throw IOException("Can't read dungeon file.") }
        BufferedReader(FileReader(dungeonFile)).use { br ->
            var monsterList = HashSet<Monster>()
            var split = br.readLine().split(",")
            dungeonName = split[0]
            dungeonLevel = split[1].trim().toInt()
            br.readLines().forEach { line ->
                split = line.split(",")
                monsterList.add(Monster(split[1], split[0]))
            }
            dungeon = Dungeon(dungeonName, dungeonLevel, monsterList)
        }
    }

    private fun equipPrize(player: Character, prize: Equipment): Boolean = when (prize) {
        is Weapon -> player.equipWeapon(prize)
        is Armor -> player.equipArmor(prize)
        is Heirloom -> player.equipHeirloom(prize)
        else -> false
    }
}