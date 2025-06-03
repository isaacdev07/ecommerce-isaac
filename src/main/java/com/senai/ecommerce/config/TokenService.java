package com.senai.ecommerce.config;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.senai.ecommerce.entities.Usuario;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private  String secret;

    public  String GenerateToken(Usuario user){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String Token = JWT.create()
                    .withIssuer("auth-rest-api")
                    .withSubject(user.getEmail())
                    .withExpiresAt(GenExpDate())	
                    .sign(algorithm);
            return Token;
        }catch (JWTCreationException exception){
            throw new RuntimeException("Error, Falha ao gerar o token", exception);

        }
    }

    public  String ValidatToken(String Token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-rest-api")
                    .build()
                    .verify(Token)
                    .getSubject();
        }catch (JWTVerificationException  ValException){
            return "";
        }
    }

    private Instant GenExpDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

}