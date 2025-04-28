package vti.authenticationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import vti.config.CommonProperties;

@SpringBootApplication
@ComponentScan(basePackages = {"vti.exception_handler", "vti.authenticationservice"})
@EnableConfigurationProperties(CommonProperties.class)
public class AuthenticationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationServiceApplication.class, args);
	}

}
