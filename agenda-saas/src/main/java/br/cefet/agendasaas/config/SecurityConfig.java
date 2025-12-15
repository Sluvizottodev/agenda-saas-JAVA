package br.cefet.agendasaas.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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

import br.cefet.agendasaas.security.AuthTokenFilter;
import br.cefet.agendasaas.security.JwtAuthenticationEntryPoint;

/**
 * Configuração de Spring Security com JWT e Role-Based Access Control
 * 
 * Define:
 * - Regras de autorização por role (CLIENTE, PRESTADOR)
 * - Filtro JWT para validar tokens
 * - CORS habilitado
 * - Stateless session (JWT)
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private AuthTokenFilter authTokenFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    /**
     * Bean PasswordEncoder usando BCrypt
     * Usado para codificar e validar senhas
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configuração principal de segurança
     * Define quais endpoints são protegidos e quais roles podem acessar
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Define a estratégia de criação de sessão como STATELESS (JWT)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Configura o handler para erros de autenticação
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))

                // Configuração de autorização de endpoints
                .authorizeHttpRequests(authz -> authz
                        // Endpoints públicos (sem autenticação)
                        .requestMatchers("/api/auth/login", "/api/auth/register/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/public/**").permitAll()
                        .requestMatchers("/").permitAll()

                        // Endpoint /me disponível para usuários autenticados
                        .requestMatchers("/api/auth/me").authenticated()
                        .requestMatchers("/api/auth/logout").authenticated()

                        // Endpoints de agendamento
                        // GET agendamentos: ambas roles podem listar
                        .requestMatchers(HttpMethod.GET, "/api/agendamentos").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/agendamentos/**").authenticated()

                        // POST agendamentos: CLIENTE cria agendamentos
                        .requestMatchers(HttpMethod.POST, "/api/agendamentos").hasRole("CLIENTE")

                        // PUT/DELETE agendamentos: apenas quem criou ou PRESTADOR pode gerenciar
                        .requestMatchers(HttpMethod.PUT, "/api/agendamentos/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/agendamentos/**").authenticated()

                        // Endpoints de serviços
                        // GET: ambas roles podem listar
                        .requestMatchers(HttpMethod.GET, "/api/servicos").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/servicos/**").authenticated()

                        // POST: apenas PRESTADOR cria serviços
                        .requestMatchers(HttpMethod.POST, "/api/servicos").hasRole("PRESTADOR")

                        // PUT/DELETE: apenas PRESTADOR gerencia seus serviços
                        .requestMatchers(HttpMethod.PUT, "/api/servicos/**").hasRole("PRESTADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/servicos/**").hasRole("PRESTADOR")

                        // Endpoints de horários
                        // GET: ambas roles
                        .requestMatchers(HttpMethod.GET, "/api/horarios").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/horarios/**").authenticated()

                        // POST: apenas PRESTADOR cria horários
                        .requestMatchers(HttpMethod.POST, "/api/horarios").hasRole("PRESTADOR")

                        // PUT/DELETE: apenas PRESTADOR
                        .requestMatchers(HttpMethod.PUT, "/api/horarios/**").hasRole("PRESTADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/horarios/**").hasRole("PRESTADOR")

                        // Endpoints de pagamentos
                        // GET: pode ver pagamentos relacionados
                        .requestMatchers(HttpMethod.GET, "/api/pagamentos").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/pagamentos/**").authenticated()

                        // POST: CLIENTE cria pagamentos
                        .requestMatchers(HttpMethod.POST, "/api/pagamentos").hasRole("CLIENTE")

                        // PUT/DELETE: apenas gerenciador
                        .requestMatchers(HttpMethod.PUT, "/api/pagamentos/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/pagamentos/**").authenticated()

                        // Endpoints de notificações
                        .requestMatchers(HttpMethod.GET, "/api/notificacoes").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/notificacoes/**").authenticated()

                        // Endpoints de usuários
                        .requestMatchers(HttpMethod.GET, "/api/usuarios").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/usuarios/**").authenticated()

                        // Qualquer outra requisição requer autenticação
                        .anyRequest().authenticated());

        // Adiciona o filtro JWT antes do filtro padrão de autenticação
        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configuração de CORS
     * Permite requisições do frontend e define headers permitidos
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Origens permitidas (ajuste para produção)
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "http://localhost:8080",
                "http://127.0.0.1:3000",
                "http://127.0.0.1:8080",
                "file://"));

        // Métodos HTTP permitidos
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        // Headers permitidos
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Accept",
                "X-Requested-With",
                "X-CSRF-Token",
                "Origin"));

        // Headers expostos ao cliente
        configuration.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Total-Count"));

        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
