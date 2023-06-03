package com.saint.api.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfiguration(
    private val authenticationFilter: AuthenticationFilter,
    private val authorizationFilter: AuthorizationFilter
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.let {
            it
                .cors()
                .and()
                .csrf()
                .disable()
                .httpBasic()
                .disable()
                .addFilterBefore(authenticationFilter, BasicAuthenticationFilter::class.java)
                .addFilterBefore(authorizationFilter, BasicAuthenticationFilter::class.java)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }

        return http.build()
    }
}