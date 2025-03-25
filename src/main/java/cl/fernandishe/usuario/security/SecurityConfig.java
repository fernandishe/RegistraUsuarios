package cl.fernandishe.usuario.security;

import cl.fernandishe.usuario.utils.JwtAuthenticationFilter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = "cl.fernandishe.usuario.controller")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // Deshabilitar CSRF, ya que estás usando JWT
                .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()  // No requiere autenticación
                .antMatchers("/api/usuarios/**").authenticated()  // Requiere autenticación para acceder a los usuarios
                .anyRequest().permitAll()  // Permite acceso libre a otros endpoints
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);  // Asegúrate de que el filtro JWT se ejecute antes
    }




}
