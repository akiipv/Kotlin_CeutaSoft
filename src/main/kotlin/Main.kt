package org.example

import org.example.Characters.Character
import org.example.Database.*
import org.example.Characters.Stats
import org.example.Characters.Subclasses.*
import org.example.Combat.Combat
import org.example.Deco.CombatUI
import org.example.Equipment.Armor
import org.example.GameModes.Gauntlet
import java.io.File

fun main() {

    val mage = Mage(
        name = "Christian",
        race = "Fairy",
        lvl = 5,
        stats = Stats(
            hp = 113,
            atk = 1,
            arm = 6,
            spd = 6,
            res = 6,
            mag = 99
        ),
        isCPU = false
    )

    val priest = Priest(
        name = "Lucius",
        race = "Elf",
        lvl = 4,
        stats = Stats(
            hp = 110,
            atk = 6,
            arm = 8,
            spd = 10,
            res = 20,
            fth = 25
        ),
        isCPU = true
    )
    val hunter = Hunter(
        name = "Ragnar",
        race = "Nord",
        lvl = 6,
        stats = Stats(
            hp = 100,
            atk = 15,
            arm = 10,
            spd = 14,
            res = 8
        ),
        isCPU = true,
        petName = "Fenrir",
        petRace = "Canid"
    )

    Database.connect()
    val equip = Combat.treasures
    ItemDB.addItem(equip)
    Database.disconnect()

    /*var list = arrayListOf<Character>(hunter, priest, mage)

    var prueba: Gauntlet = Gauntlet()
    prueba.initGauntlet(File("./DataFiles/Dungeons/burningHells.csv"), list)
    prueba.playGauntlet()*/

}