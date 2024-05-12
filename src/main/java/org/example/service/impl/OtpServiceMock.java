package org.example.service.impl;

import org.example.service.OtpService;

import java.util.UUID;

// TODO
public class OtpServiceMock implements OtpService {
    @Override
    public void sendOtp(UUID key) {

    }

    @Override
    public boolean validateOtp(UUID key, String otp) {
        return true;
    }
}
