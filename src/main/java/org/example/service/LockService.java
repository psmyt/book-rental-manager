package org.example.service;

import java.util.UUID;

public interface LockService {
    void lock(UUID key);
    void unlock(UUID key);
}
