package dev.danielrodriguez;

import dev.danielrodriguez.filters.JWTAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@SpringBootApplication
public class DanielRodriguezPageApplication {

    public static void main(String[] args) {
        SpringApplication.run(DanielRodriguezPageApplication.class, args);
    }

    @EnableWebSecurity
    @Configuration
    @EnableGlobalMethodSecurity(securedEnabled = true)
    class WebSecurityConfig extends WebSecurityConfigurerAdapter {
        //TODO: configurar Spring security para que los endpoints no sean alcanzables si no se tiene el rol requerido
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable()
                    .addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                    .authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/authenticate").permitAll()
                    .anyRequest().authenticated();
        }
        @Bean
        public PasswordEncoder passwordEncoder(){
            return new BCryptPasswordEncoder(10);
        }
    }
}
