package com.driven.dm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class DeliveryManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryManagementApplication.class, args);

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("test1234"));
	}

}
