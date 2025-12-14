package dev.crayson.smartgrades

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import dev.crayson.smartgrades.config.ConfigHandler
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt

fun Application.configureSecurity() {
    install(Authentication) {
        jwt("auth-jwt") {
            val c = ConfigHandler.config
            realm = c.realm
            val secret = c.secret
            val issuer = c.issuer
            val audience = c.audience

            verifier(
                JWT
                    .require(Algorithm.HMAC256(secret))
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .build(),
            )

            validate { credential ->
                if (credential.payload.getClaim("id").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}
