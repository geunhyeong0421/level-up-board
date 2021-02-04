package com.gh.levelupboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//@EnableJpaAuditing
@SpringBootApplication
public class LevelUpBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(LevelUpBoardApplication.class, args);
	}

}
