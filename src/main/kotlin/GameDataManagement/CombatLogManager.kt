package org.example.GameDataManagement

class CombatLogManager {
    companion object {
        private val logBuffer = StringBuilder()

        fun startLog() {
            logBuffer.setLength(0)
        }

        fun out(msg: String) {
            var newMsg = msg
            if (msg.startsWith("\u001B[3m") && msg.endsWith("\u001B[0m")) newMsg = msg.replace("\u001B[3m", "").replace("\u001B[0m", "")

            println(msg)
            logBuffer.appendLine(newMsg)
        }

        fun getFullLog(): String = logBuffer.toString()

        fun closeLog() {
            logBuffer.setLength(0)
        }

        fun hasLogStarted(): Boolean = logBuffer.isNotEmpty()
    }
}