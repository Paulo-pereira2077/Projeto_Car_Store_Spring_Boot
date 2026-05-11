package br.com.carstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // ← ADICIONAR: Injetar o filtro
    private final JwtRequestFilter jwtRequestFilter;

    // ← ADICIONAR: Construtor com @Lazy
    public SecurityConfig(@Lazy JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {

        // Regra 1: Configuração para API (Stateless) - Rotas /api/**
        http.securityMatcher("/api/**") // Aplica ESTA regra SOMENTE a /api/**
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Sem sessão no servidor
                )

                // ← ADICIONAR: Filtro JWT
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)

                // Neste ponto, adicionaremos o Filtro JWT posteriormente.

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // Endpoint de Login API é público
                        .anyRequest().authenticated() // Todas as outras rotas API exigem autenticação
                );


        return http.build();

    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // ← PREENCHER: Configuração para web (não-API)
            http.csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth
                            .anyRequest().permitAll()  // Ajuste conforme necessário
                    );
            return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Usa BCrypt, que é o padrão seguro para hashear senhas.
        return new BCryptPasswordEncoder();
    }

    // Este método deve ser adicionado DENTRO da classe SecurityConfig
    @Bean
    public org.springframework.security.core.userdetails.UserDetailsService users(PasswordEncoder passwordEncoder) {

        // Detalhes do usuário de teste
        org.springframework.security.core.userdetails.UserDetails user =
                org.springframework.security.core.userdetails.User.builder()
                        .username("admin")
                        // A senha 'admin' será codificada pelo BCryptPasswordEncoder
                        .password(passwordEncoder.encode("admin"))
                        .roles("USER", "ADMIN") // Roles para uso futuro em autorização
                        .build();

        // Gerenciador em memória (apenas para testes)
        return new org.springframework.security.provisioning.InMemoryUserDetailsManager(user);

    }

    // ← ADICIONAR: AuthenticationManager como Bean
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}