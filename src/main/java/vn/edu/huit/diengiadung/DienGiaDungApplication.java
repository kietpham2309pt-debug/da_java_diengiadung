package vn.edu.huit.diengiadung;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DienGiaDungApplication {

    public static void main(String[] args) {
        SpringApplication.run(DienGiaDungApplication.class, args);
    }
}
