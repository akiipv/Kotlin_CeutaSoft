package org.example.GameDataManagement

import java.io.*

class CombatLogManager {
    companion object {
        var writer: PrintWriter? = null

        fun startLog(combatLog: String) {
            try {
                writer = PrintWriter(FileWriter(File(GameLogger.defaultDirectory, "$combatLog.txt"), true))
            } catch (e: IOException) {
                System.err.println("Unable to start logging the combat.")
            }
        }

        fun out(msg: String) {
            println(msg)
            writer?.println(msg)
            writer?.flush()
        }

        fun hasLogStarted(): Boolean = writer != null
    }
}