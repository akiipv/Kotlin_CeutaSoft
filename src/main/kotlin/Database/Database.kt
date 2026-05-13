package org.example.Database

import java.sql.Connection
import java.sql.DriverManager

object Database {

    val URL = "jdbc:mysql://localhost:3306/CeutaSoftRPG"
    val USER = "root"
    val PASSWORD = "user"

    var connection: Connection? = null

    fun connect() {
        connection = DriverManager.getConnection(URL, USER, PASSWORD)
    }

    fun disconnect() {
        connection?.close()
        connection = null
    }

    fun isConnected(): Boolean {
        return connection != null
    }
}