package com.cassiopea.spring_security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain (HttpSecurity http ) throws Exception {
        http.authorizeHttpRequests ( requests -> requests.anyRequest().authenticated() ) ;
        http.formLogin ( Customizer.withDefaults() ) ;
        http.httpBasic ( Customizer.withDefaults() ) ;

        return http.build () ;
    }


    @Bean
    public UserDetailsService userDetailsService () {

        UserDetails user1 = User.withUsername ( "user1" )
                .password ( "{noop}user1" )
                .roles ( "USER" )
                .build() ;

        UserDetails admin1 = User.withUsername ( "admin1" )
                .password ( "{noop}admin1" )
                .roles ( "ADMIN" )
                .build () ;

        return new InMemoryUserDetailsManager( user1 , admin1 ) ;
    }
}
