package cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;

import java.util.LinkedList;

public class InMemoryCache {

    private Map<CacheKey, Object> cacheMap;
    private Queue<CacheKey> keyQueue;
    private int maxSize;

    public InMemoryCache() {
        cacheMap = new LinkedHashMap<>();
        keyQueue = new LinkedList<>();
        maxSize = 5;
    }

    public void put(CacheKey cacheKey, Object value) {
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

    public Object get(CacheKey cacheKey) {
        return cacheMap.get(cacheKey);
    }

    public void remove(CacheKey cacheKey) {
        cacheMap.remove(cacheKey);
        keyQueue.remove(cacheKey);
    }

    public void clear() {
        cacheMap.clear();
        keyQueue.clear();
    }

    public int getSize() {
        return cacheMap.size();
    }
}