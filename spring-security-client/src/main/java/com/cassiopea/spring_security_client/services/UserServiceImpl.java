package com.cassiopea.spring_security_client.services;

import com.cassiopea.spring_security_client.entities.User;
import com.cassiopea.spring_security_client.entities.VerificationToken;
import com.cassiopea.spring_security_client.models.UserModel;
import com.cassiopea.spring_security_client.repositories.UserRepository;
import com.cassiopea.spring_security_client.repositories.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository repository ;

    @Autowired
    private PasswordEncoder passwordEncoder ;

    @Autowired
    private VerificationTokenRepository tokenRepository ;

    @Override
    public User registerUser ( UserModel userModel )  {
        User user = new User () ;
        user.setFirstName ( userModel.getFirstName () ) ;
        user.setLastName ( userModel.getLastName () ) ;
        user.setEmail ( userModel.getEmail () ) ;
        user.setRole ( "USER" ) ;
        user.setPassword ( passwordEncoder.encode ( userModel.getPassword () ) ) ;

        repository.save ( user ) ; 
        return user ;

    }

    @Override
    public void saveTokenForUser(String tokenUUID, User user) {
        // create the token for the user :
        VerificationToken verificationToken = new VerificationToken( user , tokenUUID ) ;

        // persist the token entity :
        tokenRepository.save( verificationToken ) ;
    }
}
