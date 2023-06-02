package com.saint.api.security

import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.exceptions.InvalidClaimException

@Component
class AuthorizationFilter: OncePerRequestFilter {

    private val endpointScopeMap: HashMap<String, List<String>> =
        HashMap<String, List<String>> ()

    constructor() {
        endpointScopeMap["/api/saints:GET"] = listOf("saint:read", "saint:write")
        endpointScopeMap["/api/saints:POST"] = listOf("saint:write")
        endpointScopeMap["/api/saints:PUT"] = listOf("saint:write")
        endpointScopeMap["/api/saints:DELETE"] = listOf("saint:write")
    }

    @Throws(InvalidClaimException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain): Unit {
        val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
        val route = request.requestURI
        val method = request.method

        authorizeFromHeader(route, method, authHeader)

        filterChain.doFilter(request, response)
    }

    private fun authorizeFromHeader(route: String, method: String, authHeader: String): Unit {
        val authHeaderParts: List<String> = authHeader.split(" ")

        val authHeaderPrefix: String = authHeaderParts.first().lowercase()

        when (authHeaderPrefix) {
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
        val authorizedScopes = endpointScopeMap[routeKey] ?: listOf()
        val scopesIntersection = authorizedScopes.toSet().intersect(scopesFromJWT.toSet())

        logger.info("route: $route")
        logger.info("method: $method")
        logger.info("scopeClaim: $scopeClaim")
        logger.info("scopesFromJWT: $scopesFromJWT")
        logger.info("scopesFromJWT count: ${scopesFromJWT.count()}")
        logger.info("endpointScopeMap: $endpointScopeMap")
        logger.info("routeKey: $routeKey")
        logger.info("authorizedScopes: $authorizedScopes")
        logger.info("scopesIntersection: $scopesIntersection")
        logger.info("scopesIntersection size: ${scopesIntersection.size}")

        if (scopesIntersection.size == 0) {
            throw InvalidClaimException("The provided JWT is missing required ${authorizedScopes.joinToString(" or ")} scope(s) to access $method $route")
        }
    }

    private fun validateAPIKey(requestURI: String, method: String, apiKey: String): Unit {}
}
