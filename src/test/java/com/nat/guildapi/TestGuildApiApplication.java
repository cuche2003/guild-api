package com.nat.guildapi;

import org.springframework.boot.SpringApplication;

public class TestGuildApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(GuildApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
