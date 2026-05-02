package com.cusi.barberia_backend.config;

import com.cusi.barberia_backend.security.AdminUserDetailsService;
import com.cusi.barberia_backend.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configuración de Spring Security 7.
 *
 * REGLAS de acceso:
 *  - POST  /api/auth/login              → público (login del admin)
 *  - GET   /api/cursos                  → público (web pública muestra cursos)
 *  - POST  /api/matriculas              → público (interesado envía formulario)
 *  - GET   /api/matriculas/consulta     → público (consulta estado por DNI)
 *  - Todo lo demás bajo /api/...admin.. → requiere ROLE_ADMIN + JWT válido
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AdminUserDetailsService userDetailsService;

    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // ── CSRF: deshabilitado porque usamos JWT stateless (no cookies de sesión)
                .csrf(csrf -> csrf.disable())

                // ── CORS: delegamos al bean corsConfigurationSource definido abajo
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // ── Reglas de autorización (Lambda DSL obligatorio en Spring Security 7)
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/cursos").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/matriculas").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/matriculas/consulta").permitAll()

                        // Todo lo demás requiere autenticación con ROLE_ADMIN
                        .anyRequest().hasRole("ADMIN")
                )

                // ── Sin sesión HTTP: cada request se autentica solo con el JWT
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // ── Proveedor de autenticación (BCrypt + UserDetailsService)
                .authenticationProvider(authenticationProvider())

                // ── Nuestro filtro JWT va ANTES del filtro estándar de usuario/contraseña
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * CORS: permite que Angular (localhost:4200) llame al backend.
     * En producción cambia allowedOrigins en application.properties.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(allowedOrigins.split(",")));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(false); // no usamos cookies, solo Bearer token

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}