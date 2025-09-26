package com.mentorlink.users.domain.location;

import org.springframework.stereotype.Component;

@Component
public class TimezoneImpl implements ITimezone{

    @Override
    public String getTimezoneByCountry(String country) {
        return switch (country.toUpperCase()) {
            case "BRASIL" -> "America/Sao_Paulo";
            case "URUGUAY" -> "America/Montevideo";
            case "CHILE" -> "America/Santiago";
            default -> "America/Argentina/Buenos_Aires";
        };
    }
}
