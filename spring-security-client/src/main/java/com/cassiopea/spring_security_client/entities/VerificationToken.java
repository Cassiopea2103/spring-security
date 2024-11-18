package com.cassiopea.spring_security_client.entities;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class VerificationToken {

    @Id
    @GeneratedValue ( strategy = GenerationType.IDENTITY )
    private long tokenId ;

    private static final int EXPIRATION_TIME = 10 ;

    private String tokenString ;
    private Date expirationTime ;

    @OneToOne ( fetch = FetchType.EAGER )
    @JoinColumn (
            name = "user_id",
            referencedColumnName = "userId",
            nullable = false ,
            foreignKey = @ForeignKey ( name = "FK_USER_VERIFY_TOKEN" )
    )
    private User user ;

    // constructor to pass a user and a token to this class :
    // it will process automatically the expiration time :
    public VerificationToken ( User user , String tokenString ) {
        super () ;
        this.user = user ;
        this.tokenString = tokenString ;
        this.expirationTime = calculateExpirationTime ( EXPIRATION_TIME ) ;
    }


    private Date calculateExpirationTime ( int expirationTime  ) {
        // set a calendar instance :
        Calendar calendar = Calendar.getInstance () ;

        // set the time to NOW :
        calendar.setTimeInMillis( new Date().getTime());

        // add the minute of token expiration time :
        calendar.add ( Calendar.MINUTE , expirationTime ) ;

        return new Date ( calendar.getTime().getTime() ) ;
    }
}
