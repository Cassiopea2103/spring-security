package com.cassiopea.spring_security.controller ;

import com.cassiopea.spring_security.dto.LoginRequest;
import com.cassiopea.spring_security.dto.LoginResponse;
import com.cassiopea.spring_security.jwt.JWTUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping ( "/api/auth" )
public class AuthController {

    private final JWTUtils jwtUtils ;
    private final AuthenticationManager authenticationManager ;

    @PostMapping ( "/login" )
    public ResponseEntity < ? > login (@RequestBody LoginRequest loginRequest ) {
        log.info ( "Received login request for user {} " , loginRequest.getUsername () ) ;

        Authentication authentication ;
        try {
            log.info("Logging user...");

            // try to authentication the request :
            authentication = authenticationManager.authenticate (
                    new UsernamePasswordAuthenticationToken( loginRequest.getUsername () , loginRequest.getPassword () )
            );

        }
        catch ( AuthenticationException authenticationException ) {

            log.error ( "Invalid login credentials ");

            Map < String , Object > errorResponse = new HashMap<>() ;
            errorResponse.put ( "status" , HttpStatus.BAD_REQUEST ) ;
            errorResponse.put ( "message" , authenticationException.getMessage () ) ;

            return ResponseEntity.status ( HttpStatus.BAD_REQUEST ).body ( errorResponse ) ;
        }


        // set the security context :
        SecurityContextHolder.getContext().setAuthentication ( authentication ) ;

        // retrieve user details :
        UserDetails userDetails = ( UserDetails ) authentication.getPrincipal() ;

        // get the user roles :
        List < String > userRoles = userDetails.getAuthorities().stream()
                .map (GrantedAuthority::getAuthority )
                .toList () ;

        // generate the JWT token :
        String token = jwtUtils.generateToken ( userDetails ) ;

        // build the response object :
        LoginResponse response = LoginResponse.builder()
                .token ( token )
                .username ( loginRequest.getUsername () )
                .roles ( userRoles )
                .build () ;

        log.info ( "Successfully authenticated user {} " ,loginRequest.getUsername () ) ;
        return ResponseEntity.status ( HttpStatus.OK ).body ( response ) ;
    }
}