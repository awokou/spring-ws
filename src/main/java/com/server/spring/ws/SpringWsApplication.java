package com.server.spring.ws;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringWsApplication {

    public SpringWsApplication(BitPayService bitPayService) {
        this.bitPayService = bitPayService;
    }

    public static void main(String[] args) {
		SpringApplication.run(SpringWsApplication.class, args);
	}

	private final BitPayService bitPayService;

	@Bean
	public CommandLineRunner run() {
		return args -> bitPayService.persistBitPay();
	}
}
