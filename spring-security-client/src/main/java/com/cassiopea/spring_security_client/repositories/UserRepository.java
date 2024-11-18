package com.cassiopea.spring_security_client.repositories;

import com.cassiopea.spring_security_client.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
