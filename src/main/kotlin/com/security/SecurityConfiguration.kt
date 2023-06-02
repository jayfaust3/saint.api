package com.saint.api.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfiguration {
    @Autowired
    lateinit var authenticationFilter: AuthenticationFilter

    @Autowired
    lateinit var authorizationFilter: AuthorizationFilter

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.let {
            it.cors().and()
                .csrf().disable()
                .httpBasic().disable()
                //.authorizeHttpRequests()
                //.anyRequest().authenticated()
                //.requestMatchers("/").permitAll()
                // .anyRequest().hasAuthority("action:resource")
                //.and()
                .addFilterBefore(authenticationFilter, BasicAuthenticationFilter::class.java)
                .addFilterBefore(authorizationFilter, BasicAuthenticationFilter::class.java)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }

        return http.build()
    }
}