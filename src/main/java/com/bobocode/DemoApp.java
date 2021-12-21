package com.bobocode;

import com.bobocode.pool.ConnectionPool;
import com.bobocode.pool.PoolConfig;
import lombok.SneakyThrows;


public class DemoApp {
    private static final String URL_CONNECTION = "jdbc:postgresql://localhost:5433/study";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";

    @SneakyThrows
    public static void main(String[] args) {

//        var config = PoolConfig.builder()
//                .url(URL_CONNECTION)
//                .username(USERNAME)
//                .password(PASSWORD)
//                .initialPoolSize(10)
//                .driverClassName("org.postgresql.Driver")
//                .build();
//
//        var dataSource = new ConnectionPool(config);

        var dataSource = new ConnectionPool();

        try (var connection = dataSource.getConnection()) {
            try (var statement = connection.createStatement()) {
                var resultSet = statement.executeQuery("SELECT * FROM products;");
                while (resultSet.next()) {
                    System.out.println(resultSet.getString("name"));
                }
            }
        }
    }
}
