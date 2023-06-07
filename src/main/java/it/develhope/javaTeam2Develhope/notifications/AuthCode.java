package it.develhope.javaTeam2Develhope.notifications;

import java.util.UUID;

public class AuthCode {
    private final String code = UUID.randomUUID().toString();


    public String getCode() {
        return code;
    }
}
