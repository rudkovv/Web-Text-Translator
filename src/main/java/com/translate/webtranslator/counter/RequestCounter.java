package com.translate.webtranslator.counter;

import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Component;

@Component
public class RequestCounter {
    private static final AtomicInteger counter = new AtomicInteger(0);
    
    private RequestCounter() {
    
    }

    public static void increment() {
        counter.incrementAndGet();
    }

    public static int getCount() {
        return counter.get();
    }

    public static void reset() {
        counter.set(0);
    }
}
