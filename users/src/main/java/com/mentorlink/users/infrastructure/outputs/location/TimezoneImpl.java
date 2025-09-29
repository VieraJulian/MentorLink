package com.mentorlink.users.infrastructure.outputs.location;

import com.mentorlink.users.domain.port.outbound.ITimezone;
import org.springframework.stereotype.Component;

@Component
public class TimezoneImpl implements ITimezone {

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
