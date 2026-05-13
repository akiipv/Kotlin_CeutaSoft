package org.example.Database

import org.example.Characters.Character
import java.sql.PreparedStatement
import java.sql.Statement

object CharacterDB {
    fun addCharacter(character: Character): Boolean {
        try { // soy una genia ya no echo de menos el throws al principio del metodo
            val query = "insert into Characters(class, name, race, lvl, idStats) values(?, ?, ?, ?, ?)"
            val ps: PreparedStatement = Database.connection!!.prepareStatement(query)

            ps.setString(1, character.getClassName())
            ps.setString(2, character.name)
            ps.setString(3, character.race)
            ps.setInt(4, character.lvl)
            ps.setInt(5, statsLoader(character))

            ps.executeUpdate()
            return true
        } catch (e: Exception) {
            System.err.println("There's been an error adding the character.")
            e.printStackTrace()
        }
        return false
    }

    private fun statsLoader(character: Character): Int {
        val query = "insert into Stats(hp, atk, arm, spd, res, mag, fth) values(?, ?, ?, ?, ?, ?, ?)"
        val ps: PreparedStatement = Database.connection!!.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        ps.setInt(1, character.stats.hp)
        ps.setInt(2, character.stats.atk)
        ps.setInt(3, character.stats.arm)
        ps.setInt(4, character.stats.spd)
        ps.setInt(5, character.stats.res)
        ps.setInt(6, character.stats.mag)
        ps.setInt(7, character.stats.fth)

        ps.executeUpdate()
        val key = ps.generatedKeys
        key.next()
        return key.getInt(1)
    }
}