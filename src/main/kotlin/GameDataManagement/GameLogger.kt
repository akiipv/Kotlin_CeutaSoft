package org.example.GameDataManagement

import org.example.Characters.Character
import java.io.*

class GameLogger {
    companion object {
        val defaultDirectory: File = File("./src/main/OutputFiles")

        fun readCharacter(characterSheet: File): Character {
            var data = Array(11) { " " }
            var i = 0

            BufferedReader(FileReader(characterSheet)).use { br ->
                br.forEachLine { line ->
                    if (":" in line) {
                        var mapping = line.split(":")
                        data[i] = mapping[1].trim()
                        i++
                    }
                }
                return CharacterFactory.createfromDataArray(data)!!
            }
        }

        fun printCharacterSheet(character: Character) {
            val characterSheet = File(defaultDirectory, "${character.name}.txt")
            BufferedWriter(FileWriter(characterSheet)).use { bw ->
                println("Printing ${character.name}'s character sheet.")
                bw.write(character.toString())
            }
        }

        fun printCharacterSheet(character: Character, d: File) {
            require(d.isDirectory) { "${d.name} is not a directory." }
            val characterSheet = File(d, "${character.name}.txt")
            BufferedWriter(FileWriter(characterSheet)).use { bw ->
                println("Printing ${character.name}'s character sheet.")
                bw.write(character.toString())
            }
        }

        fun printPartySheet(party: MutableList<Character>) {
            party.sort()
            val partySheet = File(defaultDirectory, "party_${party[0].name}.txt")
            BufferedWriter(FileWriter(partySheet)).use { bw ->
                party.forEach {
                    bw.write(it.toString())
                    bw.write("\n₊˚ ✧ ━━━━⊱⋆⊰━━━━ ✧ ₊˚\n")
                }
            }
        }

        fun checKIfCharacterExists(characterSheets: MutableList<File>, name: String): Boolean {
            characterSheets.forEach {
                require(it.isFile && it.canRead()) { "Error. Provide an adecuate list of File paths." }
                var c: Character = readCharacter(it)
                if (c.name == name)
                    return true
            }
            return false
        }

        fun checkIfRepeatedClass(characterSheets: MutableList<File>, name: String): Boolean {
            var classChecklist = BooleanArray(6)
            characterSheets.forEach {
                require(it.isFile && it.canRead())
                var className = getClassNameFromFile(it)
                var index = getClassIndex(className)
                if (index == -1) throw RuntimeException("Class not found in the index database.")
                if (classChecklist[index]) return false
                classChecklist[index] = true
            }
            return true
        }

        fun readCombatWin(c: Character, combatLog: File): Boolean {
            require(combatLog.isFile && combatLog.canRead()) { "Error. You must procure a valid combat log." }
            val lastLine = BufferedReader(FileReader(combatLog)).useLines { it.lastOrNull() }
            if(lastLine != null && lastLine.startsWith(c.name)) {
                println("This character won the last combat, and thus levels up.")
                return true
            } else System.err.println("This character didn't win last combat.")
            return false
        }

        private fun getClassNameFromFile(file: File): String = BufferedReader(FileReader(file)).useLines { line ->
            line.find { it.startsWith("Class:") }?.substringAfter(":")?.trim() ?: ""
        }

        private fun getClassIndex(className: String): Int = when (className) {
            "Warrior" -> 0
            "Mage" -> 1
            "Thief" -> 2
            "Hunter" -> 3
            "Paladin" -> 4
            "Priest" -> 5
            else -> -1
        }
    }
}