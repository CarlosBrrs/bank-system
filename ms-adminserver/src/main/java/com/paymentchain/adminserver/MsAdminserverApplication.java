package com.paymentchain.adminserver;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@SpringBootApplication
@EnableAutoConfiguration
@EnableAdminServer
@EnableDiscoveryClient
@EnableWebSecurity
public class MsAdminserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsAdminserverApplication.class, args);
	}

	@Configuration
	public static class SecurityPermitAllConfig {
		@Bean
		public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
			httpSecurity
					.csrf(AbstractHttpConfigurer::disable)
					.authorizeHttpRequests(
							authorize -> authorize.anyRequest().permitAll());
			return httpSecurity.build();
		}

	}
}
