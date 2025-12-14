package dev.crayson.smartgrades.util

import org.mindrot.jbcrypt.BCrypt

object PasswordManager {
    fun hashPassword(pasword: String): String = BCrypt.hashpw(pasword, BCrypt.gensalt())

    fun comparePassword(
        password: String,
        hashedPassword: String,
    ): Boolean = BCrypt.checkpw(password, hashedPassword)
}
