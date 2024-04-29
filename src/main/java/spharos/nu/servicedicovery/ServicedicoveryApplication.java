package spharos.nu.servicedicovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class ServicedicoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicedicoveryApplication.class, args);
	}

}
