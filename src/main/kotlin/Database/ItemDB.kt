package org.example.Database

import org.example.Characters.Character
import org.example.Equipment.*
import java.sql.PreparedStatement
import java.sql.SQLIntegrityConstraintViolationException
import java.sql.Statement
import java.sql.Types

object ItemDB {
    fun addItem(item: Equipment): Boolean {
        try {
            Database.connection!!.autoCommit = false
            val query = "insert into Item(idStat, class, name, rarity, type, piece, value) values(?, ?, ?, ?, ?, ?, ?)"
            val ps = Database.connection!!.prepareStatement(query)

            ps.setInt(1,statsLoader(item))
            ps.setString(2, item::class.simpleName)
            ps.setString(3, item.name)
            ps.setString(4, item.rarity)

            when (item) {
                is Armor -> {
                    ps.setString(5, item.piece)
                    ps.setString(6, item.type)
                }
                is Weapon -> {
                    ps.setString(5, item.type)
                    ps.setString(6, Types.VARCHAR.toString())
                }
                is Heirloom -> {
                    ps.setString(5, item.type)
                    ps.setString(6, Types.VARCHAR.toString())
                }
            }
            ps.setInt(7, item.value)

            ps.executeUpdate()
            Database.connection!!.commit()
            return true
        }catch (e: Exception){
            System.err.println("Error while adding item.")
            Database.connection!!.rollback()
            e.printStackTrace()
            return false
        } finally { Database.connection!!.autoCommit = true }
    }

    fun addItem(equipment: ArrayList<Equipment>): Boolean {
        try {
            Database.connection!!.autoCommit = false
            val query = "insert into Item(idStat, class, name, rarity, type, piece, value) values(?, ?, ?, ?, ?, ?, ?)"
            equipment.forEach { item ->
                val ps = Database.connection!!.prepareStatement(query)

                ps.setInt(1,statsLoader(item))
                ps.setString(2, item::class.simpleName)
                ps.setString(3, item.name)
                ps.setString(4, item.rarity)

                when (item) {
                    is Armor -> {
                        ps.setString(5, item.type)
                        ps.setString(6, item.piece)
                    }
                    is Weapon -> {
                        ps.setString(5, item.type)
                        ps.setString(6, null)
                    }
                    is Heirloom -> {
                        ps.setString(5, item.type)
                        ps.setString(6, null)
                    }
                }
                ps.setInt(7, item.value)
                ps.executeUpdate()
            }
            Database.connection!!.commit()
            return true
        }catch (e: Exception){
            System.err.println("Error while adding item.")
            Database.connection!!.rollback()
            e.printStackTrace()
            return false
        } finally { Database.connection!!.autoCommit = true }
    }

    fun getItemById(equipmentId: Int): Equipment? {
        TODO()
    }

    fun removeItem(equipment: Equipment): Boolean {
        TODO()
    }

    private fun statsLoader(item: Equipment): Int {
        val query = "insert into Stats(hp, atk, arm, spd, res, mag, fth) values(?, ?, ?, ?, ?, ?, ?)"
        val ps: PreparedStatement = Database.connection!!.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        ps.setInt(1, item.equipmentStats.hp)
        ps.setInt(2, item.equipmentStats.atk)
        ps.setInt(3, item.equipmentStats.arm)
        ps.setInt(4, item.equipmentStats.spd)
        ps.setInt(5, item.equipmentStats.res)
        ps.setInt(6, item.equipmentStats.mag)
        ps.setInt(7, item.equipmentStats.fth)

        ps.executeUpdate()
        val key = ps.generatedKeys
        key.next()
        return key.getInt(1)
    }
}