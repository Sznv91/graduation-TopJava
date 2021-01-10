package ru.topjava.web;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:spring-mvc.xml")
@ComponentScan("ru.topjava.config")
public class TestConfig {

}
