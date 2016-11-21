package vn.wse.lms.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@ImportResource(value={"classpath:/spring/datasource-config.xml"})
@EnableTransactionManagement(proxyTargetClass = true)
@ComponentScan(basePackages={"vn.wse.lms.repository.impl"})
public class DatabaseConfig {
    public DatabaseConfig() {
        super();
    }
}