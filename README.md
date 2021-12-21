# Pool Connection

`PoolConnection` - is a pool of connections for connections to the physical data source that this Pool object
represents.

You can initialize `PoolConnrction` with:

- `properties.application` file when invoke default constructor

<pre><code>
url=jdbc:postgresql://localhost:5433/study
username=postgres
password=postgres
initialPoolSize=10
driverClassName=org.postgresql.Driver
<hr />
var dataSource = mre ConnectionPool();
</code></pre>

- `PoolConfig` class when invoke constructor with parameter

<pre><code>
        var config = PoolConfig.builder()
                .url(URL_CONNECTION)
                .username(USERNAME)
                .password(PASSWORD)
                .initialPoolSize(10)
                .driverClassName("org.postgresql.Driver")
                .build();

        var dataSource = new ConnectionPool(config);
</code></pre>

## License

This project is Apache License 2.0 - see the [LICENSE](LICENSE) file for details
