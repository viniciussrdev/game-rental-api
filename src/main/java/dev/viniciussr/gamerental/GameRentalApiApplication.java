package dev.viniciussr.gamerental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GameRentalApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameRentalApiApplication.class, args);
    }

}