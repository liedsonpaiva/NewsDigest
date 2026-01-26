package com.newsdigest.service;

import com.newsdigest.domain.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class DigestEligibilityService {

    public boolean isEligible(User user, LocalTime now, LocalDate today) {
        return user.isAtivo()
                && !hasReceivedToday(user, today)
                && isTimeReached(user, now);
    }

    private boolean hasReceivedToday(User user, LocalDate today) {
        return user.getDataUltimoEnvioDigest() != null
                && user.getDataUltimoEnvioDigest().isEqual(today);
    }

    private boolean isTimeReached(User user, LocalTime now) {
        return !user.getHorarioEnvio().isAfter(now);
    }
}
