package com.zpsx.NatlexTestTask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class NatlexTestTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(NatlexTestTaskApplication.class, args);
	}

}
