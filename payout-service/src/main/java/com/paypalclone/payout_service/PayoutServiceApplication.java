package com.paypalclone.payout_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PayoutServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PayoutServiceApplication.class, args);
	}

}
