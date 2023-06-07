package it.develhope.javaTeam2Develhope.notifications;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AuthCode {
    private final String code = UUID.randomUUID().toString();


    public String getCode() {
        return code;
    }
}
