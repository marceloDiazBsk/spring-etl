package corar.etl.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean(name = "postgres")
    @Primary
    @ConfigurationProperties(prefix="spring.postgres-datasource")
    public DataSource postgresDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "mysql")
    @ConfigurationProperties(prefix="spring.mysql-datasource")
    public DataSource mysqlDataSource() {
        return DataSourceBuilder.create().build();
    }
}
