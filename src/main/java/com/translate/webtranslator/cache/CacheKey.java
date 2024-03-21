package com.translate.webtranslator.cache;

import java.util.Objects;

public class CacheKey {
	
    private final Object key;

    public CacheKey(Object key) {
        this.key = key;
    }

    public Object getKey() {
        return key;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CacheKey otherKey = (CacheKey) obj;
        return Objects.equals(key, otherKey.key);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (key != null ? key.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Key{" + "key=" + key + '}';
    }
}
