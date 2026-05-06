package org.example.GameDataManagement

import org.example.Characters.Character
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class GameLogger {
    companion object {
        protected val defaultDirectory: File = File("./OutputFiles/")

        fun readCharacter(characterSheet: File): Character {
            var data = MutableList(11) { " " }
            var i = 0

            BufferedReader(FileReader(characterSheet)).use { br ->
                br.forEachLine { line ->
                    if (":" in line) {
                        var mapping = line.split(":")
                        data[i] = mapping[1].trim()
                        i++
                    }
                }
            }
            return TODO()
        }
    }
}