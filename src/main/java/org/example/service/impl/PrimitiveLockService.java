package org.example.service.impl;

import org.example.service.LockService;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class PrimitiveLockService implements LockService {
    private final ConcurrentMap<UUID, Lock> locks = new ConcurrentHashMap<>();

    @Override
    public void lock(UUID key) {
        locks.computeIfAbsent(key, k -> new ReentrantLock())
                .lock();
    }

    @Override
    public void unlock(UUID key) {
        Lock lock = locks.get(key);
        if (lock != null) {
            lock.unlock();
        }
    }
}
