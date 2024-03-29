package com.spring.training.config;

import com.spring.training.util.Cors;
import com.spring.training.util.JwtConverter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    static final String[] whitelist = {
            "/actuator/**", "/v3/api-docs/**", "/swagger-ui.html", "/webjars/**", "/swagger-ui/**",
            "/*/v3/api-docs", "/swagger-config"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(authentication -> authentication
                .antMatchers(whitelist).permitAll()
                .antMatchers("/**").authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt().jwtAuthenticationConverter(new JwtConverter()))
                .cors().and().csrf().disable().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return http.build();
    }

    @Bean
    @ConfigurationProperties("cors")
    public Cors cors() {
        return new Cors();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(Cors cors) {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(cors.isAllowCredentials());
        config.addAllowedOriginPattern(cors.getAllowedOriginPattern());
        config.setAllowedHeaders(cors.getAllowedHeaders());
        config.setAllowedMethods(cors.getAllowedMethods());
        source.registerCorsConfiguration("/**", config);
        return source;
    }


}