package com.example.FalconMES;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class FalconMesApplication {

	public static void main(String[] args) {
		SpringApplication.run(FalconMesApplication.class, args);
	}

}
