package com.translate.webtranslator.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;

import java.util.LinkedList;

/**
 * The InMemoryCache class represents an in-memory cache implementation.
 * It uses a LinkedHashMap as the underlying cache map and a Queue to track the order of cache keys.
 * The cache has a maximum size specified by the maxSize variable.
 */
public class InMemoryCache {

    private Map<CacheKey, Object> cacheMap;
    private Queue<CacheKey> keyQueue;
    private int maxSize;

    /**
     * Constructs a new InMemoryCache object with default settings.
     * The maxSize is set to 5 by default.
     */
    public InMemoryCache() {
        cacheMap = new LinkedHashMap<>();
        keyQueue = new LinkedList<>();
        maxSize = 5;
    }

    /**
     * Puts the specified key-value pair into the cache.
     * If the cache already contains the key, the value is updated.
     * If the cache exceeds the maximum size, the oldest key-value pair is evicted.
     *
     * @param cacheKey The key to store in the cache.
     * @param value The value to associate with the key.
     */
    public synchronized void put(CacheKey cacheKey, Object value) {
        if (cacheMap.containsKey(cacheKey)) {
            cacheMap.put(cacheKey, value);
        } else {
            cacheMap.put(cacheKey, value);
            keyQueue.add(cacheKey);
            if (cacheMap.size() > maxSize) {
                CacheKey oldestKey = keyQueue.poll();
                cacheMap.remove(oldestKey);
            }
        }
    }

    /**
     * Retrieves the value associated with the specified key from the cache.
     *
     * @param cacheKey The key whose associated value is to be retrieved.
     * @return The value associated with the key, or null if the key is not found in the cache.
     */
    public synchronized Object get(CacheKey cacheKey) {
        return cacheMap.get(cacheKey);
    }

    /**
     * Removes the specified key and its associated value from the cache.
     *
     * @param cacheKey The key to be removed from the cache.
     */
    public synchronized void remove(CacheKey cacheKey) {
        cacheMap.remove(cacheKey);
        keyQueue.remove(cacheKey);
    }

    /**
     * Clears the cache, removing all key-value pairs.
     */
    public synchronized void clear() {
        cacheMap.clear();
        keyQueue.clear();
    }

    /**
     * Returns the current size of the cache.
     *
     * @return The number of key-value pairs currently stored in the cache.
     */
    public synchronized int getSize() {
        return cacheMap.size();
    }
}