package org.example.GameDataManagement

import java.io.*

class CombatLogManager {
    companion object {
        var writer: PrintWriter? = null
        private val logBuffer = StringBuilder()

        fun startLog(combatLog: String) {
            try {
                writer = PrintWriter(FileWriter(File(GameLogger.defaultDirectory, "$combatLog.txt"), true))
            } catch (e: IOException) {
                System.err.println("Unable to start logging the combat.")
            }
        }

        fun out(msg: String) {
            var newMsg: String = msg
            if (msg.startsWith("\u001B[3m") && msg.endsWith("\u001B[0m"))
                newMsg = msg.replace("\u001B[3m", "").replace("\u001B[0m", "")

            println(msg)
            writer?.println(newMsg)
            writer?.flush()
            logBuffer.appendLine(newMsg)
        }

        fun getFullLog(): String = logBuffer.toString()

        fun closeLog() {
            writer?.close()
            writer = null
            logBuffer.clear()
        }

        fun hasLogStarted(): Boolean = writer != null
    }
}