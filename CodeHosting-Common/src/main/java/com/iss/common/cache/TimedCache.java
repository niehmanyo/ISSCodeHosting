package com.iss.common.cache;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimedCache<K, V> {

    private final Map<K, TimedValue<V>> cache = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public TimedCache(long ttlInSeconds) {
        scheduler.scheduleAtFixedRate(this::evictExpiredEntries, ttlInSeconds, ttlInSeconds, TimeUnit.SECONDS);
    }

    public void put(K key, V value, long ttlInSeconds) {
        cache.put(key, new TimedValue<>(value, ttlInSeconds));
    }

    public V get(K key) {
        TimedValue<V> timedValue = cache.get(key);
        if (timedValue == null || timedValue.isExpired()) {
            cache.remove(key);
            return null;
        }
        return timedValue.getValue();
    }

    private void evictExpiredEntries() {
        cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }

    private static class TimedValue<V> {
        private final V value;
        private final Instant expiryTime;

        public TimedValue(V value, long ttlInSeconds) {
            this.value = value;
            this.expiryTime = Instant.now().plusSeconds(ttlInSeconds);
        }

        public V getValue() {
            return value;
        }

        public boolean isExpired() {
            return Instant.now().isAfter(expiryTime);
        }
    }

    public void shutdown() {
        scheduler.shutdown();
    }
}
