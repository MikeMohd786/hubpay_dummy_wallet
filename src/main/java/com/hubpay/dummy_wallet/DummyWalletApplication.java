package com.hubpay.dummy_wallet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;

@SpringBootApplication
@EnableTransactionManagement
public class DummyWalletApplication {

    public static void main(String[] args) {
        SpringApplication.run(DummyWalletApplication.class, args);
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void loadSql() {
        Resource resource = new ClassPathResource("seed-data/setup.sql");
        try {
            jdbcTemplate.execute(new String(Files.readAllBytes(resource.getFile().toPath())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
