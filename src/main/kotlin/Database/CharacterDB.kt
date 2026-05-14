package org.example.Database

import org.example.Characters.Character
import org.example.GameDataManagement.CharacterFactory
import java.sql.PreparedStatement
import java.sql.SQLIntegrityConstraintViolationException
import java.sql.Statement

object CharacterDB {
    fun addCharacter(character: Character): Boolean {
        try { // soy una genia ya no echo de menos el throws al principio del metodo
            Database.connection!!.autoCommit = false
            val query = "insert into Characters(className, name, race, lvl, isCPU, idStats) values(?, ?, ?, ?, ?, ?)"
            val ps: PreparedStatement = Database.connection!!.prepareStatement(query)

            ps.setString(1, character.getClassName())
            ps.setString(2, character.name)
            ps.setString(3, character.race)
            ps.setInt(4, character.lvl)
            ps.setBoolean(5, character.isCPU)
            ps.setInt(6, statsLoader(character))

            ps.executeUpdate()
            Database.connection!!.commit()
            return true
        } catch (e: SQLIntegrityConstraintViolationException) {
            System.err.println("Character already exists.")
            return false
        } catch (e: Exception) {
            System.err.println("There's been an error adding the character.")
            Database.connection!!.rollback()
            e.printStackTrace()
        } finally { Database.connection!!.autoCommit = true }
        return false
    }

    fun getCharacterById(id: Int): Character? {
        try {
            val query = "select c.className as className, c.name, c.race, c.lvl, c.isCPU, s.hp, s.atk, s.arm, s.spd, s.res, s.mag, s.fth from Characters as c inner join Stats as s on c.idStats = s.idStats where c.id = ?"
            val ps: PreparedStatement = Database.connection!!.prepareStatement(query)
            ps.setInt(1, id)

            val key = ps.executeQuery()

            if (key.next()) {
                return CharacterFactory.createFromResultSet(key)
            }

        } catch (e: Exception) {
            System.err.println("ID might be wrong.")
            e.printStackTrace()
            return null
        }
        return null
    }

    fun getAllCharacters(): List<Character>? {
        try {
            val query = "select c.className, c.name, c.race, c.lvl, c.isCPU, s.hp, s.atk, s.arm, s.spd, s.res, s.mag, s.fth from Characters as c inner join Stats as s on c.idStats = s.idStats"
            val ps: PreparedStatement = Database.connection!!.prepareStatement(query)
            val key = ps.executeQuery()
            var characters: MutableList<Character> = ArrayList()

            while (key.next()) {
                var char = CharacterFactory.createFromResultSet(key)
                characters.add(char)
            }
            return characters
        } catch (e: Exception) {
            System.err.println("Error retrieving characters.")
            e.printStackTrace()
            return null
        }
    }

    fun removeCharacter(character: Character): Boolean {
        try {
            Database.connection!!.autoCommit = false
            val query = "delete from Characters where name = ? and race = ? and lvl = ? and isCPU = ?"
            val ps: PreparedStatement = Database.connection!!.prepareStatement(query)

            ps.setString(1, character.name)
            ps.setString(2, character.race)
            ps.setInt(3, character.lvl)
            ps.setBoolean(4, character.isCPU)

            ps.executeUpdate()
            Database.connection!!.commit()
            return true
        } catch (e: Exception) {
            System.err.println("Can not remove character.")
            Database.connection!!.rollback()
            e.printStackTrace()
            return false
        } finally { Database.connection!!.autoCommit = true }
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