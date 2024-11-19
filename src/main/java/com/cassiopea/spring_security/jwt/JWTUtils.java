package com.cassiopea.spring_security.jwt;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTUtils {

    @Value( "${spring.app.jwtSecret}")
    private String jwtSecret ;

    @Value ( "${spring.app.jwtExpirationMS}")
    private Integer jwtExpirationMS ;

    // generate token from username :
    public String generateToken (UserDetails userDetails ) {
        log.info("Generating token for user : {}", userDetails.getUsername());

        return Jwts.builder()
                .subject ( userDetails.getUsername () )
                .issuedAt ( new Date () )
                .expiration ( new Date ( new Date().getTime () + jwtExpirationMS ) )
                .signWith( getKey () )
                .compact()  ;
    }



    // get token from headers :
    public String getTokenFromHeaders ( HttpServletRequest request ) {

        log.info ( "Retrieving token from headers" ) ;
        String bearerToken = request.getHeader ( "Authorization" ) ;

        log.info ( "Bearer token : {}", bearerToken ) ;

        if ( bearerToken != null ) {
            return bearerToken.substring ( 7 ) ;
        }

        return null ;
    }


    // get username from token :
    public String getUsernameFromToken ( String token ) {
        log.info ( "Retrieving username from token..." ) ;
        return Jwts.parser()
                .verifyWith( ( SecretKey ) getKey () )
                .build()
                .parseSignedClaims ( token )
                .getPayload ()
                .getSubject () ;
    }


    public boolean validateToken ( String token ) {

        try {
            log.info ( "Validating token..." ) ;
            Jwts.parser().verifyWith ( ( SecretKey ) getKey () ).build ().parseSignedClaims ( token ) ;
            return true ;
        }
        catch ( MalformedJwtException malformedJwtException ) {
            log.error ( "Invalid JWT token : {}", malformedJwtException.getMessage () ) ;
        }
        catch ( ExpiredJwtException  expiredJwtException ) {
            log.error ( "Expired JWT token : {}", expiredJwtException.getMessage () ) ;
        }
        catch ( UnsupportedJwtException unsupportedJwtException ) {
            log.error ( "Unsupported JWT token : {}", unsupportedJwtException.getMessage () ) ;
        }
        catch ( IllegalArgumentException argumentException ) {
            log.error ( "JWT claims string is empty : {}", argumentException.getMessage () ) ;
        }

        return false ;
    }


    // get key from jwt secret :
    private Key getKey () {
        return Keys.hmacShaKeyFor (Decoders.BASE64.decode ( jwtSecret ) ) ;
    }
}
