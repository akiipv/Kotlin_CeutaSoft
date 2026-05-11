package org.example.GameDataManagement

import org.example.Characters.Character
import org.example.Characters.Stats
import org.example.Characters.Subclasses.Warrior

class CharacterFactory {
    companion object {
        fun createfromDataArray(data: MutableList<String>): Character {
            var character: Character? = null
            var isCPU: Boolean = true
            var characterStats = Stats(
                data[4].toInt(),
                data[5].toInt(),
                data[6].toInt(),
                data[7].toInt(),
                data[8].toInt(),
                data[8].toInt()
            )
            when (data[1]) {
                "Warrior" -> {
                    if(data[9] == "Player")
                        isCPU = false
                    character = Warrior(data[0], data[2], data[3].toInt(), characterStats, isCPU)
                }
                "Mage" -> {
                    if(data[10] == "Player")
                        isCPU = false
                    characterStats.mag = data[9].toInt()
                    character = Warrior(data[0], data[2], data[3].toInt(), characterStats, isCPU)
                }
                "Thief" -> {
                    if(data[9] == "Player")
                        isCPU = false
                    character = Warrior(data[0], data[2], data[3].toInt(), characterStats, isCPU)
                }
                "Hunter" -> {
                    if(data[9] == "Player")
                        isCPU = false
                    character = Warrior(data[0], data[2], data[3].toInt(), characterStats, isCPU)
                }
                "Paladin" -> {
                    if(data[10] == "Player")
                        isCPU = false
                    characterStats.fth = data[9].toInt()
                    character = Warrior(data[0], data[2], data[3].toInt(), characterStats, isCPU)
                }
                "Priest" -> {
                    if(data[10] == "Player")
                        isCPU = false
                    characterStats.fth = data[9].toInt()
                    character = Warrior(data[0], data[2], data[3].toInt(), characterStats, isCPU)
                }
                "Monster" -> {
                    if(data[9] == "Player")
                        isCPU = false
                    character = Warrior(data[0], data[2], data[3].toInt(), characterStats, isCPU)
                }
                else -> throw IllegalArgumentException(data[1] + " is not a valid class. Character cannot be generated.")
            }
            return character
        }
    }
}