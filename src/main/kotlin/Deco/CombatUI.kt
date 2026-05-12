package org.example.Deco

import org.example.Characters.*

object CombatUI {

    // todo -> no encuentro un ascii mejor para hacer esto m cagoentodo esq no m gusta del todo cmo q ueda la cajita 
    val topLeft = '╭'
    val topRight = '╮'
    val bottomLeft = '╰'
    val bottomRIght = '╯'
    val horizontal = '─'
    val vertical = '│'
    val padding = 1

    fun box(text: String): String {
        val lines = text.lines()
        val maxLength = lines.maxOfOrNull { it.length } ?: 0
        val boxWidth = maxLength + (padding * 2)

        val sb = StringBuilder()

        sb.append(topLeft)
        sb.append(horizontal.toString().repeat(boxWidth + 2))
        sb.append(topRight).append("\n")

        for (line in lines) {
            val padding = " ".repeat(padding)
            val spacing = " ".repeat(maxLength - line.length)
            sb.append("$vertical $padding$line$spacing$padding $vertical\n")
        }

        sb.append(bottomLeft)
        sb.append(horizontal.toString().repeat(boxWidth + 2))
        sb.append(bottomRIght)

        return sb.toString()
    }

    fun hpDisplay(c1: Character, c2: Character): String {
        val content = """
            ${c1.name}: ${if (c1.stats.hp <= 0) "DEAD" else c1.stats.hp}
            ${c2.name}: ${if (c2.stats.hp <= 0) "DEAD" else c2.stats.hp}
        """.trimIndent()

        return box(content)
    }

    fun message(text: String): String {
        return box(text)
    }

    fun specialActionUI(subclass: String): String = when (subclass) {
        "1" -> TODO()
            "2" -> TODO()
            "3" -> TODO()
            "4" -> TODO()
            "5" -> TODO()
            "6" -> TODO()
            "7" -> TODO()
        else -> throw IllegalArgumentException("Tf is ts")
    }
}
