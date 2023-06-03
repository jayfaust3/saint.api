package com.saint.api.security

import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.exceptions.JWTVerificationException

@Component
class AuthorizationFilter: OncePerRequestFilter {

    private val readEndpointsScopeMap: HashMap<String, List<String>> = HashMap()
    private val writeEndpointsScopeMap: HashMap<String, List<String>> = HashMap()

    constructor() {
        readEndpointsScopeMap["/api/saints:GET"] = listOf("saint:read", "saint:write")
        writeEndpointsScopeMap["/api/saints:POST"] = listOf("saint:write")
        writeEndpointsScopeMap["/api/saints:PUT"] = listOf("saint:write")
        writeEndpointsScopeMap["/api/saints:DELETE"] = listOf("saint:write")
    }

    @Throws(JWTVerificationException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain): Unit {
        val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
        val route = request.requestURI.split("/").take(3).joinToString("/")
        val method = request.method

        authorizeFromHeader(route, method, authHeader)

        filterChain.doFilter(request, response)
    }

    private fun authorizeFromHeader(route: String, method: String, authHeader: String): Unit {
        val authHeaderParts: List<String> = authHeader.split(" ")

        when (authHeaderParts.first().lowercase()) {
            "apikey" -> {
                val apiKey: String = authHeaderParts.last()

                validateAPIKey(route, method, apiKey)
            }
            "bearer" -> {
                val token: String = authHeaderParts.last()

                val decodedJWT: DecodedJWT = JWT.decode(token)

                validateJWT(route, method, decodedJWT)
            }
        }
    }

    private fun validateJWT(route: String, method: String, jwt: DecodedJWT): Unit {
        val scopeClaim = jwt.claims["scp"].toString().replace("\"", "")
        val scopesFromJWT = scopeClaim.split(" ")
        val routeKey = "$route:$method"
        val authorizedScopes = readEndpointsScopeMap[routeKey] ?: writeEndpointsScopeMap[routeKey] ?: listOf()
        val scopesIntersection = authorizedScopes.toSet().intersect(scopesFromJWT.toSet())

        logger.info("route: $route")
        logger.info("method: $method")
        logger.info("scopeClaim: $scopeClaim")
        logger.info("scopesFromJWT: $scopesFromJWT")
        logger.info("scopesFromJWT count: ${scopesFromJWT.count()}")
        logger.info("readEndpointsScopeMap: $readEndpointsScopeMap")
        logger.info("writeEndpointsScopeMap: $writeEndpointsScopeMap")
        logger.info("routeKey: $routeKey")
        logger.info("authorizedScopes: $authorizedScopes")
        logger.info("scopesIntersection: $scopesIntersection")

        if (scopesIntersection.isEmpty()) {
            val errMessage = "The provided JWT with id '${jwt.claims["jti"]}' is missing required ${authorizedScopes.joinToString(" or ")} scope(s) to access $method $route"
            logger.error(errMessage)
            throw JWTVerificationException(errMessage)
        }
    }

    private fun validateAPIKey(route: String, method: String, apiKey: String): Unit {
        val routeKey = "$route:$method"
        val authorizedScopes = readEndpointsScopeMap[routeKey] ?: listOf()

        if (authorizedScopes.isEmpty()) {
            val errMessage = "The provided API key, '$apiKey', is not permitted to access $method $route"
            logger.error(errMessage)
            throw JWTVerificationException(errMessage)
        }
    }
}
