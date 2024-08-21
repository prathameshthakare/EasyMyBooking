package com.example.ticket_booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TicketBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketBookingApplication.class, args);
	}
}
