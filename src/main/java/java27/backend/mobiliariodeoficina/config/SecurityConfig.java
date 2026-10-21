package java27.backend.mobiliariodeoficina.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java27.backend.mobiliariodeoficina.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults()) // Busca el bean corsConfigurationSource() que está abajo
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas de autenticación y documentación
                        .requestMatchers(
                                "/auth/**",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/error"
                        ).permitAll()

                        // --- NUEVAS RUTAS PÚBLICAS DEL CATÁLOGO Y SHOWROOM ---
                        .requestMatchers(HttpMethod.GET, "/api/v1/productos/**", "/api/v1/categorias/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/v1/citas").authenticated()

                        // --- RUTAS PROTEGIDAS ADMINISTRATIVAS ---
                        // --- RUTAS PROTEGIDAS ADMINISTRATIVAS ---
                        .requestMatchers(HttpMethod.POST, "/api/v1/productos", "/api/v1/productos/**").hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/productos/**").hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/productos/**").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/v1/citas/**").hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/citas/**").hasAnyAuthority("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/v1/categorias").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/categorias/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/categorias/**").hasAuthority("ADMIN")
                        // Todo lo demás requiere autenticación
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // --- CONFIGURACIÓN DE CORS PARA EL FRONTEND (VUE.JS) ---
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Permite el puerto por defecto de Vite/Vue.js (ajusta si usas otro puerto)
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:3000"));

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}