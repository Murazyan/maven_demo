package com.example.maven_demo;

import lombok.SneakyThrows;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;

import java.io.FileInputStream;
import java.util.Properties;

public class Main {


    public static void main(String[] args) {
        System.out.println("Starting application");
        initDb();
        Application application =  new Application();
        application.start();
    }

    @SneakyThrows
    public static void initDb(){
        Properties properties = new Properties();
        properties.load(new FileInputStream("C:\\Users\\Margarita_Murazyan\\Desktop\\maven_demo\\src\\main\\resources\\application.properties"));
        FluentConfiguration configuration = Flyway.configure()
                .configuration(properties);
        Flyway flyway = new Flyway(configuration);
        flyway.migrate();
    }

}
