package com.bobocode.pool;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PoolConfig {
    private String url;
    private String username;
    private String password;
    private int initialPoolSize;
    private String driverClassName;
}
