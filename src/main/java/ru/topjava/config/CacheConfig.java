package ru.topjava.config;

import com.google.common.cache.CacheBuilder;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

//    https://habr.com/ru/post/465667/
    @Primary
    @Bean("userCache")
    public CacheManager userCacheManager() {
        return new ConcurrentMapCacheManager() {
            @Override
            protected Cache createConcurrentMapCache(String name) {
                return new ConcurrentMapCache(
                        name,
                        CacheBuilder.newBuilder()
                                .expireAfterAccess(5, TimeUnit.MINUTES)
                                .build().asMap(),
                        false
                );
            }
        };
    }


    @Bean("RestaurantCache")
    public CacheManager RestaurantCacheManager() {
        return new ConcurrentMapCacheManager() {
            @Override
            protected Cache createConcurrentMapCache(String name) {
                return new ConcurrentMapCache(
                        name,
                        CacheBuilder.newBuilder()
                                .expireAfterWrite(30, TimeUnit.MINUTES)
                                .maximumSize(300)
                                .build().asMap(),
                        false
                );
            }
        };
    }
}
