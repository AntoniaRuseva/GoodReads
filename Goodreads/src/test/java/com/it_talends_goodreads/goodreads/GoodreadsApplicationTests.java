package com.it_talends_goodreads.goodreads;

import com.it_talends_goodreads.goodreads.model.repositories.UserRepository;
import com.it_talends_goodreads.goodreads.service.UserService;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;

import javax.sql.DataSource;

@SpringBootTest

class GoodreadsApplicationTests {

    @Test
    void contextLoads() {
    }
}

