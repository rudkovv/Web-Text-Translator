package com.translate.webtranslator.counter;

import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Component;

@Component
public class RequestCounter {
    private static final AtomicInteger counter = new AtomicInteger(0);

    public synchronized static void increment() {
        counter.incrementAndGet();
    }

    public synchronized static int getCount() {
        return counter.get();
    }

    public synchronized static void reset() {
        counter.set(0);
    }
}
