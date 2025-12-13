package org.pet.backendpetshelter.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write(
                                    "{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"Authentication required\"}");
                        }))
                .authorizeHttpRequests(auth -> auth
                        // Auth endpoints
                        .requestMatchers("/api/auth/register").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/refresh").permitAll()
                        .requestMatchers("/api/auth/logout").authenticated()

                        // Public GET endpoints - allow browsing without authentication
                        .requestMatchers(HttpMethod.GET, "/api/animal/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/species/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/breed/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/veterinarian/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/foster-care/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/medical-record/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/adoption/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/vaccination/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/vaccination-type/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/vaccine-type-species/**").permitAll()

                        // Stored procedures GETs
                        .requestMatchers(HttpMethod.GET, "/api/test-procedures/**").permitAll()

                        // Mongo and Neo4j GET's public
                        .requestMatchers(HttpMethod.GET, "/api/mongo/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/neo4j/**").permitAll()
                        
                        // Dog Facts
                        .requestMatchers(HttpMethod.GET, "/api/dog-facts/**").permitAll()

                        // sentry test endpoints
                        .requestMatchers(HttpMethod.GET, "/test-sentry/**").permitAll()

                        // Public docs
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()

                        // Animal management - ADMIN and STAFF can modify
                        .requestMatchers(HttpMethod.POST, "/api/animal/**").hasAnyAuthority("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.PUT, "/api/animal/**").hasAnyAuthority("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.DELETE, "/api/animal/**").hasAnyAuthority("ADMIN", "STAFF")
                        
                        // Species and Breed management - ADMIN and STAFF
                        .requestMatchers(HttpMethod.POST, "/api/species/**").hasAnyAuthority("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.PUT, "/api/species/**").hasAnyAuthority("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.DELETE, "/api/species/**").hasAnyAuthority("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.POST, "/api/breed/**").hasAnyAuthority("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.PUT, "/api/breed/**").hasAnyAuthority("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.DELETE, "/api/breed/**").hasAnyAuthority("ADMIN", "STAFF")

                        // Adoption management - ADMIN and STAFF
                        .requestMatchers(HttpMethod.POST, "/api/adoption/**").hasAnyAuthority("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.PUT, "/api/adoption/**").hasAnyAuthority("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.DELETE, "/api/adoption/**").hasAnyAuthority("ADMIN", "STAFF")

                        // Medical Records - ADMIN and VETERINARIAN
                        .requestMatchers(HttpMethod.POST, "/api/medical-record/**").hasAnyAuthority("ADMIN", "VETERINARIAN")
                        .requestMatchers(HttpMethod.PUT, "/api/medical-record/**").hasAnyAuthority("ADMIN", "VETERINARIAN")
                        .requestMatchers(HttpMethod.DELETE, "/api/medical-record/**").hasAnyAuthority("ADMIN", "VETERINARIAN")

                        // Vaccination management - ADMIN and VETERINARIAN
                        .requestMatchers(HttpMethod.POST, "/api/vaccination/**").hasAnyAuthority("ADMIN", "VETERINARIAN")
                        .requestMatchers(HttpMethod.PUT, "/api/vaccination/**").hasAnyAuthority("ADMIN", "VETERINARIAN")
                        .requestMatchers(HttpMethod.DELETE, "/api/vaccination/**").hasAnyAuthority("ADMIN", "VETERINARIAN")

                        // Vaccination Type management - ADMIN and VETERINARIAN
                        .requestMatchers(HttpMethod.POST, "/api/vaccination-type/**").hasAnyAuthority("ADMIN", "VETERINARIAN")
                        .requestMatchers(HttpMethod.PUT, "/api/vaccination-type/**").hasAnyAuthority("ADMIN", "VETERINARIAN")
                        .requestMatchers(HttpMethod.DELETE, "/api/vaccination-type/**").hasAnyAuthority("ADMIN", "VETERINARIAN")

                        // User management
                        .requestMatchers(HttpMethod.GET, "/api/user/**")
                        .hasAnyAuthority("ADMIN", "STAFF", "USER", "VETERINARIAN", "ADOPTER", "FOSTER")

                        .anyRequest().authenticated())
                .addFilterBefore(jwtFilter,
                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}