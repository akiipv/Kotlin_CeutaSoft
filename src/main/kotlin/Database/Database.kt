package org.example.Database

import java.sql.Connection
import java.sql.DriverManager

object Database {

    const val HOST_URL = "jdbc:mysql://localhost:3306/"
    const val DB_NAME = "CeutaSoftRPG"
    const val URL = "jdbc:mysql://localhost:3306/$DB_NAME?serverTimezone=UTC"
    const val USER = "root"
    const val PASSWORD = "user"

    var connection: Connection? = null

    fun initialize() {
        Class.forName("com.mysql.cj.jdbc.Driver")
        createDatabaseIfNotExists()
        connect()
        createTables()
    }

    private fun createDatabaseIfNotExists() {
        DriverManager.getConnection(HOST_URL, USER, PASSWORD).use { conn ->
            conn.createStatement().use { stmt ->
                stmt.executeUpdate("create database if not exists `$DB_NAME`")
            }
        }
    }

    fun connect() {
        println("Connected to database")
        connection = DriverManager.getConnection(URL, USER, PASSWORD)
    }

    fun disconnect() {
        println("Disconnected from database")
        connection?.close()
        connection = null
    }

    fun isConnected(): Boolean = connection != null

    private fun createTables() {
        val conn = connection ?: throw IllegalStateException("database not connected")

        conn.createStatement().use { cs ->

            cs.executeUpdate(
                """
                create table if not exists `Stats` (
                    `idStats` int not null auto_increment,
                    `hp` int not null,
                    `atk` int not null,
                    `arm` int not null,
                    `spd` int not null,
                    `res` int not null,
                    `mag` int default null,
                    `fth` int default null,
                    primary key (`idStats`)
                ) engine=innodb default charset=utf8mb4 collate=utf8mb4_0900_ai_ci
                """.trimIndent()
            )

            cs.executeUpdate(
                """
                create table if not exists `Item` (
                    `idItem` int not null auto_increment,
                    `idStat` int not null,
                    `class` varchar(45) not null,
                    `name` varchar(45) not null,
                    `rarity` varchar(45) not null,
                    `type` varchar(45) not null,
                    `piece` varchar(45) default null,
                    `value` int not null,
                    primary key (`idItem`),
                    key `fk_Item_1_idx` (`idStat`),
                    constraint `fk_Item_1` foreign key (`idStat`) references `Stats` (`idStats`)
                ) engine=innodb default charset=utf8mb4 collate=utf8mb4_0900_ai_ci
                """.trimIndent()
            )

            cs.executeUpdate(
                """
                create table if not exists `Characters` (
                    `id` int not null auto_increment,
                    `className` varchar(45) not null,
                    `name` varchar(45) not null,
                    `race` varchar(45) not null,
                    `lvl` int not null,
                    `isCPU` tinyint(1) not null,
                    `idStats` int not null,
                    primary key (`id`),
                    unique key `unique_character` (`className`, `name`, `race`, `lvl`, `isCPU`),
                    key `fk_Character_2_idx` (`idStats`),
                    constraint `fk_Characters_1` foreign key (`idStats`) references `Stats` (`idStats`)
                ) engine=innodb default charset=utf8mb4 collate=utf8mb4_0900_ai_ci
                """.trimIndent()
            )

            cs.executeUpdate(
                """
                create table if not exists `Combat` (
                    `idCombat` int not null auto_increment,
                    `prizeId` int not null,
                    `winner` enum('player','enemy') not null,
                    `date` datetime default null,
                    primary key (`idCombat`),
                    key `fk_Combat_2_idx1` (`prizeId`),
                    key `fk_Combat_1_idx` (`winner`),
                    constraint `fk_Combat_2` foreign key (`prizeId`) references `Item` (`idItem`)
                ) engine=innodb default charset=utf8mb4 collate=utf8mb4_0900_ai_ci
                """.trimIndent()
            )

            cs.executeUpdate(
                """
                create table if not exists `Combat_log` (
                    `idCombat` int not null,
                    `fullLog` longtext,
                    primary key (`idCombat`),
                    constraint `fk_Combat_log_1` foreign key (`idCombat`) references `Combat` (`idCombat`)
                ) engine=innodb default charset=utf8mb4 collate=utf8mb4_0900_ai_ci
                """.trimIndent()
            )

            cs.executeUpdate(
                """
                create table if not exists `Combat_players` (
                    `combatId` int not null,
                    `playerId` int not null,
                    `side` enum('player','enemy') not null,
                    primary key (`combatId`, `playerId`),
                    key `fk_Combat_players_2_idx` (`playerId`),
                    key `fk_Combat_players_3_idx` (`side`),
                    constraint `fk_Combat_players_1` foreign key (`combatId`) references `Combat` (`idCombat`),
                    constraint `fk_Combat_players_2` foreign key (`playerId`) references `Characters` (`id`)
                ) engine=innodb default charset=utf8mb4 collate=utf8mb4_0900_ai_ci
                """.trimIndent()
            )

            cs.executeUpdate(
                """
                create table if not exists `Dungeon` (
                    `idDungeon` int not null auto_increment,
                    `name` varchar(45) default null,
                    `lvl` varchar(45) default null,
                    primary key (`idDungeon`)
                ) engine=innodb default charset=utf8mb4 collate=utf8mb4_0900_ai_ci
                """.trimIndent()
            )

            cs.executeUpdate(
                """
                create table if not exists `Dungeon_monster` (
                    `idDungeon` int not null,
                    `idMonster` int not null,
                    primary key (`idDungeon`, `idMonster`),
                    key `fk_Dungeon_monster_2_idx` (`idMonster`),
                    constraint `fk_Dungeon_monster_1` foreign key (`idMonster`) references `Characters` (`id`),
                    constraint `fk_Dungeon_monster_2` foreign key (`idDungeon`) references `Dungeon` (`idDungeon`)
                ) engine=innodb default charset=utf8mb4 collate=utf8mb4_0900_ai_ci
                """.trimIndent()
            )

            cs.executeUpdate(
                """
                create table if not exists `Equipment` (
                    `idCharacter` int not null,
                    `idItem` int not null,
                    `type` varchar(45) not null,
                    `quantity` int not null,
                    primary key (`idCharacter`, `idItem`),
                    key `fk_Equipment_1_idx` (`idCharacter`),
                    key `fk_Equipment_2_idx` (`idItem`),
                    constraint `fk_Equipment_1` foreign key (`idCharacter`) references `Characters` (`id`),
                    constraint `fk_Equipment_2` foreign key (`idItem`) references `Item` (`idItem`)
                ) engine=innodb default charset=utf8mb4 collate=utf8mb4_0900_ai_ci
                """.trimIndent()
            )

            cs.executeUpdate(
                """
                create table if not exists `Gauntlet` (
                    `idGauntlet` int not null auto_increment,
                    `idDungeon` int not null,
                    `ended` tinyint not null,
                    `idCombat` int not null,
                    primary key (`idGauntlet`),
                    key `fk_Gauntlet_1_idx` (`idDungeon`),
                    key `fk_Gauntlet_2_idx` (`idCombat`),
                    constraint `fk_Gauntlet_1` foreign key (`idDungeon`) references `Dungeon` (`idDungeon`),
                    constraint `fk_Gauntlet_2` foreign key (`idCombat`) references `Combat` (`idCombat`)
                ) engine=innodb default charset=utf8mb4 collate=utf8mb4_0900_ai_ci
                """.trimIndent()
            )
        }
    }
}