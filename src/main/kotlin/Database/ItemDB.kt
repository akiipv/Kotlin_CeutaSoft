package org.example.Database

import org.example.Characters.Stats
import org.example.Equipment.*
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import java.sql.Types
import kotlin.use

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

            typeSetter(ps, item, 1)
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

                typeSetter(ps, item, 1)
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

    fun getItemById(id: Int): Equipment? {
        try {
            val query = "select i.class, i.name, i.rarity, i.type, i.piece, i.value, s.hp, s.atk, s.arm, s.spd, s.res, s.mag, s.fth from Item i inner join Stats s on i.idStat = s.idStats where i.idItem = ?"
            Database.connection!!.prepareStatement(query).use { ps ->
                ps.setInt(1, id)
                ps.executeQuery().use { key -> return if (key.next()) miniItemFactory(key) else null }
            }

        } catch (e: Exception) {
            System.err.println("ID might be wrong.")
            e.printStackTrace()
            return null
        }
    }

    fun getItemId(item: Equipment): Int {
        try {
            val query = "select idItem from Item where class = ? and name = ? and rarity = ? and type = ? and piece <=> ? and value = ?"
            Database.connection!!.prepareStatement(query).use { ps ->
                ps.setString(1, item::class.simpleName)
                ps.setString(2, item.name)
                ps.setString(3, item.rarity)

                typeSetter(ps, item, 0)
                ps.setInt(6, item.value)

                ps.executeQuery().use { key -> return if (key.next()) key.getInt("idItem") else 0 }
            }

        } catch (e: Exception) {
            System.err.println("This item does not exist.")
            e.printStackTrace()
            return 0
        }
    }

    fun removeItem(item: Equipment): Boolean {
        try {
            Database.connection!!.autoCommit = false
            val query = "delete from Item where name = ? and class = ? and rarity = ? and type = ? and piece <=> ? and value = ?"
            Database.connection!!.prepareStatement(query).use { ps ->
                ps.setString(1, item.name)
                ps.setString(2, item::class.simpleName)
                ps.setString(3, item.rarity)

                typeSetter(ps, item, 0)
                ps.setInt(6, item.value)

                ps.executeUpdate()
                Database.connection!!.commit()
                return true
            }
        } catch (e: Exception) {
            System.err.println("Can not remove item.")
            Database.connection!!.rollback()
            e.printStackTrace()
            return false
        } finally { Database.connection!!.autoCommit = true }
    }

    private fun statsLoader(item: Equipment): Int {
        val query = "insert into Stats(hp, atk, arm, spd, res, mag, fth) values(?, ?, ?, ?, ?, ?, ?)"
        val ps: PreparedStatement = Database.connection!!.prepareStatement(query, Statement.RETURN_GENERATED_KEYS).use { ps ->
            ps.setInt(1, item.equipmentStats.hp)
            ps.setInt(2, item.equipmentStats.atk)
            ps.setInt(3, item.equipmentStats.arm)
            ps.setInt(4, item.equipmentStats.spd)
            ps.setInt(5, item.equipmentStats.res)
            ps.setInt(6, item.equipmentStats.mag)
            ps.setInt(7, item.equipmentStats.fth)

            ps.executeUpdate()
            ps.generatedKeys.use { key -> return if (key.next()) key.getInt(1) else 0 }
        }
    }

    private fun miniItemFactory(key: ResultSet): Equipment? {
        val stats = Stats(
            key.getInt("hp"),
            key.getInt("atk"),
            key.getInt("arm"),
            key.getInt("spd"),
            key.getInt("res"),
            key.getInt("mag"),
            key.getInt("fth"),
        )

        return when (key.getString("class")) {
            "Armor" -> {
                Armor(key.getString("name"),
                    stats,
                    key.getString("rarity"),
                    key.getInt("value"),
                    key.getString("type"),
                    key.getString("piece"))
            }
            "Weapon" -> {
                Weapon(key.getString("name"),
                    stats,
                    key.getString("rarity"),
                    key.getInt("value"),
                    key.getString("type"),
                    0)
            }
            "Heirloom" -> {
                Heirloom(key.getString("name"),
                    stats,
                    key.getString("rarity"),
                    key.getInt("value"),
                    key.getString("type"))
            }
            else -> {
                System.err.println("This id does not have an item")
                null
            }
        }
    }

    private fun typeSetter(ps: PreparedStatement, item: Equipment, pos: Int) {
        when (item) {
            is Armor -> {
                ps.setString(4 + pos, item.type)
                ps.setString(5 + pos, item.piece)
            }
            is Weapon -> {
                ps.setString(4 + pos, item.type)
                ps.setNull(5 + pos, Types.VARCHAR)
            }
            is Heirloom -> {
                ps.setString(4 + pos, item.type)
                ps.setNull(5 + pos, Types.VARCHAR)
            }
        }
    }
}