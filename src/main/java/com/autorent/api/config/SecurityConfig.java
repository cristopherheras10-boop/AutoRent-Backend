package com.autorent.api.config;

import com.autorent.api.security.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.*;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.util.Map;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwt;
    private final ObjectMapper mapper;
    public SecurityConfig(JwtAuthenticationFilter j,ObjectMapper m) {
        jwt=j;
        mapper=m;
    }
    @Bean
    SecurityFilterChain filterChain(HttpSecurity h)throws Exception {
        h.csrf(c->c.disable()).cors(c-> {
        }
        ).sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(a -> a
                .requestMatchers("/api/auth/**","/swagger-ui/**","/swagger-ui.html","/v3/api-docs/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET,"/api/categorias/**","/api/vehiculos/**").permitAll()
                .requestMatchers(HttpMethod.POST,"/api/reservas").hasRole("USER")
                .requestMatchers(HttpMethod.GET,"/api/reservas/mias","/api/pagos/mios").hasRole("USER")
                .requestMatchers(HttpMethod.PUT,"/api/reservas/*/cancelar").hasRole("USER")
                .requestMatchers("/api/categorias/**","/api/vehiculos/**","/api/reservas/**","/api/pagos/**","/api/mantenimientos/**").hasRole("ADMIN")
                .anyRequest().authenticated()).exceptionHandling(e->e.authenticationEntryPoint((q,r,x)->json(r,401,"No autenticado o token invalido")).accessDeniedHandler((q,r,x)->json(r,403,"No tiene permisos para esta operacion"))).addFilterBefore(jwt,UsernamePasswordAuthenticationFilter.class);
        return h.build();
    }
    private void json(HttpServletResponse r,int s,String m)throws java.io.IOException {
        r.setStatus(s);
        r.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mapper.writeValue(r.getWriter(),Map.of("estado",s,"mensaje",m));
    }
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration c)throws Exception {
        return c.getAuthenticationManager();
    }
}
