package com.cassiopea.spring_security_client.listeners;

import com.cassiopea.spring_security_client.entities.User;
import com.cassiopea.spring_security_client.events.RegistrationCompleteEvent;
import com.cassiopea.spring_security_client.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class RegistrationCompleteEventListener implements
    ApplicationListener<RegistrationCompleteEvent> {

    @Autowired
    private UserService userService ;

    // when the registration complete event happens :
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // retrieve user object from event :
        User user = event.getUser() ;

        // create a token :
        String tokenUUID  = UUID.randomUUID().toString();

        // save the verification token for the user :
        userService.saveTokenForUser ( tokenUUID , user ) ;

        // createe verification url :
        String url =
                event.getApplicationUrl () +
                "verififyRegistration?token=" + tokenUUID ;

        // send link to user ( we are just logging it ) :
        log.info("Click on the link to verify : {}" , url ) ;
    }
}
