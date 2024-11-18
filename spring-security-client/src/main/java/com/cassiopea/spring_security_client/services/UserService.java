package com.cassiopea.spring_security_client.services;

import com.cassiopea.spring_security_client.entities.User;
import com.cassiopea.spring_security_client.models.UserModel;

public interface UserService {
    User registerUser(UserModel userModel);

    void saveTokenForUser(String tokenUUID, User user);
}
