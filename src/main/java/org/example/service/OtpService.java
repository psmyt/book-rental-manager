package org.example.service;

import java.util.UUID;

public interface OtpService {
    void sendOtp(UUID key);
    boolean validateOtp(UUID key, String otp);
}
