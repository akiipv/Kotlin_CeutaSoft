package org.example.Database

import org.example.Characters.Character
import org.example.Equipment.Equipment
import java.sql.PreparedStatement

object CombatDB {
    fun addCombat(winner: Character, prize: Equipment, player: Character, enemy: Character, attacks: Int): Boolean {
        try {
            val query = "insert into Combat(prizeId, winner, date) values (?, ?, now())"
            val ps = Database.connection!!.prepareStatement(query)
            ps.setInt(1, TODO())
            ps.setString(2, winner.name)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}