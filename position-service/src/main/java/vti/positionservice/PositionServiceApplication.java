package vti.positionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"vti.common", "vti.positionservice"})
public class PositionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PositionServiceApplication.class, args);
    }

}
