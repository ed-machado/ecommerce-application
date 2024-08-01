    package com.machado.ecommerce.config;

    import com.machado.ecommerce.service.UserService;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.web.SecurityFilterChain;

    import static org.springframework.security.config.Customizer.withDefaults;

    @Configuration
    @EnableWebSecurity
    public class SecurityConfig {

        private final UserService userService;

        public SecurityConfig(UserService userService) {
            this.userService = userService;
        }

        @Bean
        public UserDetailsService userDetailsService() {
            return userService;
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
            return authenticationConfiguration.getAuthenticationManager();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(AbstractHttpConfigurer::disable) // Desabilita CSRF
                    .authorizeHttpRequests(auth -> auth  // Configura autorizações
                            .requestMatchers("/api/auth/**").permitAll()  // Permite acesso a /api/auth/**
                            .anyRequest().authenticated() // Exige autenticação para outras requisições
                    )
                    .cors(withDefaults())
                    .formLogin(AbstractHttpConfigurer::disable); // Desabilita o formulário de login padrão
            return http.build();
        }
    }

