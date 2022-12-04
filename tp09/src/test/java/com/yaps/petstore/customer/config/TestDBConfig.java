package com.yaps.petstore.customer.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;


// Code de configuration de la base de données pour les tests.
// noter que ce code est un peu redondant (certaines parties se retrouvent aussi dans l'application).
@Configuration
@EnableAutoConfiguration
@ActiveProfiles("test") // active application-test.properties en PLUS de application.properties
public class TestDBConfig {
    
    @Value("${spring.datasource.driver-class-name}")
    String jdbcDriverName;
    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String user;
    @Value("${spring.datasource.password}")
    String password;


    /**
     * Spring boot provides us with a default Datasource. However,
     * the implementation (Hikari) chosen in this case seems to cause problems with DBUnit.
     * 
     * Hence, for the tests, we avoid it and work with a simple DriverManagerDataSource.
     * @return
     */
    // @Bean
    // public DataSource dataSource() {
    //     DriverManagerDataSource manager = new DriverManagerDataSource(url, user, password);
    //     manager.setDriverClassName(jdbcDriverName);
    //     return manager;
    // }
}
