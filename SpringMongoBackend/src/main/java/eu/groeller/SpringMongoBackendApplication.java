package eu.groeller;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
public class SpringMongoBackendApplication  {

	public static void main(String[] args) {
		SpringApplication.run(SpringMongoBackendApplication.class, args);
	}

}
