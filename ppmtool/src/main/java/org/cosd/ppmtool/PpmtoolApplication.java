package org.cosd.ppmtool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableJpaRepositories
public class PpmtoolApplication {

	 @Bean
	    BCryptPasswordEncoder bCryptPasswordEncoder(){
	        return new BCryptPasswordEncoder();
	    }

	public static void main(String[] args) {
		SpringApplication.run(PpmtoolApplication.class, args);
	}

}
