package org.example.GameDataManagement

import org.example.Characters.Character
import org.example.Characters.Stats
import org.example.Characters.Subclasses.*
import java.sql.ResultSet

class CharacterFactory {
    companion object {
        fun createfromDataArray(data: Array<String>): Character {
            var character: Character? = null
            var isCPU: Boolean = true
            var characterStats = Stats(
                data[4].toInt(),
                data[5].toInt(),
                data[6].toInt(),
                data[7].toInt(),
                data[8].toInt()
            )

            when (data[1]) {
                "Warrior", "Thief", "Hunter", "Monster" -> {
                    if(data[9] == "Player")
                        isCPU = false
                    character = createCharacter(data[1], data[0], data[2], data[3].toInt(), characterStats, isCPU = isCPU)
                }
                "Mage" -> {
                    if(data[10] == "Player")
                        isCPU = false
                    characterStats.mag = data[9].toInt()
                    character = createCharacter(data[1], data[0], data[2], data[3].toInt(), characterStats, isCPU = isCPU)
                }
                "Paladin", "Priest" -> {
                    if(data[10] == "Player")
                        isCPU = false
                    characterStats.fth = data[9].toInt()
                    character = createCharacter(data[1], data[0], data[2], data[3].toInt(), characterStats, isCPU = isCPU)
                }
                else -> throw IllegalArgumentException("${data[1]} is not a valid class. Character cannot be generated.")
            }
            return character
        }

        fun createFromResultSet(key: ResultSet): Character {
            val stats = Stats(
                key.getInt("hp"),
                key.getInt("atk"),
                key.getInt("arm"),
                key.getInt("spd"),
                key.getInt("res"),
                key.getInt("mag"),
                key.getInt("fth"),
            )

            return createCharacter(
                key.getString("className"),
                key.getString("name"),
                key.getString("race"),
                key.getInt("lvl"),
                stats,
                key.getBoolean("isCPU")
            )
        }

        private fun createCharacter(
            className: String,
            name: String,
            race: String,
            lvl: Int,
            stats: Stats,
            isCPU: Boolean
        ): Character = when (className) {
            "Warrior" -> Warrior(name, race, lvl, stats, isCPU = isCPU)
            "Mage" -> Mage(name, race, lvl, stats, isCPU = isCPU)
            "Thief" -> Thief(name, race, lvl, stats, isCPU = isCPU)
            "Hunter" -> Hunter(name, race, lvl, stats, isCPU = isCPU)
            "Paladin" -> Paladin(name, race, lvl, stats, isCPU = isCPU)
            "Priest" -> Priest(name, race, lvl, stats, isCPU = isCPU)
            "Monster" -> Monster(name, race, lvl, stats, isCPU = isCPU)
            else -> throw IllegalArgumentException("Invalid class: $className")
        }
    }
}