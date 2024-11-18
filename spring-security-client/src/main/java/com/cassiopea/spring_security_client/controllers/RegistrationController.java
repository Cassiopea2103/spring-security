package com.cassiopea.spring_security_client.controllers;

import com.cassiopea.spring_security_client.entities.User;
import com.cassiopea.spring_security_client.events.RegistrationCompleteEvent;
import com.cassiopea.spring_security_client.models.UserModel;
import com.cassiopea.spring_security_client.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    @Autowired
    private UserService userService ;

    @Autowired
    private ApplicationEventPublisher publisher ;

    @PostMapping("/register")
    public String registerUser (
            @RequestBody UserModel userModel ,
            final HttpServletRequest request
            ) {
        User user = userService.registerUser( userModel ) ;
        publisher.publishEvent( new RegistrationCompleteEvent (
                user ,
                getApplicationUrl ( request  )
        ));

        return "Successfully registered user!";
    }


    private String getApplicationUrl (HttpServletRequest request ) {
        return "http://" +
                request.getServerName() +
                ":" + request.getServerPort() +
                request.getContextPath() ;
    }
}
