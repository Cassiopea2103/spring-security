package com.cassiopea.spring_security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Slf4j
@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    JWTUtils jwtUtils ;
    @Autowired
    UserDetailsService userDetailsService ;


    @Override
    protected void doFilterInternal (HttpServletRequest request , HttpServletResponse response , FilterChain chain ) throws ServletException, IOException {

        log.info ( "Entered Authentication Token Filter ... " ) ;

        // retrieve token from headers :
        String token = jwtUtils.getTokenFromHeaders ( request ) ;

        if ( token == null ) {
            log.error ( "Token is null !" ) ;
        }
        if ( token != null && jwtUtils.validateToken ( token ) ) {

            // Retrieve username from the token :
            String username = jwtUtils.getUsernameFromToken(token);

            // User Details :
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Username - Password authentication object :
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            log.info("Authorizations from user {} : {} ", username, userDetails.getAuthorities());

            // authentication details :
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // set the security context :
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }


        chain.doFilter(request, response);
    }
}
