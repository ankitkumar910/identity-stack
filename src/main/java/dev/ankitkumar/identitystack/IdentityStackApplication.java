package dev.ankitkumar.identitystack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IdentityStackApplication {

    public static void main(String[] args) {
        System.out.println("Hey Ankit spring boot app started to run.");
        SpringApplication.run(IdentityStackApplication.class, args);
        System.out.println("Hey Ankit spring boot app ran.");
    }

}
