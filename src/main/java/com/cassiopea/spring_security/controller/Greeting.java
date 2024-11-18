package com.cassiopea.spring_security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Greeting {

    @GetMapping
    public  String hello () {
        return "There we go Cassiopea!" ;
    }


    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user" )
    public String userEndpoint () {
        return "User landing page!" ;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin" )
    public String adminDashboard () {
        return "ADMIN dashboard page !!s!" ;
    }
}
