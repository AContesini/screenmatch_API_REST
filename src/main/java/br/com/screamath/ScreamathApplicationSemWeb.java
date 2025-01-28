package br.com.screamath;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreamathApplicationSemWeb implements CommandLineRunner {

    public static class ScreamathApplication {

        public static void main(String[] args) {
            SpringApplication.run(br.com.screamath.ScreamathApplicationSemWeb.class, args);
        }

    }
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Olá mundo");

    }

}
