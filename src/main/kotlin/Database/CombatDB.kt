package org.example.Database

import org.example.Characters.Character
import org.example.Equipment.Equipment
import java.io.File
import java.sql.PreparedStatement
import java.sql.Statement

object CombatDB {
    fun addCombat(winnerSide: String, prize: Equipment, players: ArrayList<Character>, enemies: ArrayList<Character>, fullLog: String): Boolean {
        try {
            Database.connection!!.autoCommit = false

            val combatId = combatLoader(winnerSide, prize)
            logLoader(combatId, fullLog)
            combatPlayerLoader(combatId, players, "player")
            combatPlayerLoader(combatId, enemies, "enemy")

            Database.connection!!.commit()
            return true
        } catch (e: Exception) {
            System.err.println("This combat is not valid.")
            Database.connection!!.rollback()
            e.printStackTrace()
            return false
        } finally { Database.connection!!.autoCommit = true }
    }

    private fun combatLoader(winnerSide: String, prize: Equipment): Int {
        val prizeId = ItemDB.getItemId(prize) ?: throw Exception("Prize not found.")
        val query = "insert into Combat(prizeId, winner, date) values (?, ?, now())"
        Database.connection!!.prepareStatement(query, Statement.RETURN_GENERATED_KEYS).use { ps ->
            ps.setInt(1, prizeId)
            ps.setString(2, winnerSide)
            ps.executeUpdate()

            ps.generatedKeys.use { key -> return if (key.next()) key.getInt(1) else 0 }
        }
    }

    private fun logLoader(combatId: Int, fullLog: String) {
        val query = "insert into Combat_log values (?, ?)"
        Database.connection!!.prepareStatement(query).use { ps ->
            ps.setInt(1, combatId)
            ps.setString(2, fullLog)
            ps.executeUpdate()
        }
    }

    private fun combatPlayerLoader(combatId: Int, characters: ArrayList<Character>, side: String) {
        val query = "insert into Combat_players values (?, ?, ?)"
        Database.connection!!.prepareStatement(query).use { ps ->
            ps.setInt(1, combatId)
            characters.forEach {
                val characterId = CharacterDB.getCharacterId(it) ?: throw Exception("Character not found.")
                ps.setInt(2, characterId)
                ps.setString(3, side)
                ps.executeUpdate()
            }
        }
    }
}