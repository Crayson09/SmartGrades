package dev.crayson.smartgrades.util

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import dev.crayson.smartgrades.config.ConfigHandler
import java.util.Date
import java.util.UUID

object TokenManager {
    private val config = ConfigHandler.config

    fun generateToken(studentId: UUID): String =
        JWT
            .create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withClaim("id", studentId.toString())
            .withExpiresAt(Date(System.currentTimeMillis() + 60 * 60 * 1000))
            .sign(Algorithm.HMAC256(config.secret))
}
